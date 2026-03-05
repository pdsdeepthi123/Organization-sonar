package com.dtt.organization.service.impl;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dtt.organization.exception.ExceptionHandlerUtil;
import com.dtt.organization.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.BenificiariesDto;
import com.dtt.organization.dto.BenificiariesRespDto;
import com.dtt.organization.dto.BenificiariesResponseDto;
import com.dtt.organization.enums.SponsorType;
import com.dtt.organization.model.BeneficiariedPrivilegeService;
import com.dtt.organization.model.BeneficiaryInfoView;
import com.dtt.organization.model.BeneficiaryValidity;
import com.dtt.organization.model.Benificiaries;
import com.dtt.organization.model.Subscriber;
import com.dtt.organization.repository.BeneficiariedPrivilegeServiceRepo;
import com.dtt.organization.repository.BeneficiariesRepo;
import com.dtt.organization.repository.BeneficiaryInfoViewRepo;
import com.dtt.organization.repository.BeneficiaryValidityRepository;
import com.dtt.organization.repository.SubscriberFcmTokenRepoIface;
import com.dtt.organization.repository.SubscriberRepository;
import com.dtt.organization.service.iface.BeneficiaryIface;
import com.dtt.organization.util.AppUtil;
import com.dtt.organization.util.LinkedBeneficiraryWorkerThread;

@Service
public class BeneficiaryImpl implements BeneficiaryIface {
	
	static final String CLASS = "BeneficiaryImpl";
	Logger logger = LoggerFactory.getLogger(BeneficiaryImpl.class);
	Locale locale = LocaleContextHolder.getLocale();

	private static final String BENEFICIARY_ALREADY_EXISTS = "api.error.beneficiary.already.exists";
	private static final String ERROR_SOMETHING_WENT_WRONG = "api.error.something.went.wrong.please.contact.admin";
	private static final String ACTIVE ="ACTIVE";
	private static final String SPONSOR_NOT_FOUND ="api.error.sponsor.not.found";
	private static final String SUID_EMPTY ="api.error.suid.is.empty";
	private static final String SPONSOR_FOUND = "api.response.sponsor.found";
	private static final String SUBSCRIBER_NOT_FOUND ="api.error.subscriber.not.found";


	private final BeneficiariesRepo beneficiariesRepo;
	private final BeneficiaryValidityRepository beneficiaryValidityRepository;
	private final BeneficiariedPrivilegeServiceRepo beneficiariedPrivilegeServiceRepo;
	private final SubscriberRepository subscriberRepository;
	private final BeneficiaryInfoViewRepo beneficiaryInfoViewRepo;
	private final SubscriberFcmTokenRepoIface subscriberFcmTokenRepoIface;
	private final MessageSource messageSource;
	private final ExceptionHandlerUtil exceptionHandlerUtil;


	public BeneficiaryImpl(
			BeneficiariesRepo beneficiariesRepo,
			BeneficiaryValidityRepository beneficiaryValidityRepository,
			BeneficiariedPrivilegeServiceRepo beneficiariedPrivilegeServiceRepo,
			SubscriberRepository subscriberRepository,
			BeneficiaryInfoViewRepo beneficiaryInfoViewRepo,
			SubscriberFcmTokenRepoIface subscriberFcmTokenRepoIface,
			MessageSource messageSource,
			ExceptionHandlerUtil exceptionHandlerUtil) {

		this.beneficiariesRepo = beneficiariesRepo;
		this.beneficiaryValidityRepository = beneficiaryValidityRepository;
		this.beneficiariedPrivilegeServiceRepo = beneficiariedPrivilegeServiceRepo;
		this.subscriberRepository = subscriberRepository;
		this.beneficiaryInfoViewRepo = beneficiaryInfoViewRepo;
		this.subscriberFcmTokenRepoIface = subscriberFcmTokenRepoIface;
		this.messageSource = messageSource;
		this.exceptionHandlerUtil = exceptionHandlerUtil;
	}
	@Value("${return.multiple.sponsors}")
	private boolean returnMultipleSponsors;

	@Value("${orgLink.notifyurl}")
	private String sendNotificationURL;

	@Value("${sponsor.linked.msg}")
	private String sponsorLinkedMessage;
	
	@Value("${egp.org.id}")
	private String egpOrgId;




