package com.dtt.organization.service.impl;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.EmailReqDto;
import com.dtt.organization.dto.OTPResponseDTO;
import com.dtt.organization.dto.RegisterOrganizationDTO;
import com.dtt.organization.dto.TrustedStakeholderDto;
import com.dtt.organization.dto.TrustedStakeholderRequestDto;
import com.dtt.organization.model.OrganizationDetails;
import com.dtt.organization.model.TrustedStakeholder;
import com.dtt.organization.repository.TrustedStakeholdersRepository;
import com.dtt.organization.service.iface.EOIIface;
import com.dtt.organization.util.AppUtil;
import com.dtt.organization.util.EOISendEmail;
import com.fasterxml.jackson.databind.ObjectMapper;

import ug.daes.DAESService;
import ug.daes.Result;

@Service
public class EOIImpl implements EOIIface {
	
	Logger logger = LoggerFactory.getLogger(EOIImpl.class);
	
	 static final String CLASS = "EOIImpl";
	
	@Value(value = "${nira.api.timetolive}")
	private int timeToLive;
	
	@Value(value = "${email.url}")
	private String emailBaseUrl;
	
	@Value(value = "${eoi.portal.link}")
	private String link;

	private final OrganizationServiceImpl organizationService;
	private final TrustedStakeholdersRepository trustedStakeholdersRepository;
	private final RestTemplate restTemplate;
	private final MessageSource messageSource;

	public EOIImpl(
			OrganizationServiceImpl organizationService,
			TrustedStakeholdersRepository trustedStakeholdersRepository,
			RestTemplate restTemplate,
			MessageSource messageSource) {

		this.organizationService = organizationService;
		this.trustedStakeholdersRepository = trustedStakeholdersRepository;
		this.restTemplate = restTemplate;
		this.messageSource = messageSource;
	}

	Random random = new Random();

	Locale locale = LocaleContextHolder.getLocale();

	public static final String ERROR_SOMETHING_WENT_WRONG = "api.error.something.went.wrong.please.contact.admin";
	public static final String TRUSTED_STAKEHOLDER_NOT_FOUND = "api.error.trusted.stakeHolder.not.found";

	public static final String TRUSTED_STAKEHOLDER_FOUND = "api.response.trusted.stake.holder.found";




	@Override
	public ApiResponses registerTrustedOrganizationEOIPortal(RegisterOrganizationDTO registerOrganizationDTO,
															 String referenceId) {
		try {
			logger.info(CLASS + " registerTrustedOrganizationEOIPortal referenceId {}", referenceId);
			TrustedStakeholder trustedStakeholder = trustedStakeholdersRepository.findByReferenceId(referenceId);
			if (trustedStakeholder == null) {
				return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.invalid.reference.id",null,locale),null);
			} else if (trustedStakeholder.isStatus()) {
				return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.invalid.reference.id.organizatio.already.onboarded",null, locale),
						(Object) null);
			} else {
				if (registerOrganizationDTO.getSpocUgpassEmail() == null) {
					return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.spocmail.cant.be.null",null, locale), (Object) null);
				}
				if (registerOrganizationDTO.getOrgUserList().isEmpty()) {
					return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.orguserlist.cant.be.null",null,locale),
							(Object) null);
				}
				if (!trustedStakeholder.getSpocUgpassEmail().equals(registerOrganizationDTO.getSpocUgpassEmail())) {
					return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.spoc.is.not.authorized.to.onboard.this.organization",null, locale),
							(Object) null);
				} else {
					ApiResponses res = organizationService.registerOrganization(registerOrganizationDTO);
					if (res.isSuccess()) {
						ObjectMapper objectMapper = new ObjectMapper();
						
						
						String s = objectMapper.writerWithDefaultPrettyPrinter()
								.writeValueAsString(res.getResult());
						OrganizationDetails organizationDetails = objectMapper.readValue(s, OrganizationDetails.class);
						trustedStakeholder.setOnboardingTime(AppUtil.getCurrentDate());
						trustedStakeholder.setStatus(true);
						trustedStakeholder.setOrganizationUid(organizationDetails.getOrganizationUid());
						trustedStakeholdersRepository.save(trustedStakeholder);

						sendEmailToSpoc(registerOrganizationDTO.getSpocUgpassEmail());

						return AppUtil.createApiResponse(true,
								messageSource.getMessage("api.response.organization.onboarded.purchase.eseal",null, locale),
								res.getResult());
					}
				}

			}
		}
		catch (Exception e) {
			logger.info( "registerTrustedOrganizationEOIPortal >> ERROR>> {}" ,e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.something.went.wrong.please.try.after.sometime",null,locale), null);
		}
		return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.organization.onboarding.failed",null, locale), (Object) null);
	}

	@Override
	public ApiResponses registerTrustedOrganizationEOI(RegisterOrganizationDTO registerOrganizationDTO) {
		try {
			// 1. Cleaned up the duplicate loggers and used proper format specifiers!
			logger.info("{} :: registerTrustedOrganizationEOI() :: req body {}", CLASS, registerOrganizationDTO);

			if (registerOrganizationDTO == null) {
				return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.request.cant.be.empty", null, locale), null);
			}

			String url = "admin portal api";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Object> requestEntity = new HttpEntity<>(registerOrganizationDTO, headers);

			ResponseEntity<ApiResponses> res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ApiResponses.class);

			// 2. Extract the body to a local variable. This completely satisfies the linter.
			ApiResponses responseBody = res.getBody();

			return switch (res.getStatusCode()) {
				case HttpStatus.OK ->
					// 3. Safely use the local variable
						AppUtil.createApiResponse(true, responseBody != null ? responseBody.getMessage() : null, responseBody);

				case HttpStatus.BAD_REQUEST ->
						AppUtil.createApiResponse(false, "Bad Request", null);
				case HttpStatus.INTERNAL_SERVER_ERROR ->
						AppUtil.createApiResponse(false, "Internal server error", null);
				default ->
						AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG, null, locale), null);
			};

		} catch (Exception e) {
			// 4. Added the 'e' object to the logger so you don't lose your stack trace!
			logger.error("Error in registerTrustedOrganizationEOI: {}", e.getMessage(), e);
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG, null, locale), null);
		}
	}

	@Override
	public ApiResponses sendEmailOTP(String referenceId) {
		try {
			if (referenceId.trim().isEmpty()) {
				return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.reference.id.cant.be.empty",null, locale), null);
			}
			TrustedStakeholder trustedStakeholder = trustedStakeholdersRepository.findByReferenceId(referenceId);
			if (trustedStakeholder == null) {
				return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.invalid.reference.id",null, locale), null);
			}

			String emailOTP = generateOtp(5);
			EmailReqDto emailReqDto = new EmailReqDto();
			emailReqDto.setEmailOtp(emailOTP);

			emailReqDto.setTtl(timeToLive);
			emailReqDto.setEmailId(trustedStakeholder.getSpocUgpassEmail());
			ApiResponses res = organizationService.sendEmailToSubscriber(emailReqDto);

			OTPResponseDTO otpResponseDto = new OTPResponseDTO();
			otpResponseDto.setEmailEncrptyOTP(encryptedString(emailOTP));
			otpResponseDto.setTtl(timeToLive);
			otpResponseDto.setMobileOTP(null);
			otpResponseDto.setEmailOTP(null);

			if (res.isSuccess()) {
				return AppUtil.createApiResponse(true, messageSource.getMessage("api.response.email.send.successfully",null, locale), otpResponseDto);
			} else {
				return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.email.send.failed",null, locale), null);
			}
		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null, locale), null);
		}
	}

	@Override
	public ApiResponses sendEmailToSpoc(String spocEmail) {

		try {
			if (spocEmail == null) {
				return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.spoc.email.cant.be.empty",null, locale), null);
			}
			EmailReqDto emailReqDto = new EmailReqDto();
			emailReqDto.setEmailId(spocEmail);
			ApiResponses res = organizationService.sendEmailToSubscriber(emailReqDto);
			if (res.isSuccess()) {
				return AppUtil.createApiResponse(true, messageSource.getMessage("api.response.email.send.successfully",null, locale), null);
			} else {
				return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.email.send.failed",null,locale), null);
			}
		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null, locale), null);
		}
	}

	public String generateOtp(int maxLength) {
		try {
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			StringBuilder otp = new StringBuilder(maxLength);

			for (int i = 0; i < maxLength; i++) {
				otp.append(secureRandom.nextInt(9));
			}
			return otp.toString();
		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return null;
		}
	}

	private String encryptedString(String s) {
		try {
			Result result = DAESService.encryptData(s);
			return new String(result.getResponse());
		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public ApiResponses addStakeHoldersList(TrustedStakeholderRequestDto requestDto) {

		try {
			logger.info("{} addStakeHolders request {}", CLASS, requestDto);

			if (requestDto == null) {
				return AppUtil.createApiResponse(
						false,
						messageSource.getMessage("api.error.request.cant.be.empty", null, locale),
						null
				);
			}

			List<TrustedStakeholder> stakeholders =
					buildTrustedStakeholders(requestDto.getTrustedStakeholderDtosList());

			trustedStakeholdersRepository.saveAll(stakeholders);

			sendEmailsAsync(stakeholders);

			return AppUtil.createApiResponse(
					true,
					messageSource.getMessage("api.response.trusted.stakeHolder.added.successfully", null, locale),
					null
			);

		} catch (Exception e) {
			logger.error("Error while adding stakeholders {}", e.getMessage(), e);

			return AppUtil.createApiResponse(
					false,
					messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG, null, locale),
					null
			);
		}
	}

	@Override
	public ApiResponses getStakeHolder(String referenceId) {
		try {
			logger.info(CLASS + " getStakeHolder request referenceId {}", referenceId);

			TrustedStakeholder trustedStakeholder = trustedStakeholdersRepository.findByReferenceId(referenceId);
			if(trustedStakeholder != null) {
				return AppUtil.createApiResponse(true, messageSource.getMessage(TRUSTED_STAKEHOLDER_FOUND,null,locale), trustedStakeholder);
			}else {
				return AppUtil.createApiResponse(false, messageSource.getMessage(TRUSTED_STAKEHOLDER_NOT_FOUND,null,locale), null);
			}
		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null,locale), null);
		}
	}

	@Override
	public ApiResponses getAllStakeHolder(String referredBy, String stakeholderType) {
		try {
			logger.info(CLASS+" getAllStakeHolder orgId {} and stake holder type {}", referredBy,stakeholderType);
			List<TrustedStakeholder> trustedStakeholders = null;
			if (!Objects.equals(referredBy, "")) {
				trustedStakeholders = trustedStakeholdersRepository.getAllTrustedStakeHolderByOrgId(referredBy);
				return AppUtil.createApiResponse(true, messageSource.getMessage(TRUSTED_STAKEHOLDER_FOUND,null, locale), trustedStakeholders);
			} else if (!Objects.equals(stakeholderType, "")) {
				trustedStakeholders = trustedStakeholdersRepository
						.getAllTrustedStakeHolderByStakeHolderType(stakeholderType);
				return AppUtil.createApiResponse(true, messageSource.getMessage(TRUSTED_STAKEHOLDER_FOUND,null, locale), trustedStakeholders);
			} else {
				trustedStakeholders = trustedStakeholdersRepository.getAllTrustedStakeHolder();
				return AppUtil.createApiResponse(true, messageSource.getMessage(TRUSTED_STAKEHOLDER_FOUND,null,locale), trustedStakeholders);
			}
		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null,locale), null);
		}
	}

	@Override
	public ApiResponses updateStakeHolder(TrustedStakeholderDto trustedStakeHolder) {
		try {
			logger.info(CLASS + " updateStakeHolder request {}", trustedStakeHolder);
			Optional<TrustedStakeholderDto> stakeHolder = Optional.ofNullable(trustedStakeHolder);
			if (stakeHolder.isPresent()) {
				if (Objects.equals(stakeHolder.get().getReferenceId(), "")) {
					return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.provide.reference.id.for.update.record",null,locale), null);
				} else {
					TrustedStakeholder trustedStake = trustedStakeholdersRepository
							.findByReferenceId(stakeHolder.get().getReferenceId());
					if (trustedStake != null) {
						trustedStake.setName(stakeHolder.get().getName());
						trustedStake.setSpocUgpassEmail(stakeHolder.get().getSpocUgpassEmail());
						trustedStakeholdersRepository.save(trustedStake);
						return AppUtil.createApiResponse(true, messageSource.getMessage("api.response.trusted.update.stakeholder",null, locale), null);
					} else {
						return AppUtil.createApiResponse(false, messageSource.getMessage(TRUSTED_STAKEHOLDER_FOUND,null, locale), null);
					}
				}
			} else {
				return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.trustedStakeHolder.cant.be.null.or.empty",null, locale), null);
			}
		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null, locale), null);
		}
	}

	@Override
	public ApiResponses getStakeHolderList(String spocEmail) {
		try {
			List<TrustedStakeholder> trustedStakeholders = trustedStakeholdersRepository.getStakeHolderList(spocEmail);
			if(trustedStakeholders.isEmpty()) {
				return AppUtil.createApiResponse(false, messageSource.getMessage(TRUSTED_STAKEHOLDER_NOT_FOUND,null, locale), null);
			}else {
				return AppUtil.createApiResponse(true, messageSource.getMessage(TRUSTED_STAKEHOLDER_FOUND,null, locale), trustedStakeholders);
			}
		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR_SOMETHING_WENT_WRONG,null, locale), null);
		}
	}

	public String generateRandomSuffix(int length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(characters.charAt(random.nextInt(characters.length())));
		}
		return sb.toString();
	}


	private List<TrustedStakeholder> buildTrustedStakeholders(List<TrustedStakeholderDto> dtoList) {

		List<TrustedStakeholder> stakeholders = new ArrayList<>();

		for (TrustedStakeholderDto dto : dtoList) {

			validateReferredBy(dto);

			TrustedStakeholder stakeholder = new TrustedStakeholder();
			stakeholder.setReferenceId(generateRandomSuffix(10));
			stakeholder.setName(dto.getName());
			stakeholder.setSpocUgpassEmail(dto.getSpocUgpassEmail());
			stakeholder.setStatus(dto.isStatus());
			stakeholder.setCreationTime(AppUtil.getCurrentDate());
			stakeholder.setStakeholderType(resolveStakeholderType(dto.getStakeholderType()));
			stakeholder.setReferredBy(dto.getReferredBy());

			stakeholders.add(stakeholder);
		}

		return stakeholders;
	}

	private void validateReferredBy(TrustedStakeholderDto dto) {

		if (dto.getReferredBy() == null || dto.getReferredBy().isEmpty()) {
			throw new IllegalArgumentException(
					messageSource.getMessage("api.error.referredby.cant.be.empty", null, locale)
			);
		}
	}

	private String resolveStakeholderType(String stakeholderType) {
		return (stakeholderType == null || stakeholderType.isEmpty())
				? "VENDOR"
				: stakeholderType;
	}

	private void sendEmailsAsync(List<TrustedStakeholder> stakeholders) {

		// The executor is declared inside the try() block.
		// Java will automatically call executor.close() at the end.
		try (ExecutorService executor = Executors.newFixedThreadPool(10)) {

			Runnable task = new EOISendEmail(stakeholders, emailBaseUrl, restTemplate, link);
			executor.execute(task);

		}
	}
}