	@Override
	public ApiResponses addBeneficiary(BenificiariesDto dto) {

		try {

			ApiResponses validationResponse = validateBeneficiaryRequest(dto);
			if (validationResponse != null) {
				return validationResponse;
			}

			ApiResponses duplicateResponse = checkDuplicateBeneficiary(dto);
			if (duplicateResponse != null) {
				return duplicateResponse;
			}

			Benificiaries benificiaries = getBenificiaries(dto);
			Benificiaries saved = beneficiariesRepo.save(benificiaries);

			BenificiariesResponseDto responseDto = buildBeneficiaryResponse(dto, saved);

			triggerLinkedBeneficiaryNotification(saved);

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.beneficiary.created.success",
					responseDto);

		} catch (Exception e) {
			logger.error("{} - {} : Exception in addBeneficiary: {}",
					CLASS, Utility.getMethodName(), e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses updateBeneficiary(BenificiariesDto dto) {

		try {

			ApiResponses validationResponse = validateBeneficiaryRequest(dto);
			if (validationResponse != null) {
				return validationResponse;
			}

			ApiResponses duplicateResponse = checkDuplicateBeneficiaryForUpdate(dto);
			if (duplicateResponse != null) {
				return duplicateResponse;
			}

			Benificiaries beneficiary = beneficiariesRepo.findById(dto.getId()).orElse(null);

			if (beneficiary == null) {
				return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.beneficiary.not.found",null,locale), null);
			}

			updateBeneficiaryFields(beneficiary, dto);

			Benificiaries saved = beneficiariesRepo.save(beneficiary);

			BenificiariesResponseDto responseDto = buildBeneficiaryResponse(dto, saved);

			return AppUtil.createApiResponse(true, messageSource.getMessage("api.response.beneficiary.updated",null,locale), responseDto);

		} catch (Exception e) {
			logger.info("{}", e.getMessage());
			return AppUtil.createApiResponse(false,
					messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG, null, locale),
					null);
		}
	}





	private ApiResponses checkDuplicateBeneficiaryForUpdate(BenificiariesDto dto) {

		Benificiaries existing = beneficiariesRepo.findDuplicateBeneficiariesByNIN(
				dto.getSponsorDigitalId(), dto.getBeneficiaryNin());

		if (existing != null && existing.getId() != dto.getId()) {
			return AppUtil.createApiResponse(false, BENEFICIARY_ALREADY_EXISTS, null);
		}

		return null;
	}

	private void updateBeneficiaryFields(Benificiaries entity, BenificiariesDto dto) {

		entity.setBeneficiaryPassport(dto.getBeneficiaryPassport());
		entity.setBeneficiaryType(dto.getBeneficiaryType());
		entity.setUpdatedOn(AppUtil.getDate());
		entity.setSponsorType(dto.getSponsorType());
		entity.setStatus(ACTIVE);
		entity.setSponsorDigitalId(dto.getSponsorDigitalId());
		entity.setSponsorExternalId(dto.getSponsorExternalId());
		entity.setBeneficiaryDigitalId(dto.getBeneficiaryDigitalId());
		entity.setSignaturePhoto(dto.getSignaturePhoto());
		entity.setDesignation(dto.getDesignation());

		entity.setSponsorPaymentPriorityLevel(
				dto.getSponsorType().equals(SponsorType.ORGANIZATION) ? 2 : 1);
	}

	private BenificiariesResponseDto buildBeneficiaryResponse(
			BenificiariesDto dto,
			Benificiaries saved) {

		BenificiariesResponseDto responseDto = new BenificiariesResponseDto();
		responseDto.setBenificiaries(saved);

		List<BeneficiaryValidity> validities = new ArrayList<>();

		for (BeneficiaryValidity validity : dto.getBeneficiaryValidities()) {

			BeneficiaryValidity db = getBeneficiaryValidity(validity, saved);
			beneficiaryValidityRepository.save(db);
			validities.add(db);
		}

		responseDto.setBeneficiaryValidity(validities);

		return responseDto;
	}
	private void triggerLinkedBeneficiaryNotification(Benificiaries beneficiary) {
		// The ExecutorService is declared inside the try() parentheses.
		// Java will automatically close it when the block finishes.
		try (ExecutorService executor = Executors.newFixedThreadPool(1000)) {

			Runnable worker = new LinkedBeneficiraryWorkerThread(
					beneficiary,
					subscriberRepository,
					beneficiariesRepo,
					subscriberFcmTokenRepoIface,
					sendNotificationURL,
					sponsorLinkedMessage);

			executor.execute(worker);
		}
	}
	private static Benificiaries getBenificiaries(BenificiariesDto benificiariesDto) {
		Benificiaries benificiaries = new Benificiaries();

		benificiaries.setBeneficiaryPassport(benificiariesDto.getBeneficiaryPassport());
		benificiaries.setBeneficiaryType(benificiariesDto.getBeneficiaryType());
		benificiaries.setBeneficiaryName(benificiariesDto.getBeneficiaryName());
		benificiaries.setSponsorName(benificiariesDto.getSponsorName());
		benificiaries.setUpdatedOn(AppUtil.getDate());
		benificiaries.setCreatedOn(AppUtil.getDate());
		benificiaries.setSponsorType(benificiariesDto.getSponsorType());
		benificiaries.setStatus(ACTIVE);
		benificiaries.setSponsorDigitalId(benificiariesDto.getSponsorDigitalId());
		benificiaries.setSponsorExternalId(benificiariesDto.getSponsorExternalId());
		benificiaries.setBeneficiaryDigitalId(benificiariesDto.getBeneficiaryDigitalId());
		benificiaries.setSignaturePhoto(benificiariesDto.getSignaturePhoto());
		benificiaries.setDesignation(benificiariesDto.getDesignation());
		benificiaries.setBeneficiaryNin(benificiariesDto.getBeneficiaryNin());
		benificiaries.setBeneficiaryMobileNumber(benificiariesDto.getBeneficiaryMobileNumber());
		benificiaries.setBeneficiaryOfficeEmail(benificiariesDto.getBeneficiaryOfficeEmail());
		benificiaries.setBeneficiaryUgPassEmail(benificiariesDto.getBeneficiaryUgpassEmail());
		benificiaries.setBeneficiaryConsentAcquired(benificiariesDto.isBeneficiaryConsentAcquired());

		if (benificiariesDto.getSponsorType().equals(SponsorType.ORGANIZATION)) {
			benificiaries.setSponsorPaymentPriorityLevel(2);
		} else {
			benificiaries.setSponsorPaymentPriorityLevel(1);
		}
		return benificiaries;
	}

	private static BeneficiaryValidity getBeneficiaryValidity(BeneficiaryValidity validity, Benificiaries benificiariesDb) {
		BeneficiaryValidity validityDb = new BeneficiaryValidity();
		validityDb.setBeneficiaryId(benificiariesDb.getId());
		validityDb.setCreatedOn(AppUtil.getDate());
		validityDb.setUpdatedOn(AppUtil.getDate());
		validityDb.setStatus(ACTIVE);
		validityDb.setPrivilegeServiceId(validity.getPrivilegeServiceId());
		validityDb.setValidityApplicable(validity.isValidityApplicable());
		validityDb.setValidUpTo(validity.getValidUpTo());
		validityDb.setValidFrom(validity.getValidFrom());
		return validityDb;
	}

	@Override
	public ApiResponses findPrivilegeByStatus() {
		try {

			List<BeneficiariedPrivilegeService> privilegeServices =
					beneficiariedPrivilegeServiceRepo.findPrivilegeByStatus();

			if (privilegeServices == null || privilegeServices.isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.privilege.not.found");
			}

			return exceptionHandlerUtil.createSuccessResponse(
					"api.privilege.fetch.success",
					privilegeServices);

		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null,locale), null);
		}
	}

	@Override
	public ApiResponses getAllBeneficiaries() {
		try {
			return AppUtil.createApiResponse(true, messageSource.getMessage("api.response.fetched.beneficiaries",null,locale),
					beneficiariesRepo.findAll());
		}  catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null,locale), null);
		}
	}

	@Override
	public ApiResponses getAllBeneficiariesBySponsor(String sponsorId) {
		try {

			return AppUtil.createApiResponse(true,
					messageSource.getMessage("api.response.fetched.beneficiaries.for.sponsor",null,locale),
					beneficiariesRepo.getAllBeneficiariesBySponsorByDigitalId(sponsorId));

		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null,locale), null);
		}
	}

	@Override
	public ApiResponses getBeneficiaryById(int id) {
		try {
			// 1. Fetch the Optional once to avoid redundant database queries
			Optional<Benificiaries> optionalBeneficiary = beneficiariesRepo.findById(id);

			if (optionalBeneficiary.isEmpty()) {
				return AppUtil.createApiResponse(false,
						messageSource.getMessage("api.error.data.not.found", null, locale), null);
			}

			Benificiaries benificiaries = optionalBeneficiary.get();
			BenificiariesDto benificiariesDto = new BenificiariesDto();

			// DTO Mapping (Removed the duplicate setter calls)
			benificiariesDto.setId(id);
			benificiariesDto.setUpdatedOn(benificiaries.getUpdatedOn());
			benificiariesDto.setStatus(benificiaries.getStatus());
			benificiariesDto.setCreatedOn(benificiaries.getCreatedOn());
			benificiariesDto.setBeneficiaryDigitalId(benificiaries.getBeneficiaryDigitalId());
			benificiariesDto.setSponsorName(benificiaries.getSponsorName());
			benificiariesDto.setBeneficiaryName(benificiaries.getBeneficiaryName());
			benificiariesDto.setBeneficiaryNin(benificiaries.getBeneficiaryNin());
			benificiariesDto.setSponsorType(benificiaries.getSponsorType());
			benificiariesDto.setBeneficiaryMobileNumber(benificiaries.getBeneficiaryMobileNumber());
			benificiariesDto.setBeneficiaryOfficeEmail(benificiaries.getBeneficiaryOfficeEmail());
			benificiariesDto.setBeneficiaryUgpassEmail(benificiaries.getBeneficiaryUgPassEmail());
			benificiariesDto.setBeneficiaryConsentAcquired(benificiaries.isBeneficiaryConsentAcquired());
			benificiariesDto.setSignaturePhoto(benificiaries.getSignaturePhoto());
			benificiariesDto.setDesignation(benificiaries.getDesignation());
			benificiariesDto.setSponsorDigitalId(benificiaries.getSponsorDigitalId());
			benificiariesDto.setBeneficiaryPassport(benificiaries.getBeneficiaryPassport());
			benificiariesDto.setBeneficiaryType(benificiaries.getBeneficiaryType());
			benificiariesDto.setSponsorExternalId(benificiaries.getSponsorExternalId());
			benificiariesDto.setSponsorPaymentPriorityLevel(benificiaries.getSponsorPaymentPriorityLevel());

			// 2. Fetch the validity list ONCE and reuse it
			List<BeneficiaryValidity> beneficiaryValidities = beneficiaryValidityRepository.findAllBeneficiaryValiditybybenefeciaryId(id);

			List<BeneficiariedPrivilegeService> beneficiariedPrivilegeServiceList = new ArrayList<>();
			for (BeneficiaryValidity beneficiaryValidity : beneficiaryValidities) {
				beneficiariedPrivilegeServiceList.add(
						beneficiariedPrivilegeServiceRepo.findPrivilegeById(beneficiaryValidity.getPrivilegeServiceId())
				);
			}

			benificiariesDto.setBeneficiaryValidities(beneficiaryValidities);
			benificiariesDto.setBeneficiariedPrivilegeList(beneficiariedPrivilegeServiceList);

			return AppUtil.createApiResponse(true, messageSource.getMessage("api.response.fetched.success", null, locale), benificiariesDto);

		} catch (Exception e) {
			// Log the actual exception trace alongside the message for better debugging
			logger.error("Error fetching beneficiary by ID: {}", e.getMessage(), e);
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG, null, locale), null);
		}
	}




	@Override
	public ApiResponses dlink(int id) {
		try {
			beneficiariesRepo.changeStatusById(id);
			return AppUtil.createApiResponse(true,
					messageSource.getMessage("api.response.dlink",null,locale), null);

		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null,locale), null);
		}
	}





	@Override
	public ApiResponses linkSponsor(String beneficiaryDigitalId, int id) {
		try {
			if (beneficiaryDigitalId == null || beneficiaryDigitalId.isEmpty()) {
				return AppUtil.createApiResponse(false,
						messageSource.getMessage(SUID_EMPTY,null,locale), null);
			}
			Subscriber subscriberforLink = subscriberRepository.getSubscriberEmail(beneficiaryDigitalId);

			if (subscriberforLink == null) {
				return AppUtil.createApiResponse(false,
					messageSource.getMessage(SUBSCRIBER_NOT_FOUND,null,locale)	,
						null);
			}

			Benificiaries benificiaries = beneficiariesRepo.getReferenceById(id);

            benificiaries.setBeneficiaryNin(subscriberforLink.getNationalId());
			benificiaries.setBeneficiaryDigitalId(beneficiaryDigitalId);
			benificiaries.setBeneficiaryPassport(subscriberforLink.getIdDocNumber());
			benificiaries.setBeneficiaryMobileNumber(subscriberforLink.getMobileNumber());
			benificiaries.setBeneficiaryUgPassEmail(subscriberforLink.getEmailId());
			benificiaries.setBeneficiaryConsentAcquired(true);
			benificiaries.setBeneficiaryName(subscriberforLink.getFullName());
			beneficiariesRepo.save(benificiaries);

			return AppUtil.createApiResponse(true,
					messageSource.getMessage("api.response.beneficiary.consent",null,locale),
					null);

		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null,locale), null);
		}
	}


	@Override
	public ApiResponses verifyOnBoardingSponsor(String suid) {

		try {

			if (isInvalidSuid(suid)) {
				return AppUtil.createApiResponse(false,
						messageSource.getMessage(SUID_EMPTY,null,locale), null);
			}

			Subscriber subscriber = subscriberRepository.findBysubscriberUid(suid);

			if (subscriber == null) {
				return AppUtil.createApiResponse(false,
						messageSource.getMessage(SUBSCRIBER_NOT_FOUND,null,locale), null);
			}

			if (!hasSubscriberIdentifiers(subscriber)) {
				return AppUtil.createApiResponse(false,
						messageSource.getMessage("api.error.data.null",null,locale)
						, null);
			}

			List<Benificiaries> beneficiaries = getBeneficiaries(subscriber);

			if (beneficiaries == null || beneficiaries.isEmpty()) {
				return AppUtil.createApiResponse(false,
						messageSource.getMessage(SPONSOR_NOT_FOUND,null,locale), null);
			}

			filterBeneficiaries(subscriber, beneficiaries);

			if (beneficiaries.isEmpty()) {
				return AppUtil.createApiResponse(false,
						messageSource.getMessage(SPONSOR_NOT_FOUND,null,locale), null);
			}

			if (returnMultipleSponsors) {
				return AppUtil.createApiResponse(true,
						messageSource.getMessage(SPONSOR_FOUND,null,locale), beneficiaries);
			}

			Benificiaries highestPriority = beneficiaries.stream()
					.max(Comparator.comparingInt(Benificiaries::getSponsorPaymentPriorityLevel))
					.orElse(null);

			List<Benificiaries> result = new ArrayList<>();
			result.add(highestPriority);

			return AppUtil.createApiResponse(true,
					messageSource.getMessage(SPONSOR_FOUND,null,locale), result);

		} catch (Exception e) {
			logger.info("{}", e.getMessage());
			return AppUtil.createApiResponse(
					false,
					messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG, null, locale),
					null
			);
		}
	}

	private boolean isInvalidSuid(String suid) {
		return suid == null || suid.isEmpty();
	}

	private boolean hasSubscriberIdentifiers(Subscriber subscriber) {

		return isNotEmpty(subscriber.getSubscriberUid()) ||
				isNotEmpty(subscriber.getEmailId()) ||
				isNotEmpty(subscriber.getIdDocNumber()) ||
				isNotEmpty(subscriber.getNationalId()) ||
				isNotEmpty(subscriber.getMobileNumber());
	}
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	private List<Benificiaries> getBeneficiaries(Subscriber subscriber) {

		List<Benificiaries> beneficiaries = beneficiariesRepo
				.findByEmailOrPassportOrNinOrMobileNumberorBeneficiaryDigitalId(
						subscriber.getEmailId(),
						subscriber.getIdDocNumber(),
						subscriber.getNationalId(),
						subscriber.getMobileNumber(),
						subscriber.getSubscriberUid()
				);

		logger.info("Beneficiaries {}", beneficiaries);
		return beneficiaries;
	}

	private void filterBeneficiaries(Subscriber subscriber, List<Benificiaries> beneficiaries) {

		Iterator<Benificiaries> iterator = beneficiaries.iterator();

		while (iterator.hasNext()) {

			Benificiaries beneficiary = iterator.next();

			boolean remove =
					(isNotEmpty(beneficiary.getBeneficiaryUgPassEmail())
							&& !beneficiary.getBeneficiaryUgPassEmail().equals(subscriber.getEmailId()))
							||
							(isNotEmpty(beneficiary.getBeneficiaryMobileNumber())
									&& !beneficiary.getBeneficiaryMobileNumber().equals(subscriber.getMobileNumber()))
							||
							(isNotEmpty(beneficiary.getBeneficiaryNin())
									&& !beneficiary.getBeneficiaryNin().equals(subscriber.getNationalId()))
							||
							(isNotEmpty(beneficiary.getBeneficiaryPassport())
									&& !beneficiary.getBeneficiaryPassport().equals(subscriber.getIdDocNumber()))
							||
							(isNotEmpty(beneficiary.getBeneficiaryDigitalId())
									&& !beneficiary.getBeneficiaryDigitalId().equals(subscriber.getSubscriberUid()));

			if (remove) {
				iterator.remove();
			}
		}
	}


	@Override
	public ApiResponses getAllSponsersBySuid(String beneficiaryDigitalId) {

		try {

			if (beneficiaryDigitalId == null || beneficiaryDigitalId.isEmpty()) {
				return AppUtil.createApiResponse(false,
						messageSource.getMessage(SUID_EMPTY,null,locale),
						null);
			}

			Subscriber subscriber = subscriberRepository.findBysubscriberUid(beneficiaryDigitalId);

			if (subscriber == null) {
				return AppUtil.createApiResponse(false,
						messageSource.getMessage("api.error.subscribers.not.found",null,locale),
						null);
			}


			if (isSubscriberDataEmpty(subscriber)) {
				return AppUtil.createApiResponse(false,
						messageSource.getMessage("api.error.data.null",null,locale), null);
			}

			List<BeneficiaryInfoView> beneficiaries =
					beneficiaryInfoViewRepo.findByEmailOrPassportOrNinOrMobileNumberOrBeneficiaryDigitalId(
							subscriber.getEmailId(),
							subscriber.getNationalId(),
							subscriber.getIdDocNumber(),
							subscriber.getMobileNumber(),
							beneficiaryDigitalId
					);

			if (beneficiaries == null || beneficiaries.isEmpty()) {
				return AppUtil.createApiResponse(false,
						messageSource.getMessage(SPONSOR_NOT_FOUND,null,locale), null);
			}

			List<BeneficiaryInfoView> filteredBeneficiaries =
					beneficiaries.stream()
							.filter(b -> isValidBeneficiary(b, subscriber))
							.toList();

			if (filteredBeneficiaries.isEmpty()) {
				return AppUtil.createApiResponse(false,
						messageSource.getMessage(SPONSOR_NOT_FOUND,null,locale), null);
			}

			return AppUtil.createApiResponse(true,
					messageSource.getMessage(SPONSOR_FOUND,null,locale), filteredBeneficiaries);

		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null,locale), null);
		}
	}

	private boolean isSubscriberDataEmpty(Subscriber subscriber) {
		return subscriber.getNationalId() == null &&
				subscriber.getEmailId() == null &&
				subscriber.getIdDocNumber() == null &&
				subscriber.getSubscriberUid() == null;
	}

	private boolean isValidBeneficiary(BeneficiaryInfoView b, Subscriber subscriber) {

		if (b.getBeneficiaryUgPassEmail() != null &&
				!b.getBeneficiaryUgPassEmail().equals(subscriber.getEmailId())) {
			return false;
		}

		if (b.getBeneficiaryMobileNumber() != null &&
				!b.getBeneficiaryMobileNumber().equals(subscriber.getMobileNumber())) {
			return false;
		}

		if (b.getBeneficiaryNin() != null &&
				!b.getBeneficiaryNin().equals(subscriber.getNationalId())) {
			return false;
		}

		if (b.getBeneficiaryPassport() != null &&
				!b.getBeneficiaryPassport().equals(subscriber.getIdDocNumber())) {
			return false;
		}

        return b.getBeneficiaryDigitalId() == null ||
                b.getBeneficiaryDigitalId().equals(subscriber.getSubscriberUid());
    }

	@Override
	public ApiResponses changeStatusForSSP(int id) {
		try {

			beneficiariesRepo.changeStatusForSSP(id);
			return AppUtil.createApiResponse(true,
					messageSource.getMessage("api.response.activated.success",null,locale),
					null);

		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null,locale), null);
		}
	}

	@Override
	public ApiResponses addMultipleBeneficiaries(List<BenificiariesDto> dtoList) {

		try {

			logger.info("{} addMultipleBeneficiaries {}", CLASS, dtoList);

			if (dtoList == null || dtoList.isEmpty()) {
				return AppUtil.createApiResponse(
						false,
						messageSource.getMessage("api.error.beneficiary.list.empty", null, locale),
						null
				);
			}

			List<BenificiariesRespDto> responseList = new ArrayList<>();

			for (BenificiariesDto dto : dtoList) {

				ApiResponses validationResponse = validateBeneficiaryRequest(dto);
				if (validationResponse != null) {
					return validationResponse;
				}

				ApiResponses duplicateResponse = checkDuplicateBeneficiary(dto);
				if (duplicateResponse != null) {
					return duplicateResponse;
				}

				Benificiaries beneficiary = beneficiariesRepo.save(getBenificiaries1(dto));

				List<BeneficiaryValidity> validities = saveBeneficiaryValidities(dto, beneficiary);

				responseList.add(buildResponseDto(beneficiary, validities));

				sendLinkedBeneficiaryNotification(beneficiary);
			}

			return AppUtil.createApiResponse(
					true,
					messageSource.getMessage("api.beneficiary.created.success", null, locale),
					responseList
			);

		} catch (Exception e) {

			logger.error("Error adding beneficiaries", e);

			return AppUtil.createApiResponse(
					false,
					messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG, null, locale),
					null
			);
		}
	}

	private static Benificiaries getBenificiaries1(BenificiariesDto benificiariesDto) {
		Benificiaries benificiaries = new Benificiaries();

		benificiaries.setBeneficiaryPassport(benificiariesDto.getBeneficiaryPassport());

		benificiaries.setBeneficiaryType(benificiariesDto.getBeneficiaryType());

		benificiaries.setBeneficiaryName(benificiariesDto.getBeneficiaryName());
		benificiaries.setSponsorName(benificiariesDto.getSponsorName());
		benificiaries.setUpdatedOn(AppUtil.getDate());
		benificiaries.setCreatedOn(AppUtil.getDate());

		benificiaries.setSponsorType(benificiariesDto.getSponsorType());

		benificiaries.setStatus(ACTIVE);
		benificiaries.setSponsorDigitalId(benificiariesDto.getSponsorDigitalId());
		benificiaries.setSponsorExternalId(benificiariesDto.getSponsorExternalId());
		benificiaries.setBeneficiaryDigitalId(benificiariesDto.getBeneficiaryDigitalId());
		benificiaries.setSignaturePhoto(benificiariesDto.getSignaturePhoto());
		benificiaries.setDesignation(benificiariesDto.getDesignation());

		if (benificiariesDto.getSponsorType().equals(SponsorType.ORGANIZATION)) {
			benificiaries.setSponsorPaymentPriorityLevel(2);
		} else {
			benificiaries.setSponsorPaymentPriorityLevel(1);
		}
		benificiaries.setBeneficiaryNin(benificiariesDto.getBeneficiaryNin());
		benificiaries.setBeneficiaryMobileNumber(benificiariesDto.getBeneficiaryMobileNumber());
		benificiaries.setBeneficiaryOfficeEmail(benificiariesDto.getBeneficiaryOfficeEmail());
		benificiaries.setBeneficiaryUgPassEmail(benificiariesDto.getBeneficiaryUgpassEmail());
		benificiaries.setBeneficiaryConsentAcquired(false);
		return benificiaries;
	}

	@Override
	public ApiResponses getVendorsByVendorId(String vendorId) {
		try{

			List<BeneficiaryInfoView> res = beneficiaryInfoViewRepo.getVendorsByVendorId(vendorId);
			if(res==null){
				return AppUtil.createApiResponse(false,
						messageSource.getMessage("api.error.vendor.not.found",null,locale), null);
			}

			return AppUtil.createApiResponse(true,
					messageSource.getMessage("api.response.vendors.fetched",null,locale), res);

		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null,locale), null);
		}
	}


	@Override
	public ApiResponses linkAllSponsor(BenificiariesDto benificiariesDto) {
		try {
			if (benificiariesDto.getBeneficiaryDigitalId() == null
					&& benificiariesDto.getBeneficiaryDigitalId().isEmpty()) {
				return AppUtil.createApiResponse(false,
						messageSource.getMessage(SUID_EMPTY,null,locale),
						null);
			} else {
				Subscriber subscriber = subscriberRepository
						.findBysubscriberUid(benificiariesDto.getBeneficiaryDigitalId());
				if (subscriber != null) {
					List<Benificiaries> beneficiariesList = beneficiariesRepo.findAllSponsor(
							subscriber.getEmailId(), subscriber.getIdDocNumber(),
							subscriber.getNationalId(), subscriber.getMobileNumber(),
							subscriber.getSubscriberUid());
					List<Benificiaries> beneficiaries2 = new ArrayList<>();
					for(Benificiaries beneficiaries : beneficiariesList) {
						beneficiaries.setBeneficiaryDigitalId(subscriber.getSubscriberUid());
						beneficiaries.setBeneficiaryName(subscriber.getFullName());
						beneficiaries.setBeneficiaryPassport(subscriber.getIdDocNumber());
						beneficiaries.setBeneficiaryNin(subscriber.getNationalId());
						beneficiaries.setBeneficiaryUgPassEmail(subscriber.getEmailId());
						beneficiaries.setBeneficiaryMobileNumber(subscriber.getMobileNumber());
						beneficiaries.setBeneficiaryConsentAcquired(true);
						beneficiaries2.add(beneficiaries);
					}
					beneficiariesRepo.saveAll(beneficiaries2);
					return AppUtil.createApiResponse(true,
							messageSource.getMessage("api.response.beneficiary.consent",null,locale),
							beneficiaries2);
					
				} else {
					return AppUtil.createApiResponse(false, messageSource.getMessage(SUBSCRIBER_NOT_FOUND,null,locale), null);
				}

			}
		}  catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null,locale), null);
		}
	}

	
	@Override
	public ApiResponses verifyByEgpForVendor(String vendorId, String orgid) {
	    try {

			if (egpOrgId.equals(orgid)) {
				Thread.sleep(2000);
				return AppUtil.createApiResponse(true,
						messageSource.getMessage("api.response.vendor.verified.successfully",null,locale),
						null);
			} else {
				Thread.sleep(2000);
				return AppUtil.createApiResponse(false,
						messageSource.getMessage("api.error.vendor.not.verified",null,locale), null);
			}

		}catch (InterruptedException e) {

			Thread.currentThread().interrupt();
			logger.error("Thread interrupted while verifying vendor", e);

			return AppUtil.createApiResponse(
					false,
					messageSource.getMessage(
							ERROR_SOMETHING_WENT_WRONG,
							null,
							locale
					),
					null
			);
		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG, null,
					locale), null);
		}

	}


	private ApiResponses validateBeneficiaryRequest(BenificiariesDto dto) {

		if (dto.getSponsorDigitalId() == null ||
				dto.getSponsorType() == null ||
				dto.getBeneficiaryType() == null) {

			return AppUtil.createApiResponse(
					false,
					messageSource.getMessage("api.error.beneficiary.required.fields", null, locale),
					null
			);
		}

		if (dto.getBeneficiaryDigitalId() == null &&
				dto.getBeneficiaryNin() == null &&
				dto.getBeneficiaryPassport() == null &&
				dto.getBeneficiaryUgpassEmail() == null &&
				dto.getBeneficiaryMobileNumber() == null) {

			return AppUtil.createApiResponse(
					false,
					messageSource.getMessage("api.error.beneficiary.identity.required", null, locale),
					null
			);
		}

		if (dto.getBeneficiaryValidities() == null || dto.getBeneficiaryValidities().isEmpty()) {

			return AppUtil.createApiResponse(
					false,
					messageSource.getMessage("api.error.beneficiary.service.required", null, locale),
					dto
			);
		}

		return null;
	}

	private ApiResponses checkDuplicateBeneficiary(BenificiariesDto dto) {

		String sponsorId = dto.getSponsorDigitalId();

		if (dto.getBeneficiaryNin() != null &&
				beneficiariesRepo.findDuplicateBeneficiariesByNIN(sponsorId, dto.getBeneficiaryNin()) != null) {

			return AppUtil.createApiResponse(
					false,
					messageSource.getMessage(BENEFICIARY_ALREADY_EXISTS, null, locale),
					null
			);
		}

		if (dto.getBeneficiaryPassport() != null &&
				beneficiariesRepo.findDuplicateBeneficiariesByPassport(sponsorId, dto.getBeneficiaryPassport()) != null) {

			return AppUtil.createApiResponse(
					false,
					messageSource.getMessage(BENEFICIARY_ALREADY_EXISTS, null, locale),
					null
			);
		}

		if (dto.getBeneficiaryMobileNumber() != null &&
				beneficiariesRepo.findDuplicateBeneficiariesByMobileNumber(sponsorId, dto.getBeneficiaryMobileNumber()) != null) {

			return AppUtil.createApiResponse(
					false,
					messageSource.getMessage(BENEFICIARY_ALREADY_EXISTS, null, locale),
					null
			);
		}

		return null;
	}


	private void sendLinkedBeneficiaryNotification(Benificiaries beneficiary) {

		// The ExecutorService is instantiated inside the try() parentheses.
		try (ExecutorService executor = Executors.newFixedThreadPool(10)) {

			Runnable worker = new LinkedBeneficiraryWorkerThread(
					beneficiary,
					subscriberRepository,
					beneficiariesRepo,
					subscriberFcmTokenRepoIface,
					sendNotificationURL,
					sponsorLinkedMessage
			);

			executor.execute(worker);
		} // Java automatically calls executor.close() right here
	}
	private BenificiariesRespDto buildResponseDto(Benificiaries beneficiary,
												  List<BeneficiaryValidity> validities) {

		BenificiariesResponseDto responseDto = new BenificiariesResponseDto();
		responseDto.setBenificiaries(beneficiary);
		responseDto.setBeneficiaryValidity(validities);

		BenificiariesRespDto respDto = new BenificiariesRespDto();
		respDto.setBenificiariesResponseDtos(responseDto);

		return respDto;
	}
	private List<BeneficiaryValidity> saveBeneficiaryValidities(BenificiariesDto dto, Benificiaries beneficiary) {

		List<BeneficiaryValidity> validities = new ArrayList<>();

		for (BeneficiaryValidity validity : dto.getBeneficiaryValidities()) {

			BeneficiaryValidity dbValidity = getBeneficiaryValidity(validity, beneficiary);
			beneficiaryValidityRepository.save(dbValidity);

			validities.add(dbValidity);
		}

		return validities;
	}
}
