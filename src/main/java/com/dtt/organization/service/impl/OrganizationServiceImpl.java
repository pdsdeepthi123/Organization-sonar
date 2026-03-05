package com.dtt.organization.service.impl;

import java.io.InputStream;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.*;

import com.dtt.organization.constant.Constant;
import com.dtt.organization.model.*;
import jakarta.transaction.Transactional;

import com.dtt.organization.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.exception.ExceptionHandlerUtil;
import com.dtt.organization.exception.OrgnizationServiceException;

import com.dtt.organization.repository.OrgContactsEmailRepository;
import com.dtt.organization.repository.OrgEmailDomainRepository;
import com.dtt.organization.repository.OrganizationCertificatesRepository;
import com.dtt.organization.repository.OrganizationDetailsRepository;
import com.dtt.organization.repository.OrganizationDirectorsRepository;
import com.dtt.organization.repository.OrganizationDocumentsCheckBoxRepository;
import com.dtt.organization.repository.OrganizationSignatureTemplatesRepository;
import com.dtt.organization.repository.OrganizationStatusRepository;
import com.dtt.organization.repository.SignatureTemplatesRepository;
import com.dtt.organization.repository.SubscriberCertificatesRepoIface;
import com.dtt.organization.repository.SubscriberCompleteDetailRepoIface;
import com.dtt.organization.repository.SubscriberOnboardingDataRepoIface;
import com.dtt.organization.repository.SubscriberRepository;
import com.dtt.organization.repository.SubscriberStatusRepository;
import com.dtt.organization.repository.SubscriberViewRepository;
import com.dtt.organization.repository.WalletSignCertRepo;
import com.dtt.organization.request.entity.SignatureVerificationContext1;
import com.dtt.organization.service.iface.OrganizationIface;
import com.dtt.organization.util.AppUtil;
import com.dtt.organization.util.NativeUtils;
import com.dtt.organization.util.Utility;
import com.dtt.organization.util.ValidationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import ug.daes.DAESService;
import ug.daes.Result;

@Service
public class OrganizationServiceImpl implements OrganizationIface {

	Locale locale = LocaleContextHolder.getLocale();

	private static final String CLASS = OrganizationServiceImpl.class.getSimpleName();
	Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);

	/**
	 * The object mapper.
	 */
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Value(value = "${send.email.url}")
	private String sendEmail;

	@Value(value = "${email.url}")
	private String emailBaseUrl;

	@Value(value = "${nira.api.timetolive}")
	private int timeToLive;

	@Value("${verify.url}")
	private String baseUrl;

	@Value("${orgLink.notifyurl}")
	private String orgLinkUrl;

	@Value(value = "${spring.mail.host}")
	private String emailHost;

	@Value(value = "${spring.mail.username}")
	private String emailUserName;

	@Value(value = "${spring.mail.password}")
	private String emailPassword;

	@Value(value = "${spring.mail.port}")
	private int emailPort;

	@Value(value = "${send.log}")
	private String sendLog;

	@Value(value = "${sample.spoc.name}")
	private String sampleSPOCName;

	@Value(value = "${sample.spoc.mobile}")
	private String sampleSPOCMobile;

	private final OrganizationDetailsRepository organizationDetailsRepository;

	private final OrgContactsEmailRepository orgContactsEmailRepository;

	private final OrganizationDirectorsRepository organizationDirectorsRepository;

	private final OrganizationDocumentsCheckBoxRepository organizationDocumentsCheckBoxRepository;

	private final SubscriberRepository subscriberRepository;

	private final SubscriberStatusRepository subscriberStatusRepository;

	private final OrganizationStatusRepository organizationStatusRepository;

	private final SignatureTemplatesRepository signatureTemplatesRepository;

	private final SubscriberCertificatesRepoIface subscriberCertificatesRepoIface;

	private final OrganizationSignatureTemplatesRepository organizationSignatureTemplatesRepository;

	private final SubscriberCompleteDetailRepoIface subscriberCompleteDetailRepoIface;
	private final OrgEmailDomainRepository orgEmailDomainRepository;

	private final SubscriberViewRepository subscriberViewRepository;

	private final OrganizationCertificatesRepository organizationCertificatesRepository;

	private final SubscriberOnboardingDataRepoIface subscriberOnboardingDataRepoIface;

	private final WalletSignCertRepo walletSignCertRepo;

	private final MessageSource messageSource;
	private final RestTemplate restTemplate;
	private final ExceptionHandlerUtil exceptionHandlerUtil;

	public OrganizationServiceImpl(OrganizationDetailsRepository organizationDetailsRepository,
								   OrgContactsEmailRepository orgContactsEmailRepository,
								   OrganizationDirectorsRepository organizationDirectorsRepository,
								   OrganizationDocumentsCheckBoxRepository organizationDocumentsCheckBoxRepository,
								   SubscriberRepository subscriberRepository, SubscriberStatusRepository subscriberStatusRepository,
								   OrganizationStatusRepository organizationStatusRepository,
								   SignatureTemplatesRepository signatureTemplatesRepository,
								   SubscriberCertificatesRepoIface subscriberCertificatesRepoIface,
								   OrganizationSignatureTemplatesRepository organizationSignatureTemplatesRepository,
								   SubscriberCompleteDetailRepoIface subscriberCompleteDetailRepoIface,
								   OrgEmailDomainRepository orgEmailDomainRepository, SubscriberViewRepository subscriberViewRepository,
								   OrganizationCertificatesRepository organizationCertificatesRepository,
								   SubscriberOnboardingDataRepoIface subscriberOnboardingDataRepoIface, WalletSignCertRepo walletSignCertRepo,
								   MessageSource messageSource, RestTemplate restTemplate, ExceptionHandlerUtil exceptionHandlerUtil) {
		super();
		this.organizationDetailsRepository = organizationDetailsRepository;
		this.orgContactsEmailRepository = orgContactsEmailRepository;
		this.organizationDirectorsRepository = organizationDirectorsRepository;
		this.organizationDocumentsCheckBoxRepository = organizationDocumentsCheckBoxRepository;
		this.subscriberRepository = subscriberRepository;
		this.subscriberStatusRepository = subscriberStatusRepository;
		this.organizationStatusRepository = organizationStatusRepository;
		this.signatureTemplatesRepository = signatureTemplatesRepository;
		this.subscriberCertificatesRepoIface = subscriberCertificatesRepoIface;
		this.organizationSignatureTemplatesRepository = organizationSignatureTemplatesRepository;
		this.subscriberCompleteDetailRepoIface = subscriberCompleteDetailRepoIface;
		this.orgEmailDomainRepository = orgEmailDomainRepository;
		this.subscriberViewRepository = subscriberViewRepository;
		this.organizationCertificatesRepository = organizationCertificatesRepository;
		this.subscriberOnboardingDataRepoIface = subscriberOnboardingDataRepoIface;
		this.walletSignCertRepo = walletSignCertRepo;
		this.messageSource = messageSource;
		this.restTemplate = restTemplate;
		this.exceptionHandlerUtil = exceptionHandlerUtil;
	}

	public String generateOrganizationUniqueId() {
		UUID ouid = UUID.randomUUID();
		return ouid.toString();
	}




	@Transactional
	@Override
	public ApiResponses registerOrganization(RegisterOrganizationDTO registerOrganizationDTO) {
		try {
			String result= ValidationUtil.validate(registerOrganizationDTO);
			if(result!=null){
				throw new OrgnizationServiceException(result);
			}
			if (Objects.isNull(registerOrganizationDTO)) {
				logger.error("{} - {} : Organization details can't be null", CLASS, Utility.getMethodName());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGANIZATION_DETAILS_CANT_BE_NULL);
			}

			String ouid = generateOrganizationUniqueId();

			Optional<OrganizationDetails> orgDetails = Optional.ofNullable(organizationDetailsRepository
					.getOrgnizationDetailsByName(registerOrganizationDTO.getOrganizationName().trim()));
			if (orgDetails.isPresent()) {
				logger.warn("{} - {} : Organization name already used: {}", CLASS, Utility.getMethodName(),
						registerOrganizationDTO.getOrganizationName());
				return exceptionHandlerUtil
						.createErrorResponse(Constant.API_ERROR_ORGANIZATION_NAME_ALREADY_USED_PLEASE_CHOOSE_ANOTHER);
			}

			OrganizationDetails organizationDetails = buildOrganizationDetails(registerOrganizationDTO, ouid);
			organizationDetailsRepository.save(organizationDetails);

			saveOrganizationStatus(organizationDetails);
			saveEmailDomain(registerOrganizationDTO, organizationDetails);
			saveSignatureTemplates(registerOrganizationDTO, organizationDetails);
			saveOrgUsers(registerOrganizationDTO, organizationDetails);

			if (registerOrganizationDTO.getDirectorsEmailList() != null) {
				saveOrganizationDirectors(registerOrganizationDTO, organizationDetails);
			}

			saveDocumentCheckBoxes(registerOrganizationDTO, organizationDetails);

			sendConfirmationEmail(registerOrganizationDTO);

			logger.info("{} - {} : Organization Registered Successfully", CLASS, Utility.getMethodName());




			ClassPathResource resource = new ClassPathResource("static/sample-org-logo.png");

			byte[] fileBytes;
			try (InputStream inputStream = resource.getInputStream()) {
				fileBytes = inputStream.readAllBytes();
			}

			Map<String, Object> logBody = new HashMap<>();
			logBody.put("orgId", organizationDetails.getOrganizationUid());
			logBody.put("orgName", organizationDetails.getOrganizationName());
			logBody.put("spocEmail", organizationDetails.getSpocUgpassEmail());
			logBody.put("orgLogo", Base64.getEncoder().encodeToString(fileBytes));
			logBody.put("spocName", sampleSPOCName);
			logBody.put("spocMobileNumber", sampleSPOCMobile);

			sendLog(logBody);




			logger.info("{} - {} : Organization Registered Successfully", CLASS, Utility.getMethodName());
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ORGANIZATION_REGISTERD, organizationDetails);

		}catch (OrgnizationServiceException o){
			logger.error("{} - {} : OrgnizationServiceException occurred during organization registration: {}", CLASS,
					Utility.getMethodName(), o.getMessage());
			return exceptionHandlerUtil.handleException(o);
		}
		catch (Exception e) {
			logger.error("{} - {} : Exception occurred during organization registration: {}", CLASS,
					Utility.getMethodName(), e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	private void sendLog(Map<String, Object> logBody) {
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Object> requestEntity = new HttpEntity<>(logBody, headers);

			ResponseEntity<Object> res = restTemplate.exchange(sendLog, HttpMethod.POST, requestEntity,
					Object.class);
			if (res.getStatusCode().is2xxSuccessful()) {
				logger.info("{} - {}: Log sent successfully", CLASS, Utility.getMethodName());
			} else {
				logger.error("{} - {}: Log send failed with status: {}",
						CLASS,
						Utility.getMethodName(),
						res.getStatusCode().value());
			}

		} catch (Exception e) {
			logger.error("{} - {} - {} : Error sending log ", CLASS, Utility.getMethodName(),
					e.getMessage());
		}
	}
	private OrganizationDetails buildOrganizationDetails(RegisterOrganizationDTO registerOrganizationDTO, String ouid) {
		OrganizationDetails organizationDetails = new OrganizationDetails();
		organizationDetails.setOrganizationUid(ouid);
		organizationDetails.setOrganizationName(registerOrganizationDTO.getOrganizationName().trim());
		organizationDetails.setOrganizationEmail(registerOrganizationDTO.getOrganizationEmail());
		organizationDetails.seteSealImage(registerOrganizationDTO.geteSealImage());
		organizationDetails
				.setAuthorizedLetterForSignatories(registerOrganizationDTO.getAuthorizedLetterForSignatories());
		organizationDetails.setCorporateOfficeAddress(registerOrganizationDTO.getCorporateOfficeAddress());
		organizationDetails.setIncorporation(registerOrganizationDTO.getIncorporation());
		organizationDetails.setOtherLegalDocument(registerOrganizationDTO.getOtherLegalDocument());
		organizationDetails.setOtherESealDocument(registerOrganizationDTO.getOtherESealDocument());
		organizationDetails.setSignedPdf(registerOrganizationDTO.getSignedPdf());
		organizationDetails.setTax(registerOrganizationDTO.getTax());
		organizationDetails.setTaxNo(registerOrganizationDTO.getTaxNo());
		organizationDetails.setUniqueRegdNo(registerOrganizationDTO.getUniqueRegdNo());
		organizationDetails.setSegment(registerOrganizationDTO.getSegment());
		organizationDetails.setStatus(Constant.REGISTERED);
		organizationDetails.setCreatedBy(registerOrganizationDTO.getCreatedBy());
		organizationDetails.setUpdatedBy(registerOrganizationDTO.getUpdatedBy());
		organizationDetails.setCreatedOn(AppUtil.getDate());
		organizationDetails.setUpdatedOn(AppUtil.getDate());
		organizationDetails.setEnablePostPaidOption(registerOrganizationDTO.isEnablePostPaidOption());
		organizationDetails.setSpocUgpassEmail(registerOrganizationDTO.getSpocUgpassEmail());
		organizationDetails.setAgentUrl(registerOrganizationDTO.getAgentUrl());
		organizationDetails.setManageByAdmin(true);

		return organizationDetails;
	}

	private void saveOrganizationStatus(OrganizationDetails organizationDetails) throws ParseException {
		OrganizationStatusModel organizationStatus = new OrganizationStatusModel();
		organizationStatus.setOrganizationUid(organizationDetails.getOrganizationUid());
		organizationStatus.setCreatedDate(NativeUtils.getTimeStamp());
		organizationStatus.setOrganizationStatus(Constant.ACTIVE);
		organizationStatus.setOrganizationStatusDescription(Constant.ORGANIZATION_IS_ACTIVE);
		organizationStatus.setUpdatedDate(NativeUtils.getTimeStamp());
		organizationStatusRepository.save(organizationStatus);
	}

	private void saveEmailDomain(RegisterOrganizationDTO registerOrganizationDTO,
								 OrganizationDetails organizationDetails) {
		OrganizationEmailDomain organizationEmailDomain = new OrganizationEmailDomain();
		organizationEmailDomain.setOrganizationUid(organizationDetails.getOrganizationUid());
		organizationEmailDomain.setEmailDomain(registerOrganizationDTO.getEmailDomain());
		organizationEmailDomain.setStatus(registerOrganizationDTO.isDomainStatus());
		organizationEmailDomain.setCreatedOn(AppUtil.getDate());
		organizationEmailDomain.setUpdatedOn(AppUtil.getDate());
		orgEmailDomainRepository.save(organizationEmailDomain);
	}

	private void saveSignatureTemplates(RegisterOrganizationDTO registerOrganizationDTO,
										OrganizationDetails organizationDetails) throws NoSuchMessageException, OrgnizationServiceException {
		if (registerOrganizationDTO.getTemplateId() == null || registerOrganizationDTO.getTemplateId().isEmpty()) {
			logger.error("{} - {} : Template list cannot be empty", CLASS, Utility.getMethodName());
			throw new OrgnizationServiceException(
					messageSource.getMessage(Constant.API_RESPONSE_TEMPLATE_CANT_EMPTY, null, locale));
		}

		List<OrganizationSignatureTemplates> list = new ArrayList<>();
		for (Integer templateId : registerOrganizationDTO.getTemplateId()) {
			OrganizationSignatureTemplates signatureTemplates = new OrganizationSignatureTemplates();
			signatureTemplates.setOrganizationUid(organizationDetails.getOrganizationUid());
			signatureTemplates.setTemplateId(templateId);
			signatureTemplates.setType(templateId == 5 || templateId == 6 || templateId == 7 ? "ESEAL" : "SIGN");
			list.add(signatureTemplates);
		}
		organizationSignatureTemplatesRepository.saveAll(list);
	}

	private void saveOrgUsers(RegisterOrganizationDTO registerOrganizationDTO, OrganizationDetails organizationDetails)
			throws OrgnizationServiceException {
		if (registerOrganizationDTO.getOrgUserList() != null && !registerOrganizationDTO.getOrgUserList().isEmpty()) {
			List<OrgContactsEmail> contactsEmails = new ArrayList<>();
			HashSet<String> emailSet = new HashSet<>();

			for (OrgUser orgUser : registerOrganizationDTO.getOrgUserList()) {
				// Check for duplicate employee emails
				if (!emailSet.add(orgUser.getEmployeeEmail())) {
					logger.warn("{} - {} : Duplicate employee email found: {}", CLASS, Utility.getMethodName(),
							orgUser.getEmployeeEmail());
					throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_RESPONSE_EMPLOYEE_MAIL_UNIQUE);
				}


				OrgContactsEmail contactsEmail = buildOrgContactsEmail(orgUser,
						organizationDetails.getOrganizationUid());
				contactsEmails.add(contactsEmail);
			}
			orgContactsEmailRepository.saveAll(contactsEmails);
		}
	}

	private void handleUserNotifications(OrgUser orgUser) {
		try {

			SubscriberView subscriberView = null;

			if (orgUser.getUgpassEmail() != null && !orgUser.getUgpassEmail().isEmpty()) {

				subscriberView = subscriberViewRepository.findByUgpassMail(orgUser.getUgpassEmail());
			} else if (orgUser.getMobileNumber() != null && !orgUser.getMobileNumber().isEmpty()) {
				subscriberView = subscriberViewRepository.findByMobile(orgUser.getMobileNumber());
			} else if (orgUser.getPassportNumber() != null && !orgUser.getPassportNumber().isEmpty()) {
				subscriberView = subscriberViewRepository.findByIdDocNumber(orgUser.getPassportNumber());
			} else if (orgUser.getNationalIdNumber() != null && !orgUser.getNationalIdNumber().isEmpty()) {
				subscriberView = subscriberViewRepository.findByIdDocNumber(orgUser.getNationalIdNumber());
			}

			if (subscriberView != null) {
				sendNotification(subscriberView.getDisplayName(), subscriberView.getFcmToken(), true);
			}
		} catch (Exception e) {
			logger.error("{} - {}: Error handling user notifications for email: {}: {}", CLASS, Utility.getMethodName(),
					orgUser.getEmployeeEmail(), e.getMessage());
		}
	}

	private OrgContactsEmail buildOrgContactsEmail(OrgUser orgUser, String organizationUid) {
		OrgContactsEmail orgContactsEmailDB = orgContactsEmailRepository.getOrganisationByUidAndEmail(organizationUid, orgUser.getEmployeeEmail());
		if (orgContactsEmailDB == null) {
			OrgContactsEmail contactsEmail = new OrgContactsEmail();
			contactsEmail.setOrganizationUid(organizationUid);
			contactsEmail.setEmployeeEmail(orgUser.getEmployeeEmail());
			contactsEmail.setUgpassEmail(orgUser.getUgpassEmail());
			contactsEmail.setSignaturePhoto(processSignaturePhoto(orgUser.getSignaturePhoto()));
			contactsEmail.setDesignation(orgUser.getDesignation());
			contactsEmail.setStatus(Constant.ACTIVE);

			contactsEmail.setInitial(orgUser.getInitial());
			contactsEmail.setMobileNumber(orgUser.getMobileNumber());
			contactsEmail.setNationalIdNumber(orgUser.getNationalIdNumber());
			contactsEmail.setPassportNumber(orgUser.getPassportNumber());
			contactsEmail.seteSealSignatory(orgUser.iseSealSignatory());
			contactsEmail.setDigitalFormPrivilege(orgUser.isDigitalFormPrivilege());
			contactsEmail.seteSealPreparatory(orgUser.iseSealPrepatory());
			contactsEmail.setTemplate(orgUser.isTemplate());
			contactsEmail.setDelegate(orgUser.isDelegate());
			contactsEmail.setBulksign(orgUser.isBulksign());
			handleUserNotifications(orgUser);
			return contactsEmail;
		} else {
			OrgContactsEmail contactsEmail = new OrgContactsEmail();
			contactsEmail.setOrganizationUid(organizationUid);
			contactsEmail.setEmployeeEmail(orgUser.getEmployeeEmail());
			contactsEmail.setUgpassEmail(orgUser.getUgpassEmail());
			contactsEmail.setSignaturePhoto(processSignaturePhoto(orgUser.getSignaturePhoto()));
			contactsEmail.setDesignation(orgUser.getDesignation());
			contactsEmail.setStatus(Constant.ACTIVE);
			if (orgContactsEmailDB.getUgpassEmail() != null && !orgContactsEmailDB.getUgpassEmail().isEmpty()) {
				if (orgContactsEmailDB.getUgpassEmail().equals(orgUser.getUgpassEmail())) {
					contactsEmail.setUgpassUserLinkApproved(orgContactsEmailDB.isUgpassUserLinkApproved());
				} else {
					contactsEmail.setUgpassUserLinkApproved(false);
					handleUserNotifications(orgUser);
				}
			} else {
				contactsEmail.setUgpassUserLinkApproved(false);
				handleUserNotifications(orgUser);
			}

			contactsEmail.setInitial(orgUser.getInitial());
			contactsEmail.setMobileNumber(orgUser.getMobileNumber());
			contactsEmail.setNationalIdNumber(orgUser.getNationalIdNumber());
			contactsEmail.setPassportNumber(orgUser.getPassportNumber());
			contactsEmail.seteSealSignatory(orgUser.iseSealSignatory());
			contactsEmail.setDigitalFormPrivilege(orgUser.isDigitalFormPrivilege());
			contactsEmail.seteSealPreparatory(orgUser.iseSealPrepatory());
			contactsEmail.setTemplate(orgUser.isTemplate());
			contactsEmail.setDelegate(orgUser.isDelegate());
			contactsEmail.setBulksign(orgUser.isBulksign());
			contactsEmail.setSubscriberUid(orgContactsEmailDB.getSubscriberUid());
			return contactsEmail;
		}


	}

	private String processSignaturePhoto(String signaturePhoto) {
		if (signaturePhoto != null && (signaturePhoto.startsWith(Constant.DATA_IMAGE_JPEG_BASE64)
				|| signaturePhoto.startsWith(Constant.DATA_IMAGE_PNG_BASE64))) {
			return signaturePhoto.split(",")[1];
		}
		return signaturePhoto;
	}

	private void saveOrganizationDirectors(RegisterOrganizationDTO registerOrganizationDTO,
										   OrganizationDetails organizationDetails) {
		List<OrganizationDirectors> directors = new ArrayList<>();
		for (String directorEmail : registerOrganizationDTO.getDirectorsEmailList()) {
			OrganizationDirectors organizationDirectors = new OrganizationDirectors();
			organizationDirectors.setOrganizationUid(organizationDetails.getOrganizationUid());
			organizationDirectors.setDirectorsEmailList(directorEmail);
			directors.add(organizationDirectors);
		}
		organizationDirectorsRepository.saveAll(directors);
	}

	private void saveDocumentCheckBoxes(RegisterOrganizationDTO registerOrganizationDTO,
										OrganizationDetails organizationDetails) {
		List<OrganizationDocumentsCheckBox> checkBoxs = new ArrayList<>();
		for (String checkBoxList : registerOrganizationDTO.getDocumentListCheckbox()) {
			OrganizationDocumentsCheckBox documentsCheckBox = new OrganizationDocumentsCheckBox();
			documentsCheckBox.setOrganizationUid(organizationDetails.getOrganizationUid());
			documentsCheckBox.setDocumentListCheckbox(checkBoxList);
			checkBoxs.add(documentsCheckBox);
		}
		organizationDocumentsCheckBoxRepository.saveAll(checkBoxs);
	}

	private void sendConfirmationEmail(RegisterOrganizationDTO registerOrganizationDTO) {
		try {
			sendEmail(registerOrganizationDTO.getSpocUgpassEmail(), registerOrganizationDTO.getOrganizationName());
		} catch (Exception e) {
			logger.error("{} - {} - {} : Error sending confirmation email", CLASS, Utility.getMethodName(),
					e.getMessage());
		}
	}

	@Override
	public ApiResponses getOrganizationBySerachType(String organizationName) {
		try {
			logger.info("{} - {} - {} : Request for getOrganizationBySerachType", CLASS, Utility.getMethodName(),
					organizationName);
			if (!StringUtils.hasText(organizationName)) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_SEARCH_TYPE_CANT_BE_NULL_OR_EMPTY);
			}
			List<String> organizationlist = organizationDetailsRepository.getOrganizationByName(organizationName);
			if (organizationlist.isEmpty()) {
				throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_ERROR_ORGANIZATION_LIST_NOT_FOUND);
			}
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ORGANIZATION_LIST, organizationlist);
		} catch (Exception e) {
			logger.error("{} - {} : Exception occurred during organization search for name '{}': {}", CLASS,
					Utility.getMethodName(), organizationName, e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}

	}

	@Override
	public ApiResponses getOrganizationDetailsById(String organizationUid) {
		try {
			logger.info("{} - {}: Request to get organization details for UID: {}", CLASS, Utility.getMethodName(),
					organizationUid);

			if (!StringUtils.hasText(organizationUid)) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORG_ID_CANT_BE_NULL_EMPTY);
			}
			List<OrganizationDetails> organization = organizationDetailsRepository.findOrganizationByUid(organizationUid);

			if (organization.isEmpty()) {
				logger.warn("{} - {}: Organization details not found for UID: {}", CLASS, Utility.getMethodName(),
						organizationUid);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGANIZATION_DETAILS_NOT_FOUND);
			}

			OrganizationDetails organisation = organization.get(0);
			RegisterOrganizationDTO registerOrganizationDTO = mapToRegisterOrganizationDTO(organisation);

			// Set Organization Contacts Emails
			List<OrgContactsEmail> orgContactsEmailList = orgContactsEmailRepository
					.findByOrganizationUid(organisation.getOrganizationUid());
			if (Objects.nonNull(orgContactsEmailList) && !orgContactsEmailList.isEmpty()) {
				List<OrgUser> userList = mapToOrgUserList(orgContactsEmailList);
				registerOrganizationDTO.setOrgUserList(userList);
			}

			// Set Email Domain
			OrganizationEmailDomain organizationEmailDomain = orgEmailDomainRepository
					.findByOrganizationUid(organisation.getOrganizationUid());
			registerOrganizationDTO
					.setEmailDomain(organizationEmailDomain != null ? organizationEmailDomain.getEmailDomain() : null);

			// Set Signature Templates
			List<OrganizationSignatureTemplates> signatureTemplatesList = organizationSignatureTemplatesRepository
					.findByOrganizationUid(organisation.getOrganizationUid());
			registerOrganizationDTO.setTemplateId(mapToTemplateIdList(signatureTemplatesList));

			// Set Directors Email List
			List<OrganizationDirectors> directorsList = organizationDirectorsRepository
					.findByOrganizationUid(organisation.getOrganizationUid());
			registerOrganizationDTO.setDirectorsEmailList(mapToDirectorsEmailList(directorsList));

			// Set Document Checkboxes
			List<OrganizationDocumentsCheckBox> documentsCheckBoxList = organizationDocumentsCheckBoxRepository
					.findByOrganizationUid(organisation.getOrganizationUid());
			registerOrganizationDTO.setDocumentListCheckbox(mapToDocumentCheckList(documentsCheckBoxList));

			// Set Wallet Certificate Status
			WalletSignCertificate walletSignCertificate = walletSignCertRepo.findByOrganizationId(Constant.ACTIVE,
					organizationUid);
			registerOrganizationDTO.setWalletCertificateStatus(
					Optional.ofNullable(walletSignCertificate).map(cert -> true).orElse(false));
			logger.info("{} - {}: Successfully retrieved organization details for UID: {}", CLASS,
					Utility.getMethodName(), organizationUid);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ORGANIZATION_DETAILS,
					registerOrganizationDTO);

		} catch (Exception e) {
			logger.error("{} - {} : Exception occurred during get organization details by organizationUid '{}': {}",
					CLASS, Utility.getMethodName(), organizationUid, e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	private RegisterOrganizationDTO mapToRegisterOrganizationDTO(OrganizationDetails organisation) {
		RegisterOrganizationDTO dto = new RegisterOrganizationDTO();
		dto.setOrganizationUid(organisation.getOrganizationUid());
		dto.setOrganizationName(organisation.getOrganizationName());
		dto.setOrganizationEmail(organisation.getOrganizationEmail());
		dto.setAuthorizedLetterForSignatories(organisation.getAuthorizedLetterForSignatories());
		dto.seteSealImage(organisation.geteSealImage());
		dto.setCorporateOfficeAddress(organisation.getCorporateOfficeAddress());
		dto.setTax(organisation.getTax());
		dto.setIncorporation(organisation.getIncorporation());
		dto.setTaxNo(organisation.getTaxNo());
		dto.setUniqueRegdNo(organisation.getUniqueRegdNo());
		dto.setOtherLegalDocument(organisation.getOtherLegalDocument());
		dto.setOtherESealDocument(organisation.getOtherESealDocument());
		dto.setSignedPdf(organisation.getSignedPdf());
		dto.setSegment(organisation.getSegment());
		dto.setCreatedBy(organisation.getCreatedBy());
		dto.setUpdatedBy(organisation.getUpdatedBy());
		dto.setStatus(organisation.getStatus());
		dto.setCreatedOn(organisation.getCreatedOn());
		dto.setUpdatedOn(organisation.getUpdatedOn());
		dto.setEnablePostPaidOption(organisation.isEnablePostPaidOption());
		dto.setSpocUgpassEmail(organisation.getSpocUgpassEmail());
		dto.setAgentUrl(organisation.getAgentUrl());
		dto.setManageByAdmin(organisation.isManageByAdmin());

		return dto;
	}

	private List<OrgUser> mapToOrgUserList(List<OrgContactsEmail> list) {
		return list.stream().map(userEmail -> {
			OrgUser orgUser = new OrgUser();
			orgUser.setOrgContactsEmailId(userEmail.getOrgContactsEmailId());
			orgUser.setEmployeeEmail(userEmail.getEmployeeEmail());
			orgUser.seteSealPrepatory(userEmail.iseSealPreparatory());
			orgUser.seteSealSignatory(userEmail.iseSealSignatory());
			orgUser.setSignatory(userEmail.isSignatory());
			orgUser.setDesignation(userEmail.getDesignation());
			orgUser.setSignaturePhoto(userEmail.getSignaturePhoto());
			orgUser.setTemplate(userEmail.isTemplate());
			orgUser.setBulksign(userEmail.isBulksign());
			orgUser.setUgpassEmail(userEmail.getUgpassEmail());
			orgUser.setPassportNumber(userEmail.getPassportNumber());
			orgUser.setNationalIdNumber(userEmail.getNationalIdNumber());
			orgUser.setMobileNumber(userEmail.getMobileNumber());
			orgUser.setUgpassUserLinkApproved(userEmail.isUgpassUserLinkApproved());
			orgUser.setSubscriberUid(userEmail.getSubscriberUid());
			orgUser.setStatus(userEmail.getStatus());
			orgUser.setDelegate(userEmail.isDelegate());
			orgUser.setDigitalFormPrivilege(userEmail.isDigitalFormPrivilege());
			orgUser.setInitial(userEmail.getInitial());
			return orgUser;
		}).toList();
	}

	private List<Integer> mapToTemplateIdList(List<OrganizationSignatureTemplates> list) {
		return list.stream().map(OrganizationSignatureTemplates::getTemplateId).toList();
	}

	private List<String> mapToDirectorsEmailList(List<OrganizationDirectors> list) {
		return list.stream().map(OrganizationDirectors::getDirectorsEmailList).toList();
	}

	private List<String> mapToDocumentCheckList(List<OrganizationDocumentsCheckBox> list) {
		return list.stream().map(OrganizationDocumentsCheckBox::getDocumentListCheckbox)
				.toList();
	}

	private void populateOrganizationDetails(RegisterOrganizationDTO dto, OrganizationDetails organisation) {
		// Set OrgUser list
		List<OrgContactsEmail> orgContactsEmailList = orgContactsEmailRepository
				.findByOrganizationUid(organisation.getOrganizationUid());
		if (Objects.nonNull(orgContactsEmailList) && !orgContactsEmailList.isEmpty()) {
			dto.setOrgUserList(mapToOrgUserList(orgContactsEmailList));
		}

		// Set Email Domain
		OrganizationEmailDomain organizationEmailDomain = orgEmailDomainRepository
				.findByOrganizationUid(organisation.getOrganizationUid());
		dto.setEmailDomain(organizationEmailDomain != null ? organizationEmailDomain.getEmailDomain() : null);

		// Set Templates, Directors, Documents
		dto.setTemplateId(mapToTemplateIdList(
				organizationSignatureTemplatesRepository.findByOrganizationUid(organisation.getOrganizationUid())));
		dto.setDirectorsEmailList(mapToDirectorsEmailList(
				organizationDirectorsRepository.findByOrganizationUid(organisation.getOrganizationUid())));
		dto.setDocumentListCheckbox(mapToDocumentCheckList(
				organizationDocumentsCheckBoxRepository.findByOrganizationUid(organisation.getOrganizationUid())));

		// Set Wallet Certificate Status
		WalletSignCertificate walletSignCertificate = walletSignCertRepo.findByOrganizationId(Constant.ACTIVE,
				organisation.getOrganizationUid());
		dto.setWalletCertificateStatus(Optional.ofNullable(walletSignCertificate).map(cert -> true).orElse(false));
	}


	@Override
	public ApiResponses getOrgDetailsByOrganizationName(String organizationName) {
		try {
			logger.info("{} - {}: Request to get organization details for name: {}", CLASS, Utility.getMethodName(),
					organizationName);
			if (!StringUtils.hasText(organizationName)) {
				return exceptionHandlerUtil.createErrorResponse(Constant.ORGANIZATION_ID_CANT_BE_NULL_OR_EMPTY);
			}
			List<OrganizationDetails> organizationList = organizationDetailsRepository.getOrgnizationDetails(organizationName);

			if (organizationList.isEmpty()) {
				logger.warn("{} - {}: Organization details not found for name: {}", CLASS, Utility.getMethodName(),
						organizationName);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGANIZATION_DETAILS_NOT_FOUND);
			}

			OrganizationDetails organisation = organizationList.get(0);
			RegisterOrganizationDTO registerOrganizationDTO = mapToRegisterOrganizationDTO(organisation);

			// Set OrgUser list, Email Domain, Templates, Directors, Documents, etc.
			populateOrganizationDetails(registerOrganizationDTO, organisation);

			logger.info("{} - {}: Successfully retrieved organization details for name: {}", CLASS,
					Utility.getMethodName(), organizationName);

			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ORGANIZATION_DETAILS,
					registerOrganizationDTO);
		} catch (Exception e) {
			logger.error("{} - {}: Unexpected error occurred while retrieving organization details for name: {}: {}",
					CLASS, Utility.getMethodName(), organizationName, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses isOrganizationAlreadyExixts(String organizationName) {
		try {
			logger.info("{} - {}: Checking if organization exists for name: {}", CLASS, Utility.getMethodName(),
					organizationName);
			if (!StringUtils.hasText(organizationName)) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORG_NAME_CANT_BE_NULL_OR_EMPTY);
			}
			int count = organizationDetailsRepository.isOrgnizationExist(organizationName);

			if (count == 0) {
				logger.info("{} - {}: Organization does not exist for name: {}", CLASS, Utility.getMethodName(),
						organizationName);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGANIZATION_DETAILS_NOT_FOUND);
			}

			logger.info("{} - {}: Organization exists for name: {}", CLASS, Utility.getMethodName(), organizationName);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ORGANIZATION_DETAILS, null);

		} catch (Exception e) {
			logger.error("{} - {}: Unexpected error occurred while checking organization existence for name: {}: {}",
					CLASS, Utility.getMethodName(), organizationName, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses updateOrganization(RegisterOrganizationDTO updateOrganizationDTO) {
		try {
			logger.info("{} - {}: Starting update for organization UID: {}", CLASS, Utility.getMethodName(),
					updateOrganizationDTO.getOrganizationUid());

			if (!StringUtils.hasText(updateOrganizationDTO.getOrganizationUid())) {
				return exceptionHandlerUtil.createErrorResponse(Constant.ORGANIZATION_ID_CANT_BE_NULL_OR_EMPTY);
			}
			
			OrganizationDetails organizationDetails = organizationDetailsRepository
					.findByOrganizationUid(updateOrganizationDTO.getOrganizationUid());
			if (organizationDetails == null) {
				logger.warn("{} - {}: Organization notfound for UID: {}", CLASS, Utility.getMethodName(),
						updateOrganizationDTO.getOrganizationUid());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGANIZATION_DETAILS_NOT_FOUND);
			}

			updateOrganizationDetails(organizationDetails, updateOrganizationDTO);

			// Updated related entities (templates, email domain, users, directors,
			// documents)
			updateSignatureTemplates(updateOrganizationDTO, organizationDetails);
			updateEmailDomain(updateOrganizationDTO, organizationDetails);
			updateOrgUsers(updateOrganizationDTO, organizationDetails);
			updateOrganizationDirectors(updateOrganizationDTO, organizationDetails);
			updateDocumentCheckBoxes(updateOrganizationDTO, organizationDetails);

			organizationDetailsRepository.save(organizationDetails);

			logger.info("{} - {}: Successfully updated organization UID: {}", CLASS, Utility.getMethodName(),
					updateOrganizationDTO.getOrganizationUid());
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ORGANIZATION_UPDATED,
					updateOrganizationDTO);
		}catch (OrgnizationServiceException e) {
			e.printStackTrace();
			return exceptionHandlerUtil.handleException(e);
		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred during organization update for UID: {}: {}", CLASS,
					Utility.getMethodName(), updateOrganizationDTO.getOrganizationUid(), e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	private void updateOrganizationDetails(OrganizationDetails organizationDetails,
										   RegisterOrganizationDTO updateOrganizationDTO) {
		organizationDetails.setOrganizationName(updateOrganizationDTO.getOrganizationName().trim());
		organizationDetails.setCorporateOfficeAddress(updateOrganizationDTO.getCorporateOfficeAddress());
		organizationDetails.setTaxNo(updateOrganizationDTO.getTaxNo());
		organizationDetails.setOrganizationEmail(updateOrganizationDTO.getOrganizationEmail());
		organizationDetails.setUniqueRegdNo(updateOrganizationDTO.getUniqueRegdNo());
		organizationDetails.setSegment(updateOrganizationDTO.getSegment());
		organizationDetails.setUpdatedBy(updateOrganizationDTO.getUpdatedBy());
		organizationDetails.setUpdatedOn(AppUtil.getDate());
		organizationDetails.setEnablePostPaidOption(updateOrganizationDTO.isEnablePostPaidOption());
		organizationDetails.setSpocUgpassEmail(updateOrganizationDTO.getSpocUgpassEmail());
		organizationDetails.setAgentUrl(updateOrganizationDTO.getAgentUrl());

		// Conditionally update optional fields
		if (updateOrganizationDTO.geteSealImage() != null) {
			organizationDetails.seteSealImage(updateOrganizationDTO.geteSealImage());
		}
		if (updateOrganizationDTO.getAuthorizedLetterForSignatories() != null) {
			organizationDetails
					.setAuthorizedLetterForSignatories(updateOrganizationDTO.getAuthorizedLetterForSignatories());
		}
		if (updateOrganizationDTO.getTax() != null) {
			organizationDetails.setTax(updateOrganizationDTO.getTax());
		}
		if (updateOrganizationDTO.getIncorporation() != null) {
			organizationDetails.setIncorporation(updateOrganizationDTO.getIncorporation());
		}
		if (updateOrganizationDTO.getOtherLegalDocument() != null) {
			organizationDetails.setOtherLegalDocument(updateOrganizationDTO.getOtherLegalDocument());
		}
		if (updateOrganizationDTO.getSignedPdf() != null) {
			organizationDetails.setSignedPdf(updateOrganizationDTO.getSignedPdf());
		}
	}

	private void updateSignatureTemplates(RegisterOrganizationDTO updateOrganizationDTO,
										  OrganizationDetails organizationDetails) throws NoSuchMessageException, OrgnizationServiceException {
		if (updateOrganizationDTO.getTemplateId() == null || updateOrganizationDTO.getTemplateId().isEmpty()) {
			throw new OrgnizationServiceException(
					messageSource.getMessage(Constant.API_RESPONSE_TEMPLATE_CANT_EMPTY, null, locale));
		}

		organizationSignatureTemplatesRepository
				.deleteSignatureTemplateByOrgId(organizationDetails.getOrganizationUid());

		List<OrganizationSignatureTemplates> templates = updateOrganizationDTO.getTemplateId().stream()
				.map(templateId -> {
					OrganizationSignatureTemplates signatureTemplate = new OrganizationSignatureTemplates();
					signatureTemplate.setOrganizationUid(organizationDetails.getOrganizationUid());
					signatureTemplate.setTemplateId(templateId);
					signatureTemplate.setType(templateId == 5 || templateId == 6 || templateId == 7 ? Constant.ESEAL : Constant.SIGN);
					return signatureTemplate;
				}).toList(); // <-- Fixed the syntax right here

		organizationSignatureTemplatesRepository.saveAll(templates);
	}
	private void updateEmailDomain(RegisterOrganizationDTO updateOrganizationDTO,
								   OrganizationDetails organizationDetails) throws NoSuchMessageException, OrgnizationServiceException {
		OrganizationEmailDomain emailDomain = orgEmailDomainRepository
				.findByOrganizationUid(organizationDetails.getOrganizationUid());
		if (emailDomain == null) {
			throw new OrgnizationServiceException(
					messageSource.getMessage(Constant.API_ERROR_ORGANIZATION_DETAILS_NOT_FOUND_IN_DOMAIN, null, locale));
		}

		emailDomain.setEmailDomain(updateOrganizationDTO.getEmailDomain());
		emailDomain.setStatus(updateOrganizationDTO.isDomainStatus());
		emailDomain.setUpdatedOn(AppUtil.getDate());
		orgEmailDomainRepository.save(emailDomain);
	}

	private void updateOrgUsers(RegisterOrganizationDTO updateOrganizationDTO, OrganizationDetails organizationDetails)
			throws OrgnizationServiceException {
		if (updateOrganizationDTO.getOrgUserList() == null || updateOrganizationDTO.getOrgUserList().isEmpty()) {
			return;
		}
		List<OrgContactsEmail> contactsEmails = new ArrayList<>();
		HashSet<String> emailSet = new HashSet<>();

		for (OrgUser orgUser : updateOrganizationDTO.getOrgUserList()) {
			if (!emailSet.add(orgUser.getEmployeeEmail())) {
				throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_RESPONSE_EMPLOYEE_MAIL_UNIQUE);
			}

			OrgContactsEmail contactsEmail = buildOrgContactsEmail(orgUser, organizationDetails.getOrganizationUid());
			contactsEmails.add(contactsEmail);
		}


		int deleted = orgContactsEmailRepository.deleteOrganizationEmailById(updateOrganizationDTO.getOrganizationUid());
		logger.info("is deleted:::::::::::{}" , deleted);

		orgContactsEmailRepository.saveAll(contactsEmails);
	}

	private void updateOrganizationDirectors(RegisterOrganizationDTO updateOrganizationDTO,
											 OrganizationDetails organizationDetails) {
		organizationDirectorsRepository.deleteOrganizationDirectorsEmailById(organizationDetails.getOrganizationUid());

		if (updateOrganizationDTO.getDirectorsEmailList() != null) {
			List<OrganizationDirectors> directors = updateOrganizationDTO.getDirectorsEmailList().stream()
					.map(email -> {
						OrganizationDirectors director = new OrganizationDirectors();
						director.setOrganizationUid(organizationDetails.getOrganizationUid());
						director.setDirectorsEmailList(email);
						return director;
					}).toList(); // <-- Swapped .collect(Collectors.toList()) for .toList()

			organizationDirectorsRepository.saveAll(directors);
		}
	}

	private void updateDocumentCheckBoxes(RegisterOrganizationDTO updateOrganizationDTO,
										  OrganizationDetails organizationDetails) {
		organizationDocumentsCheckBoxRepository
				.deleteOrganizationDocumentsCheckBoxById(organizationDetails.getOrganizationUid());

		if (updateOrganizationDTO.getDocumentListCheckbox() != null) {
			List<OrganizationDocumentsCheckBox> checkBoxes = updateOrganizationDTO.getDocumentListCheckbox().stream()
					.map(checkBox -> {
						OrganizationDocumentsCheckBox documentCheckBox = new OrganizationDocumentsCheckBox();
						documentCheckBox.setOrganizationUid(organizationDetails.getOrganizationUid());
						documentCheckBox.setDocumentListCheckbox(checkBox);
						return documentCheckBox;
					}).toList(); // <-- Swapped out the Collector here

			organizationDocumentsCheckBoxRepository.saveAll(checkBoxes);
		}
	}

	@Override
	public ApiResponses addOrganizationEmails(EmailListDto emailList) {
		try {
			logger.info("{} - {}: Received email list for adding organization emails: {}", CLASS,
					Utility.getMethodName(), emailList);
			if (emailList.getEmailList().isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_EMAIL_LIST_CANT_BE_NULL_OR_EMPTY);
			}
			List<String> inactiveMailList = new ArrayList<>();

			for (String email : emailList.getEmailList()) {
				String trimmedEmail = email.trim();
				String subscriberUid = subscriberRepository.findByemailId(trimmedEmail);

				if (subscriberUid == null) {
					logger.warn("{} - {}: Email not found: {}", CLASS, Utility.getMethodName(), trimmedEmail);
					inactiveMailList.add(trimmedEmail);
					continue;
				}

				SubscriberStatusModel subscriberStatus = subscriberStatusRepository.findBysubscriberUid(subscriberUid);
				if (subscriberStatus == null || Constant.INACTIVE.equalsIgnoreCase(subscriberStatus.getSubscriberStatus())) {
					logger.warn("{} - {}: Inactive or missing status for email: {}", CLASS, Utility.getMethodName(),
							trimmedEmail);
					inactiveMailList.add(trimmedEmail);
				}
			}

			if (inactiveMailList.isEmpty()) {
				logger.info("{} - {}: All emails are active.", CLASS, Utility.getMethodName());
				return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ACTIVE_EMAIL, null);
			}

			logger.warn("{} - {}: Found inactive or missing emails: {}", CLASS, Utility.getMethodName(),
					inactiveMailList);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_ERROR_INACTIVE_EMAIL, inactiveMailList);

		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred while processing organization emails: {}", CLASS,
					Utility.getMethodName(), e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getSubascriberEmailBySearchType(String searchType) {
		try {
			logger.info("{} - {}: Fetching subscriber emails for search type: {}", CLASS, Utility.getMethodName(),
					searchType);
			if (!StringUtils.hasText(searchType)) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_SEARCH_TYPE_CANT_BE_NULL_OR_EMPTY);
			}
			List<String> subscriberList = subscriberRepository.getSubscriberListByEmailId(searchType);
			if (subscriberList == null || subscriberList.isEmpty()) {
				logger.warn("{} - {}: No subscribers found for search type: {}", CLASS, Utility.getMethodName(),
						searchType);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_NO_SUBSCRIBERS_FOUND);
			}

			List<String> activeMailList = subscriberList.stream()
					.map(String::trim)
					.filter(mail -> {
						String subscriberUid = subscriberRepository.findByemailId(mail);
						if (subscriberUid == null) return false;

						SubscriberStatusModel subscriberStatus =
								subscriberStatusRepository.findBysubscriberUid(subscriberUid);

						return subscriberStatus != null &&
								Constant.ACTIVE.equalsIgnoreCase(subscriberStatus.getSubscriberStatus());
					})
					.toList(); // Replaces collect(Collectors.toList())

			if (activeMailList.isEmpty()) {
				logger.warn("{} - {}: No active subscriber emails found for search type: {}", CLASS,
						Utility.getMethodName(), searchType);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_NO_ACTIVE_EMAILS_FOUND);
			}

			logger.info("{} - {}: Successfully fetched active subscriber emails for search type: {}", CLASS,
					Utility.getMethodName(), searchType);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ACTIVE_EMAIL, activeMailList);

		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred while fetching subscriber emails for search type: {}: {}", CLASS,
					Utility.getMethodName(), searchType, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getOrganizationListAndUid(String email) {
		try {
			logger.info("{} - {}: Fetching organization list for email: {}", CLASS, Utility.getMethodName(), email);
			if (!StringUtils.hasText(email)) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_EMAIL_CANT_BE_NULL_OR_EMPTY);
			}
			// Fetch subscriber details
			Subscriber subscriberEmail = subscriberRepository.getSubscriber(email);
			if (subscriberEmail == null) {
				logger.warn("{} - {}: Subscriber email not found: {}", CLASS, Utility.getMethodName(), email);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_NO_SUBSCRIBERS_FOUND);
			}

			// Fetch contacts associated with the email
			List<OrgContactsEmail> contactsEmails = orgContactsEmailRepository.getPreprtyStatusByEmailId(email);
			if (contactsEmails == null || contactsEmails.isEmpty()) {
				logger.warn("{} - {}: No organization contacts found for subscriber email: {}", CLASS,
						Utility.getMethodName(), email);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGANIZATION_NOT_FOUND_FOR_THIS_SUBSCRIBER);
			}

			// Filter and map organizations where the user is an eSeal signatory
			List<OrganizationListResponseDTO> dtoList = contactsEmails.stream()
					.filter(OrgContactsEmail::iseSealSignatory).map(contact -> {
						OrganizationDetails details = organizationDetailsRepository
								.findByOrganizationUid(contact.getOrganizationUid());
						OrganizationListResponseDTO dto = new OrganizationListResponseDTO();
						dto.setOrganizationName(details.getOrganizationName());
						dto.setOrganizationUid(details.getOrganizationUid());
						return dto;
					}).toList(); // <-- Swapped out the Collector here
			if (dtoList.isEmpty()) {
				logger.warn("{} - {}: Subscriber does not belong to any organization as an eSeal signatory: {}", CLASS,
						Utility.getMethodName(), email);
				return exceptionHandlerUtil
						.createErrorResponse(Constant.API_ERROR_SUBSCRIBER_NOT_BELONG_FROM_ANY_ORGANIZATION_AS_ESEAL_SIGNATORY);
			}

			logger.info("{} - {}: Successfully fetched organization list for email: {}", CLASS, Utility.getMethodName(),
					email);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ESEAL_SIGNTORY_ROLE_ORG_LIST, dtoList);

		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred while fetching organization list for email: {}: {}", CLASS,
					Utility.getMethodName(), email, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@SuppressWarnings("unused")
	@Override
	public ApiResponses verifySignedDocumnet(SignatureVerificationContext1 signatureVerificationContext1) {
		try {
			logger.info("{} - {}: Verifying signed document for organization UID: {}", CLASS, Utility.getMethodName(),
					signatureVerificationContext1.getOrganizationUid());
			String result= ValidationUtil.validate(signatureVerificationContext1);
			if(result!=null){
				throw new OrgnizationServiceException(result);
			}
			if (signatureVerificationContext1 == null) {
				logger.warn("{} - {}: Request cannot be null", CLASS, Utility.getMethodName());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_RQUEST_CANT_BE_NULL);
			}

			OrganizationDetails organizationDetails = organizationDetailsRepository
					.findByOrganizationUid(signatureVerificationContext1.getOrganizationUid());
			if (organizationDetails == null) {
				logger.warn("{} - {}: Organization not found for UID: {}", CLASS, Utility.getMethodName(),
						signatureVerificationContext1.getOrganizationUid());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGANIZATION_NOT_FOUND);
			}

			String spocEmail = signatureVerificationContext1.getSignatories().get(0);
			Subscriber subscriber = subscriberRepository.getSubscriber(spocEmail);
			if (subscriber == null) {
				logger.warn("{} - {}: SPOC email not registered: {}", CLASS, Utility.getMethodName(), spocEmail);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_USER_NOT_REGISTERED);
			}

			// Perform external API call for signature verification
			ApiResponses apiResponse = callExternalSignatureVerificationApi(signatureVerificationContext1);// 1. ALWAYS do your null check first!
			if (apiResponse == null) {
				logger.warn("{} - {}: apiResponse is null", CLASS, Utility.getMethodName());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_DOC_WAS_NOT_SIGNED);
			}
			if (!apiResponse.isSuccess()) {
				return AppUtil.createApiResponse(false, apiResponse.getMessage(), null);
			}
			if (apiResponse.getResult() == null) {
				logger.warn("{} - {}: Document not signed", CLASS, Utility.getMethodName());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_DOC_WAS_NOT_SIGNED);
			}

			// Extract signature details
			String serialNumber = extractCertificateSerialNumber(apiResponse);
			if (serialNumber == null) {
				logger.warn("{} - {}: Document contains more than one signature", CLASS, Utility.getMethodName());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_DOC_CONTAINS_MORE_THAN_ONE_SIGN);
			}

			// Validate SPOC and certificate serial number
			String subscriberUId = subscriberRepository.findByemailId(spocEmail);
			String certSuid = subscriberCertificatesRepoIface.findSuidByCertSerialNumber(serialNumber);
			if (subscriberUId == null || !subscriberUId.equals(certSuid)) {
				logger.warn("{} - {}: Document not signed by SPOC: {}", CLASS, Utility.getMethodName(), spocEmail);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_DOC_WAS_NOT_SIGNED_BY_SPOC);
			}

			// Update organization with signed PDF
			organizationDetails.setSignedPdf(signatureVerificationContext1.getDocData());
			organizationDetailsRepository.save(organizationDetails);

			logger.info("{} - {}: Successfully verified and updated signed document for organization UID: {}", CLASS,
					Utility.getMethodName(), signatureVerificationContext1.getOrganizationUid());
			return apiResponse;

		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			logger.error("{} - {}: Exception occurred HttpStatusCodeException during document verification: {}", CLASS, Utility.getMethodName(),
					e.getMessage(), e);
			return exceptionHandlerUtil.handleHttpException(e);
		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred during document verification: {}", CLASS, Utility.getMethodName(),
					e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	public ApiResponses callExternalSignatureVerificationApi(SignatureVerificationContext1 context) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<SignatureVerificationContext1> requestEntity = new HttpEntity<>(context, headers);

			logger.info("{} - {}: Initiating API call to external signature verification service.", CLASS,
					Utility.getMethodName());
			ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity,
					String.class);
			logger.info("{} - {}: External API response received successfully.", CLASS, Utility.getMethodName());
			// Parse and return the response as an ApiResponse object
			return objectMapper.readValue(response.getBody(), ApiResponses.class);
		} catch (Exception e) {
			// Log generic exceptions
			logger.error("{} - {}: Unexpected error occurred while calling external API: {}", CLASS,
					Utility.getMethodName(), e.getMessage(), e);
			return exceptionHandlerUtil.handleHttpException(e);
		}
	}

	private String extractCertificateSerialNumber(ApiResponses apiResponse) {
		try {
			if (apiResponse == null || apiResponse.getResult() == null) {
				return null;
			}

			Object resultObj = apiResponse.getResult();
			if (!(resultObj instanceof Map)) {
				logger.error("{} - {}: Unexpected result type", CLASS, Utility.getMethodName());
				return null;
			}

			@SuppressWarnings("unchecked")
			Map<String, List<Map<String, String>>> resultMap =
					(Map<String, List<Map<String, String>>>) resultObj;

			if (resultMap.isEmpty()) {
				return null;
			}

			// Extract ONLY the first entry (no loop)
			Map.Entry<String, List<Map<String, String>>> firstEntry =
					resultMap.entrySet().iterator().next();

			List<Map<String, String>> signatures = firstEntry.getValue();

			if (signatures == null || signatures.size() != 1) {
				return null;
			}

			SignatureDeatils signatureDetails =
					objectMapper.convertValue(signatures.get(0), SignatureDeatils.class);

			return signatureDetails.getCertificateSerialNumber();

		} catch (Exception e) {
			logger.error("{} - {}: Failed to extract certificate serial number: {}",
					CLASS, Utility.getMethodName(), e.getMessage(), e);
			return null;
		}
	}

	@Override
	public ApiResponses getOrganizationPrepetryStatus(String suid) {
		try {
			logger.info("{} - {}: Fetching property status for subscriber UID: {}", CLASS, Utility.getMethodName(),
					suid);

			if (suid == null || suid.isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_SUBSCRIBERUID_CANT_NULL);
			}

			List<OrgContactsEmail> contactsEmails = orgContactsEmailRepository.getPreprtyStatusBySuid(suid);

			List<PrepteryStatusResponseDTO> prepteryStatusResponseDTOs = contactsEmails.stream()
					.map(orgContactsEmail -> {
						OrganizationDetails details = organizationDetailsRepository
								.findByOrganizationUid(orgContactsEmail.getOrganizationUid());
						PrepteryStatusResponseDTO dto = new PrepteryStatusResponseDTO();
						dto.setOrganizationUid(details.getOrganizationUid());
						dto.setOrganizationName(details.getOrganizationName());
						dto.setSubscriberEmailList(orgContactsEmail.getEmployeeEmail());
						dto.seteSealPrepatory(orgContactsEmail.iseSealPreparatory());
						dto.seteSealSignatory(orgContactsEmail.iseSealSignatory());
						dto.setSignatory(orgContactsEmail.isSignatory());
						dto.setTemplate(orgContactsEmail.isTemplate());
						dto.setBulksign(orgContactsEmail.isBulksign());
						dto.setDelegate(orgContactsEmail.isDelegate());
						dto.setDigitalFormPrivilege(orgContactsEmail.isDigitalFormPrivilege());
						return dto;
					}).toList(); // <-- Replaced Collectors.toList() with .toList()

			logger.info("{} - {}: Successfully retrieved property status for {} organizations", CLASS,
					Utility.getMethodName(), prepteryStatusResponseDTOs.size());
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_PREPETRY_STATUS,
					prepteryStatusResponseDTOs);

		} catch (Exception e) {
			logger.error("{} - {}: Unexpected error occurred while retrieving property status for UID: {}: {}", CLASS,
					Utility.getMethodName(), suid, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getOrganizationPrepetryStatusByOrgId(String orgId) {
		try {
			logger.info("{} - {}: Fetching property status for organization UID: {}", CLASS, Utility.getMethodName(),
					orgId);

			if (orgId == null || orgId.isEmpty()) {
				logger.warn("{} - {}: Organization ID cannot be null or empty", CLASS, Utility.getMethodName());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORG_ID_CANT_BE_NULL_EMPTY);
			}

			OrganizationDetails details = organizationDetailsRepository.findByOrganizationUid(orgId);
			if (details == null) {
				logger.warn("{} - {}: Organization not found for UID: {}", CLASS, Utility.getMethodName(), orgId);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGANIZATION_DETAILS_NOT_FOUND);
			}

			List<String> bulkSignerList = orgContactsEmailRepository.getPreprtyStatusByOrgId(orgId);
			List<String> bulkSignerEsealList = orgContactsEmailRepository.getBulkEsealListByOrgId(orgId);

			OrgPrepetryStatusDto orgPrepetryStatusDto = new OrgPrepetryStatusDto();
			orgPrepetryStatusDto.setBulkSignerList(bulkSignerList);
			orgPrepetryStatusDto.setBulkSignerEsealList(bulkSignerEsealList);

			if (bulkSignerList != null && !bulkSignerList.isEmpty()) {
				logger.info("{} - {}: Bulk signatory list found for organization UID: {}", CLASS,
						Utility.getMethodName(), orgId);
				return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_BULKPREPETORY_LIST,
						orgPrepetryStatusDto);
			}

			logger.warn("{} - {}: No bulk signatory list found for organization UID: {}", CLASS,
					Utility.getMethodName(), orgId);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_ERROR_BULKPREPETORY_NOT_FOUND,
					orgPrepetryStatusDto);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} - {}: Exception : {}", CLASS, Utility.getMethodName(), e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getOrgListAndUid() {
		try {
			logger.info("{} - {}: Fetching organization list", CLASS, Utility.getMethodName());

			List<String> details = organizationDetailsRepository.getOrganizationList();
			if (details == null || details.isEmpty()) {
				logger.warn("{} - {}: No organizations found", CLASS, Utility.getMethodName());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGANIZATION_LIST_NOT_FOUND);
			}

			logger.info("{} - {}: Organization list retrieved successfully", CLASS, Utility.getMethodName());
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ORGANIZATION_LIST, details);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} - {}: Exception occurred: {}", CLASS, Utility.getMethodName(), e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getOrganizationListForSearch() {
		try {
			logger.info("{} - {}: Fetching organization list", CLASS, Utility.getMethodName());

			List<String> details = organizationDetailsRepository.getOrganizationListForSerach();
			if (details == null || details.isEmpty()) {
				logger.warn("{} - {}: No organizations found", CLASS, Utility.getMethodName());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGANIZATION_LIST_NOT_FOUND);
			}

			logger.info("{} - {}: Organization list retrieved successfully", CLASS, Utility.getMethodName());
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ORGANIZATION_LIST, details);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} - {}: Exception occurred: {}", CLASS, Utility.getMethodName(), e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getSigntoryList(String organizationUid) {
		try {
			logger.info("{} - {}: Fetching signatory list for organizationUid: {}", CLASS, Utility.getMethodName(),
					organizationUid);

			if (organizationUid == null || organizationUid.isEmpty()) {
				logger.warn("{} - {}: Organization UID is null or empty", CLASS, Utility.getMethodName());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORG_ID_CANT_BE_NULL_EMPTY);
			}

			List<String> signatoryList = organizationDetailsRepository.getOrganizationSignatoryList(organizationUid);

			if (signatoryList == null || signatoryList.isEmpty()) {
				logger.warn("{} - {}: No signatories found for organizationUid: {}", CLASS, Utility.getMethodName(),
						organizationUid);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_SIGNATORY_LIST_NOT_FOUND);
			}

			logger.info("{} - {}: Signatory list retrieved successfully for organizationUid: {}", CLASS,
					Utility.getMethodName(), organizationUid);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_SIGNATORY_LIST, signatoryList);

		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred while fetching signatory list: {}", CLASS,
					Utility.getMethodName(), e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getAllTemplates() {
		try {
			logger.info("{} - {}: Fetching all templates", CLASS, Utility.getMethodName());

			List<SignatureTemplates> signatureTemplates = signatureTemplatesRepository.getAllTemplates();
			if (signatureTemplates == null || signatureTemplates.isEmpty()) {
				logger.warn("{} - {}: No templates for that id", CLASS, Utility.getMethodName());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_RESPONSE_TEMPLATE_CANT_EMPTY);
			}

			logger.info("{} - {}: Templates retrieved", CLASS, Utility.getMethodName());
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_SIGNATURE_TEMPLATES_FETCHED,
					signatureTemplates);

		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred fetching templates: {}", CLASS, Utility.getMethodName(),
					e.getMessage(), e);
			e.printStackTrace();
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getAllTemplatesWithoutImages() {
		try {
			logger.info("{} - {}: Fetching all templates", CLASS, Utility.getMethodName());

			List<SignatureTemplates> signatureTemplates = signatureTemplatesRepository.getAllTemplates();
			if (signatureTemplates == null || signatureTemplates.isEmpty()) {
				logger.warn("{} - {}: No templates found", CLASS, Utility.getMethodName());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_RESPONSE_TEMPLATE_CANT_EMPTY);
			}
			signatureTemplates.forEach(s->s.setSamplePreview(""));
			logger.info("{} - {}: Templates retrieved successfully", CLASS, Utility.getMethodName());
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_SIGNATURE_TEMPLATES_FETCHED,
					signatureTemplates);

		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred while fetching templates: {}", CLASS, Utility.getMethodName(),
					e.getMessage(), e);
			e.printStackTrace();
			return exceptionHandlerUtil.handleException(e);
		}
	}
	@Override
	public ApiResponses getTemplateImage(int id) {
		try {
			logger.info("{} - {}: Fetching getTemplateImage", CLASS, Utility.getMethodName());
			SignatureTemplates signatureTemplates = signatureTemplatesRepository.getTemplateImageById(id);
			if (signatureTemplates == null) {
				logger.warn("{} - {}: No templates found", CLASS, Utility.getMethodName());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_RESPONSE_TEMPLATE_CANT_EMPTY);
			}
			logger.info("{} - {}: Templates retrieved successfully", CLASS, Utility.getMethodName());
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_SIGNATURE_TEMPLATES_FETCHED,
					signatureTemplates.getSamplePreview());
		}
		catch (Exception e) {
			logger.error("{} - {}: Exception occurred while fetching templates: {}", CLASS, Utility.getMethodName(),
					e.getMessage(), e);
			e.printStackTrace();
			return exceptionHandlerUtil.handleException(e);
		}

	}


	@Override
	public ApiResponses getAgentUrlByOrg(String orgId) {
		try {
			logger.info("{} - {}: Fetching agent URL for orgId: {}", CLASS, Utility.getMethodName(), orgId);

			if (orgId == null || orgId.isEmpty()) {
				logger.warn("{} - {}: Organization ID is null or empty", CLASS, Utility.getMethodName());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORG_ID_CANT_BE_NULL_EMPTY);
			}

			OrganizationDetails details = organizationDetailsRepository.findByOrganizationUid(orgId);
			String agentUrl = details != null ? details.getAgentUrl() : null;

			if (agentUrl == null || agentUrl.isEmpty()) {
				logger.warn("{} - {}: Agent URL not found for orgId: {}", CLASS, Utility.getMethodName(), orgId);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_AGENT_URL_NOT_FOUND);
			}

			logger.info("{} - {}: Agent URL retrieved successfully for orgId: {}", CLASS, Utility.getMethodName(),
					orgId);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_AGENT_URL, agentUrl);

		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred while fetching agent URL for orgId: {}: {}", CLASS,
					Utility.getMethodName(), orgId, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public SignatureTemplateDto getTemplatesByTempId(GetTemplateDto getTemplateDto) throws OrgnizationServiceException {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Fetching template details for {}", CLASS, methodName, getTemplateDto);
		SignatureTemplateDto dto = new SignatureTemplateDto();
		try {
			// Fetching subscriber, organization details, and user signature details
			Subscriber subscriber = subscriberRepository.findBysubscriberUid(getTemplateDto.getSuid());
			String title = subscriber.getTitle();
			OrganizationDetails organizationDetails = organizationDetailsRepository
					.findByOrganizationUid(getTemplateDto.getOrgId());
			OrgContactsEmail orgUser = orgContactsEmailRepository
					.getOrganizationSigntoryDetailsBySuidAndOuid(getTemplateDto.getSuid(), getTemplateDto.getOrgId());

			// Setting values into the DTO
			dto.setDesignation(orgUser.getDesignation());
			dto.setHandWrittenSignature(orgUser.getSignaturePhoto());
			dto.setCompanyName(organizationDetails.getOrganizationName());
			if(title !=null && !title.isEmpty()) {
				dto.setFullName(title + " " + subscriber.getFullName());
			}
			dto.setFullName(subscriber.getFullName());
			dto.setEsealImage(organizationDetails.geteSealImage());

			// Fetching signature templates
			List<OrganizationSignatureTemplates> orgSignatureTemplates = organizationSignatureTemplatesRepository
					.getOrgSignatureTemplates(getTemplateDto.getOrgId());

			if (orgSignatureTemplates.size() > 1) {
				dto.setSignatureTemplateId(orgSignatureTemplates.get(0).getTemplateId());
				dto.setEsealSignatureTemplateId(orgSignatureTemplates.get(1).getTemplateId());
				logger.info("{} - {}: Successfully fetched template details for orgId: {}", CLASS, methodName,
						getTemplateDto.getOrgId());
			} else {
				logger.warn("{} - {}: Insufficient signature templates found for orgId: {}", CLASS, methodName,
						getTemplateDto.getOrgId());
				throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_ERROR_INVALID_TEMPLATE_ID);
			}

			return dto;

		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred for orgId: {}: {}", CLASS, methodName, getTemplateDto.getOrgId(),
					e.getMessage(), e);
			throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_ERROR_PLEASE_TRY_AFTER_SOMETIME);
		}
	}

	@Override
	public ApiResponses getUserTemplateDetails(GetTemplateDto dto) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Fetching user template details for {}", CLASS, methodName, dto);

		if (Objects.isNull(dto)) {
			return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_GET_TEMPLATES_DTO_REQURIED);
		}

		try {
			OrgContactsEmail orgUser = fetchOrgUser(dto, methodName);
			if (orgUser == null) return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_EMAIL_NOT_FOUND_IN_UAEID);

			if (isEmpty(orgUser.getSubscriberUid())) {
				return subscriberNotLinkedResponse();
			}

			SubscriberCompleteDetail detail = fetchSubscriberDetail(orgUser, dto, methodName);
			if (detail == null) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_SUBSCRIBER_SELFIE_NOT_FOUND);
			}

			SignatureTemplateDto signatureDto = prepareSignatureDto(detail);

			return processTemplateId(dto.getTemplateId(), orgUser, signatureDto);

		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred while fetching user template details for orgId: {}: {}",
					CLASS, methodName, dto.getOrgId(), e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}
	private ApiResponses processTemplateId(int templateId, OrgContactsEmail user, SignatureTemplateDto dto) {
		switch (templateId) {
			case 0:
				return success(Constant.API_RESPONSE_TEMPLATE_ID, dto);

			case 1:
				dto.setDesignation(user.getDesignation());
				return success(Constant.API_RESPONSE_THIS_EMAIL_HAS_STANDARD_SIGN, dto);

			case 2:
				return handleDesignationCase(user, dto);

			case 3:
				return handleHandwrittenCase(user, dto);

			case 4:
				return handleBothCase(user, dto);

			default:
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_INVALID_TEMPLATE_ID);
		}
	}

	private ApiResponses handleDesignationCase(OrgContactsEmail user, SignatureTemplateDto dto) {
		if (!isEmpty(user.getDesignation())) {
			dto.setDesignation(user.getDesignation());
			return success(Constant.API_RESPONSE_THIS_EMAIL_HAS_DESIGNATION, dto);
		}
		return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_THIS_EMAIL_NO_DESIGNATION);
	}

	private ApiResponses handleHandwrittenCase(OrgContactsEmail user, SignatureTemplateDto dto) {
		if (!isEmpty(user.getSignaturePhoto())) {
			dto.setDesignation(user.getDesignation());
			return success(Constant.API_RESPONSE_EMAIL_HAVE_HANDWRITTEN_SIGNATURE, dto);
		}
		return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_EMAIL_HAS_NO_HANDWRITTEN_SIGNATURE);
	}

	private ApiResponses handleBothCase(OrgContactsEmail user, SignatureTemplateDto dto) {
		if (!isEmpty(user.getSignaturePhoto()) && !isEmpty(user.getDesignation())) {
			dto.setDesignation(user.getDesignation());
			return success(Constant.API_RESPONSE_EMAIL_HAVE_HANDWRITTEN_SIGNATURE_AND_DESIGNATION, dto);
		}
		return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_EMAIL_HAS_NO_HANDWRITTEN_ANDDESIGNATION);
	}

	private ApiResponses success(String msgKey, Object body) {
		return exceptionHandlerUtil.createSuccessResponse(msgKey, body);
	}

	private OrgContactsEmail fetchOrgUser(GetTemplateDto dto, String methodName) {
		OrgContactsEmail user = orgContactsEmailRepository
				.getOrganizationSigntoryDetails(dto.getEmail(), dto.getOrgId());

		if (user == null) {
			logger.warn("{} - {}: Email not found in UGPass for orgId: {}", CLASS, methodName, dto.getOrgId());
		}
		return user;
	}

	private ApiResponses subscriberNotLinkedResponse() {
		return AppUtil.createApiResponse(
				false,
				messageSource.getMessage(Constant.API_ERROR_EMAIL_NOT_LINKED, null, locale),
				new SignatureTemplateDto()
		);
	}

	private SubscriberCompleteDetail fetchSubscriberDetail(OrgContactsEmail user, GetTemplateDto dto, String methodName) {
		SubscriberCompleteDetail detail =
				subscriberCompleteDetailRepoIface.getSubscriberSelfieThumbnailBySuid(user.getSubscriberUid());

		if (detail == null || isEmpty(detail.getSelfieThumbnailUri())) {
			logger.warn("{} - {}: Subscriber selfie thumbnail not found for orgId: {}", CLASS, methodName, dto.getOrgId());
			return null;
		}
		return detail;
	}

	private SignatureTemplateDto prepareSignatureDto(SubscriberCompleteDetail detail) {
		SignatureTemplateDto dto = new SignatureTemplateDto();
		dto.setSelfieThumbnail(detail.getSelfieThumbnailUri());
		dto.setFullName(detail.getFullName());
		return dto;
	}

	private boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}

	@Override
	public ApiResponses getOrganizationListBySuid(String suid) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Fetching organization list for suid: {}", CLASS, methodName, suid);
		try {
			if (!StringUtils.hasText(suid)) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_SUBSCRIBERUID_CANT_NULL);
			}
			SubscriberView subscriberView = subscriberViewRepository.getSubscriberDetailsBySuid(suid);
			if (subscriberView == null) {
				logger.info("{} - {}: Subscriber not found for suid: {}", CLASS, methodName, suid);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_SUBSCRIBER_NOT_FOUND);
			}

			List<OrgContactsEmail> orgContactsEmail = orgContactsEmailRepository.getSubscriberDetails(
					subscriberView.getEmailId(), subscriberView.getMobileNumber(), subscriberView.getIdDocNumber(),
					subscriberView.getIdDocNumber(), suid);

			if (orgContactsEmail == null || orgContactsEmail.isEmpty()) {
				logger.warn("{} - {}: No organization contacts found for suid: {}", CLASS, methodName, suid);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_DATA_NOT_FOUND);
			}

			List<OrganizationDetailsResDTO> detailsResDTOs = new ArrayList<>();
			for (OrgContactsEmail orgContact : orgContactsEmail) {
				List<OrganizationDetails> details = organizationDetailsRepository
						.findOrganizationByUid(orgContact.getOrganizationUid());

				if (details.isEmpty()) {
					logger.warn("{} - {}: No organization details found for orgUid: {}", CLASS, methodName,
							orgContact.getOrganizationUid());
					continue;
				}

				OrganizationDetailsResDTO detailsResDTO = new OrganizationDetailsResDTO();
				OrganizationDetails orgDetails = details.get(0); // Assuming only one organization per contact
				detailsResDTO.setOrgContactsEmailId(orgContact.getOrgContactsEmailId());
				detailsResDTO.setEmployeeEmail(orgContact.getEmployeeEmail());
				detailsResDTO.setOrganizationName(orgDetails.getOrganizationName());
				detailsResDTO.setOrganizationUid(orgDetails.getOrganizationUid());
				detailsResDTO.setUgpassLinkApproved(orgContact.isUgpassUserLinkApproved());
				detailsResDTOs.add(detailsResDTO);
			}

			logger.info("{} - {}: Returning organization list for suid: {}: {}", CLASS, methodName, suid,
					detailsResDTOs);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ORGANIZATION_LIST, detailsResDTOs);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} - {}: Exception occurred while fetching organization list for suid: {}: {}", CLASS,
					methodName, suid, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses sendOtp(EmailReqDto otpDto) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Sending OTP to email: {}", CLASS, methodName, otpDto.getEmailId());

		try {
			if (otpDto.getEmailId().isEmpty()) {
				logger.warn("{} - {}: Email ID cannot be empty", CLASS, methodName);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_MAIL_ID_CANT_BE_EMPTY);
			}

			String emailOTP = generateOtp(5);
			EmailReqDto dto = new EmailReqDto();
			dto.setEmailOtp(emailOTP);
			dto.setEmailId(otpDto.getEmailId());
			dto.setTtl(timeToLive);

			ApiResponses res = sendEmailToSubscriber(dto);

			OTPResponseDTO otpResponse = new OTPResponseDTO();
			otpResponse.setMobileOTP(null);
			otpResponse.setEmailOTP(null);
			otpResponse.setTtl(timeToLive);
			otpResponse.setEmailEncrptyOTP(encryptedString(emailOTP));

			if (res.isSuccess()) {
				logger.info("{} - {}: OTP sent successfully to email: {}", CLASS, methodName, otpDto.getEmailId());
				return AppUtil.createApiResponse(true, Constant.OK, otpResponse);
			} else {
				logger.error("{} - {}: Failed to send OTP to email: {}: {}", CLASS, methodName, otpDto.getEmailId(),
						res);
				return exceptionHandlerUtil
						.createErrorResponse(Constant.API_ERROR_SOMETHING_WENT_WRONG_PLEASE_TRY_AFTER_SOMETIME);
			}
		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred while sending OTP to email: {}: {}", CLASS, methodName,
					otpDto.getEmailId(), e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses linkEmail(OrgUser orgUser) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Linking email for orgUser: {}", CLASS, methodName, orgUser);

		try {
			// Check if Subscriber UID is null or empty
			if (orgUser.getSubscriberUid() == null || orgUser.getSubscriberUid().isEmpty()) {
				logger.warn("{} - {}: Subscriber UID is null or empty", CLASS, methodName);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_SUBSCRIBERUID_CANT_NULL);
			}

			// Retrieve SubscriberView by Subscriber UID
			SubscriberView subscriberView = subscriberViewRepository.getSubscriberDetailsBySuid(orgUser.getSubscriberUid());
			if (subscriberView == null) {
				logger.warn("{} - {}: Subscriber not found for UID: {}", CLASS, methodName, orgUser.getSubscriberUid());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_SUBSCRIBER_NOT_FOUND);
			}

			// Retrieve OrgContactsEmail by ID
			OrgContactsEmail contactsEmail = orgContactsEmailRepository.findById(orgUser.getOrgContactsEmailId())
					.orElse(null);
			if (contactsEmail == null) {
				logger.warn("{} - {}: Organization contact email not found for ID: {}", CLASS, methodName,
						orgUser.getOrgContactsEmailId());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGANIZATION_DETAILS_NOT_FOUND);
			}

			// Check if the email is already linked
			if (contactsEmail.isUgpassUserLinkApproved()) {
				logger.info("{} - {}: Email already linked for UID: {}", CLASS, methodName, orgUser.getSubscriberUid());
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ALREADY_LINKED);
			}

			// Update the OrgContactsEmail and save
			contactsEmail.setUgpassUserLinkApproved(true);
			contactsEmail.setSubscriberUid(orgUser.getSubscriberUid());
			orgContactsEmailRepository.save(contactsEmail);

			// Send notification to the user
			sendNotification(subscriberView.getDisplayName(), subscriberView.getFcmToken(), false);

			logger.info("{} - {}: Email linked successfully for UID: {}", CLASS, methodName,
					orgUser.getSubscriberUid());
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_USERLINK_APPROVED, null);

		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred while linking email for UID: {}: {}", CLASS, methodName,
					orgUser.getSubscriberUid(), e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
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
			e.printStackTrace();
			return null;
		}
	}

	public ApiResponses sendEmailToSubscriber(EmailReqDto emailReqDto) {
		String methodName = Utility.getMethodName();
		try {
			String url = emailBaseUrl;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Object> requestEntity = new HttpEntity<>(emailReqDto, headers);

			logger.info("{} - {}: Sending email request: {}", CLASS, methodName, requestEntity);

			ResponseEntity<ApiResponses> res = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					ApiResponses.class);

			logger.info("{} - {}: Received response: {}", CLASS, methodName, res);
			var responseBody = res.getBody();
			switch (res.getStatusCode().value()) {
				case 200:
					return AppUtil.createApiResponse(
							false,
							responseBody != null ? responseBody.getMessage() : "Unknown error occurred",
							null
					);
				case 400:
					logger.warn("{} - {}: Bad request error", CLASS, methodName);
					return AppUtil.createApiResponse(false, "Bad Request", null);
				case 500:
					logger.error("{} - {}: Internal server error", CLASS, methodName);
					return AppUtil.createApiResponse(false, "Internal server error", null);
				default:

					return AppUtil.createApiResponse(
							false,
							responseBody != null ? responseBody.getMessage() : "Unknown error occurred",
							null
					);
			}
		} catch (HttpStatusCodeException e) {
			return exceptionHandlerUtil.handleHttpException(e);
		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred while sending email: {}", CLASS, methodName, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	private String encryptedString(String s) {
		try {
			Result result = DAESService.encryptData(s);
			return new String(result.getResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@Override
	public ApiResponses deactive(DeactivateDto deactivateDto) {
		String methodName = Utility.getMethodName();
		try {
			if (isNullOrEmpty(deactivateDto.getOrganizationUID())) {
				logger.info("{} - {}: OrganizationUID can't be null or empty", CLASS, methodName);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORG_ID_CANT_BE_NULL_EMPTY);
			}
			if (isNullOrEmpty(deactivateDto.getEmployeeEmail())) {
				logger.info("{} - {}: Employee email can't be null or empty", CLASS, methodName);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_EMAIL_CANT_BE_EMPTY);
			}

			List<OrgContactsEmail> contactsEmail = orgContactsEmailRepository.findByOrganizationUidAndEmail(
					deactivateDto.getOrganizationUID(), deactivateDto.getEmployeeEmail());

			if (contactsEmail == null || contactsEmail.isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_NOT_FOUND);
			}

			for (OrgContactsEmail orgContactsEmail : contactsEmail) {
				if (orgContactsEmail.getSubscriberUid() == null) {
					logger.info("{} - {}: Employee is not linked with this organization", CLASS, methodName);
					return exceptionHandlerUtil
							.createErrorResponse(Constant.API_ERROR_EMPLOYEE_IS_NOT_LINKED_WITH_THIS_ORGANIZATION);
				}

				if ("DEACTIVE".equals(orgContactsEmail.getStatus())) {
					logger.info("{} - {}: This employee was already deactivated", CLASS, methodName);
					return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ALREADY_DEACTIVATED, null);
				}

				orgContactsEmail.setStatus(Constant.DEACTIVE);
				orgContactsEmailRepository.save(orgContactsEmail);
				logger.info("{} - {}: Employee deactivated: {}", CLASS, methodName, orgContactsEmail);
			}

			logger.info("{} - {}: Employee deactivated successfully", CLASS, methodName);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_DEACTIVATED, null);

		} catch (Exception e) {
			logger.error("{} - {}: Error while deactivating employee: {}", CLASS, methodName, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	private boolean isNullOrEmpty(String value) {
		return value == null || value.isEmpty();
	}

	public void sendNotification(String fullName, String fcm, boolean link) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Preparing to send notification to {}", CLASS, methodName, fcm);

		// Create notification headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Set notification body
		NotificationDTO notificationBody = new NotificationDTO();
		notificationBody.setTo(fcm);
		notificationBody.setPriority(Constant.HIGH);

		// Set notification data
		NotificationDataDTO dataDTO = new NotificationDataDTO();
		dataDTO.setTitle(Constant.HI + fullName);

		// Set organization link status message
		Map<String, String> orgLinkStatus = new HashMap<>();
		if (link) {
			dataDTO.setBody(Constant.ORGANIZATION_LINK_MESSAGE);
			orgLinkStatus.put(Constant.ORGLINKSTATUS, Constant.PENDING);
		} else {
			dataDTO.setBody(Constant.LINKED_SUCCESSFULLY);
			orgLinkStatus.put(Constant.ORGLINKSTATUS, Constant.SUCCESS);
		}

		// Set notification context and attach to data
		NotificationContextDTO contextDTO = new NotificationContextDTO();
		contextDTO.setPrefOrgLink(orgLinkStatus);
		dataDTO.setNotificationContext(contextDTO);
		notificationBody.setData(dataDTO);

		// Create the request entity
		HttpEntity<Object> requestEntity = new HttpEntity<>(notificationBody, headers);
		logger.info("{} - {}: Sending notification request: {}", CLASS, methodName, requestEntity);

		// Send the request
		try {
			ResponseEntity<Object> res = restTemplate.exchange(orgLinkUrl, HttpMethod.POST, requestEntity,
					Object.class);
			if (res.getStatusCode().is2xxSuccessful()) {
				logger.info("{} - {}: Notification sent successfully", CLASS, methodName);
			} else {
				logger.error("{} - {}: Notification failed with status: {}",
						CLASS, methodName, res.getStatusCode());
			}
		} catch (Exception e) {
			logger.error("{} - {}: Error sending notification: {}", CLASS, methodName, e.getMessage(), e);
		}
	}

	public ApiResponses sendEmail(EmailReqDto otpDto) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Sending email to {}", CLASS, methodName, otpDto.getEmailId());

		try {
			if (otpDto.getEmailId().isEmpty()) {
				logger.warn("{} - {}: Email ID can't be empty", CLASS, methodName);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_MAIL_ID_CANT_BE_EMPTY);
			}
			ApiResponses res = sendEmailToSubscriber(otpDto);

			if (res.isSuccess()) {
				logger.info("{} - {}: Email sent successfully with message: {}", CLASS, methodName, res.getMessage());
				return AppUtil.createApiResponse(true, Constant.OK, null);
			} else {
				logger.error("{} - {}: Failed to send email: {}", CLASS, methodName, res);
				return exceptionHandlerUtil
						.createErrorResponse(Constant.API_ERROR_SOMETHING_WENT_WRONG_PLEASE_TRY_AFTER_SOMETIME);
			}

		} catch (Exception e) {
			logger.error("{} - {}: Error occurred while sending email: {}", CLASS, methodName, e.getMessage(), e);
			return exceptionHandlerUtil.handleHttpException(e);
		}
	}

	@Override
	public ApiResponses orgStatus(String orgUid) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Request for organization status with orgUid: {}", CLASS, methodName, orgUid);
		try {
			
			if (!StringUtils.hasText(orgUid)) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGUID_CANT_BE_NULL_OR_EMPTY);
			}
			
			OrganizationCertificates organizationCertificates = organizationCertificatesRepository.findByorganizationUid(orgUid);
			if (organizationCertificates == null) {
				logger.warn("{} - {}: Organization certificate not found for orgUid: {}", CLASS, methodName, orgUid);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_NOT_ISSUED);
			}

			OrgStatusDto orgStatusDto = new OrgStatusDto();
			orgStatusDto.setCertificateStatus(organizationCertificates.getCertificateStatus());
			orgStatusDto.setCertificateStartDate(organizationCertificates.getCertificateStartDate());
			orgStatusDto.setCertificateEndDate(organizationCertificates.getCertificateEndDate());

			logger.info("{} - {}: Returning organization status for orgUid: {}", CLASS, methodName, orgUid);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ORG_STATUS, orgStatusDto);
		} catch (Exception e) {
			logger.error("{} - {}: Error occurred while fetching organization status for orgUid: {}: {}", CLASS,
					methodName, orgUid, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	public ApiResponses getCertificateDetails(String orgUid) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Request for certificate details with orgUid: {}", CLASS, methodName, orgUid);
		try {
			if (!StringUtils.hasText(orgUid)) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGUID_CANT_BE_NULL_OR_EMPTY);
			}
			OrganizationDetails organizationDetails = organizationDetailsRepository.findByOrganizationUid(orgUid);
			if (organizationDetails == null) {
				logger.warn("{} - {}: Organization details not found for orgUid: {}", CLASS, methodName, orgUid);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGANIZATION_DETAILS_NOT_FOUND);
			}

			OrganizationCertificates organizationCertificates = organizationCertificatesRepository
					.findByorganizationUid(orgUid);
			if (organizationCertificates == null) {
				logger.warn("{} - {}: Certificates not issued for orgUid: {}", CLASS, methodName, orgUid);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_CERTIFICATED_WERE_NOT_ISSUED);
			}

			OrgStatusDto orgStatusDto = new OrgStatusDto();
			orgStatusDto.setCertificateStatus(organizationCertificates.getCertificateStatus());
			orgStatusDto.setCertificateStartDate(organizationCertificates.getCertificateStartDate());
			orgStatusDto.setCertificateEndDate(organizationCertificates.getCertificateEndDate());

			logger.info("{} - {}: Returning certificate details for orgUid: {}", CLASS, methodName, orgUid);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ORG_STATUS, orgStatusDto);

		} catch (Exception e) {
			logger.error("{} - {}: Error occurred while fetching certificate details for orgUid: {}: {}", CLASS,
					methodName, orgUid, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getSigntoryListByOrgId(String organizationUid) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Fetching signatory list for orgUid: {}", CLASS, methodName, organizationUid);

		if (organizationUid == null || organizationUid.isEmpty()) {
			logger.warn("{} - {}: Organization UID cannot be null or empty", CLASS, methodName);
			return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGUID_CANT_BE_NULL_OR_EMPTY);
		}

		try {
			List<OrgContactsEmail> signatoryList = orgContactsEmailRepository
					.getOrganizationSignatoryList(organizationUid);
			if (signatoryList.isEmpty()) {
				logger.warn("{} - {}: No signatories found for orgUid: {}", CLASS, methodName, organizationUid);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_SIGNATORY_LIST_NOT_FOUND);
			}

			List<OrgSigntoryDto> orgSignatories = new ArrayList<>();
			for (OrgContactsEmail orgContactsEmail : signatoryList) {
				Optional<Subscriber> subscriberOpt = Optional
						.ofNullable(subscriberRepository.findBysubscriberUid(orgContactsEmail.getSubscriberUid()));
				if (subscriberOpt.isPresent()) {
					OrgSigntoryDto orgSigntoryDto = new OrgSigntoryDto();
					Subscriber subscriber = subscriberOpt.get();
					orgSigntoryDto.setSuid(subscriber.getSubscriberUid());
					orgSigntoryDto.setFullName(subscriber.getFullName());
					orgSigntoryDto.setEmail(subscriber.getEmailId());
					orgSigntoryDto.setOrganizationEmail(orgContactsEmail.getEmployeeEmail());
					orgSigntoryDto.setHasEseal(orgContactsEmail.iseSealSignatory());

					// Set selfie thumbnail if available
					Optional<SubscriberOnboardingData> onboardingDataOpt = Optional
							.ofNullable(subscriberOnboardingDataRepoIface.getBySubUid(subscriber.getSubscriberUid()));
					onboardingDataOpt.ifPresent(data -> orgSigntoryDto.setThumbNailUri(data.getSelfieThumbnailUri()));

					orgSignatories.add(orgSigntoryDto);
				}
			}

			logger.info("{} - {}: Found {} signatories for orgUid: {}", CLASS, methodName, orgSignatories.size(),
					organizationUid);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_SIGNATORY_LIST_FOUND, orgSignatories);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} - {}: Error occurred while fetching signatory list for orgUid: {}: {}", CLASS, methodName,
					organizationUid, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getCertificateStatusList(OrganizationIdDto orgUidList) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Fetching certificate status for organizations: {}", CLASS, methodName,
				orgUidList.getOrgId());
		if (orgUidList.getOrgId().isEmpty()) {
			return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGUID_DTO_CANT_BE_NULL_OR_EMPTY);
		}

		List<OrganizationCertificates> organizationCertificates = new ArrayList<>();
		try {
			for (String orgId : orgUidList.getOrgId()) {
				Optional<OrganizationCertificates> orgOptional = Optional
						.ofNullable(organizationCertificatesRepository.findByorganizationUid(orgId));
				orgOptional.ifPresent(organizationCertificates::add);
			}

			if (organizationCertificates.isEmpty()) {
				logger.warn("{} - {}: No certificates found for the provided organization UIDs", CLASS, methodName);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_CERTIFICATES_NOT_FOUND);
			}

			logger.info("{} - {}: Found {} certificates for the provided organization UIDs", CLASS, methodName,
					organizationCertificates.size());
			return AppUtil.createApiResponse(true, Constant.OK, organizationCertificates);

		} catch (Exception e) {
			logger.error("{} - {}: Error occurred while fetching certificates for organizations: {}", CLASS, methodName,
					orgUidList.getOrgId(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses checkValidCertificateSerialNumber(String certificateSerialNumber) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Checking validity of certificate serial number: {}", CLASS, methodName,
				certificateSerialNumber);

		try {
			if (certificateSerialNumber == null || certificateSerialNumber.isEmpty()) {
				logger.warn("{} - {}: Certificate serial number cannot be null or empty", CLASS, methodName);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_CERTIFICATES_SERIAL_NUMBER_CANNOT_BE_NULL);
			}

			SubscriberCertificate subscriberCertificate = subscriberCertificatesRepoIface
					.findByCertificateSerialNumber(certificateSerialNumber);
			if (subscriberCertificate == null) {
				logger.warn("{} - {}: No record found for certificate serial number: {}", CLASS, methodName,
						certificateSerialNumber);
				return exceptionHandlerUtil
						.createErrorResponse(Constant.API_ERROR_NO_RECORD_FOUND_GIVEN_CERT_SERIAL_NUMBER);
			}

			logger.info("{} - {}: Certificate details fetched successfully for serial number: {}", CLASS, methodName,
					certificateSerialNumber);
			return exceptionHandlerUtil.createSuccessResponse(
					Constant.API_RESPONSE_SUB_CERT_DETAILS_FETCHED_SUCCESSFULLY, subscriberCertificate);

		} catch (Exception e) {
			logger.error("{} - {}: Error occurred while checking certificate serial number: {}: {}", CLASS, methodName,
					certificateSerialNumber, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses sendEmail(String spocEmail, String orgName) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Sending email to SPOC: {}", CLASS, methodName, spocEmail);
		try {
			if (!StringUtils.hasText(spocEmail)) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_SPOC_EMAIL_CANT_BE_NULL_OR_EMPTY);
			}
			if (!StringUtils.hasText(orgName)) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORG_NAME_CANT_BE_NULL_OR_EMPTY);
			}
			
			List<String> listOfEmail = Collections.singletonList(spocEmail);
			String emailBody = String.format(
					"Dear SPOC,<br><br>We are pleased to inform you that the organization \"%s\" has been successfully created,"
							+ " and you have been designated as the SPOC (Single Point of Contact).<br><br>For any further inquiries"
							+ " please reach out to the administrator.",
					orgName.toUpperCase());
			String subject = Constant.ORGANIZATION_CREATED;

			EmailDto emailDto = new EmailDto();
			emailDto.setEmailBody(emailBody);
			emailDto.setRecipients(listOfEmail);
			emailDto.setSubject(subject);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Object> requestEntity = new HttpEntity<>(emailDto, headers);

			logger.info("{} - {}: Request entity for sending email: {}", CLASS, methodName, requestEntity);
			ResponseEntity<ApiResponses> res = restTemplate.exchange(sendEmail, HttpMethod.POST, requestEntity,
					ApiResponses.class);

			logger.info("{} - {}: Response from email service: {}", CLASS, methodName, res);
			var responseBody1 = res.getBody();
			if (res.getStatusCode().value() == 200) {
				return AppUtil.createApiResponse(
						false,
						responseBody1 != null ? responseBody1.getMessage() : "Error occurred",
						null
				);
			}

			String errorMsg = res.getStatusCode().value() == 400 ? "Bad Request" : "Internal Server Error";
			logger.error("{} - {}: Email service responded with error: {}", CLASS, methodName, errorMsg);
			return AppUtil.createApiResponse(false, errorMsg, null);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} - {}: Error Exception while sending email to SPOC: {}: {}", CLASS,
					methodName, spocEmail, e.getMessage(), e);
			return exceptionHandlerUtil.handleHttpException(e);
		}
	}

	@Override
	public ApiResponses toggleManageByAdmin(ToggleManageByAdmin toggleManageByAdmin) {
		try {
			if(toggleManageByAdmin.getOrgId()==null || toggleManageByAdmin.getOrgId().isEmpty()){
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGUID_CANT_BE_NULL_OR_EMPTY);
			}
			OrganizationDetails organizationDetails=organizationDetailsRepository.findByOrganizationUid(toggleManageByAdmin.getOrgId());
			if(organizationDetails==null){
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGANIZATION_NOT_FOUND);
			}
			organizationDetails.setManageByAdmin(toggleManageByAdmin.isManageByAdmin());
			organizationDetailsRepository.save(organizationDetails);
			return AppUtil.createApiResponse(true,"Admin access updated successfully",null);
		} catch (Exception e) {

			Object methodName = null;
			logger.error("{} - {}: Error while toggling manageByAdmin: {}",
					CLASS, methodName, e.getMessage(), e);
			return exceptionHandlerUtil.handleHttpException(e);
		}


	}

	@Override
	public ApiResponses checkTemplates(List<CheckTemplateDto> dtoList) {
		try {
			if (isInvalidList(dtoList)) {
				return exceptionHandlerUtil.createErrorResponse(
						"Template list cannot be null or empty");
			}

			for (CheckTemplateDto dto : dtoList) {

				ApiResponses validationError = validateInput(dto);
				if (validationError != null) return validationError;

				OrgContactsEmail orgUser =
						orgContactsEmailRepository.getOrganizationSigntoryDetailsBySuidAndOuid(
								dto.getSuid(), dto.getOrganizationId());

				if (orgUser == null) {
					return AppUtil.createApiResponse(false, "Business User Not Found", dtoList);
				}

				dto.setHasSignatureTemplate(determineSignatureStatus(dto.getSignatureTemplateId(), orgUser));
				dto.setHasEsealTemplate(true);
			}

			return AppUtil.createApiResponse(true, "Templates validated successfully", dtoList);

		} catch (Exception e) {
			logger.error("{} - {}: Error Exception while checking templates: {}",
					CLASS, Utility.getMethodName(), e.getMessage(), e);

			return exceptionHandlerUtil.handleHttpException(e);
		}
	}
	private ApiResponses validateInput(CheckTemplateDto dto) {

		if (dto.getOrganizationId() == null || dto.getOrganizationId().isEmpty()) {
			return exceptionHandlerUtil.createErrorResponse(
					Constant.API_ERROR_ORGUID_CANT_BE_NULL_OR_EMPTY
			);
		}

		if (dto.getSignatureTemplateId() == 0) {
			return exceptionHandlerUtil.createErrorResponse(
					Constant.CHECK_TEMPLATE_ID_CANNOT_ZERO
			);
		}

		if (dto.getEsealTemplateId() == 0) {
			return exceptionHandlerUtil.createErrorResponse(
					Constant.CHECK_TEMPLATE_ID_CANNOT_ZERO
			);
		}

		return null; // valid
	}
	private boolean isInvalidList(List<CheckTemplateDto> dtoList) {
		return dtoList == null || dtoList.isEmpty();
	}



	private boolean determineSignatureStatus(int templateId, OrgContactsEmail orgUser) {

		String designation = orgUser.getDesignation();
		String signaturePhoto = orgUser.getSignaturePhoto();

		return switch (templateId) {
			case 1 -> true;

			case 2 -> designation != null && !designation.isEmpty();

			case 3 -> signaturePhoto != null && !signaturePhoto.isEmpty();

			case 4 -> (designation != null && !designation.isEmpty()) &&
					(signaturePhoto != null && !signaturePhoto.isEmpty());

			default -> false;
		};
	}
	@Override
	public ApiResponses getOrganizationStatus() {
		try {

			// Fetch counts
			long total = organizationDetailsRepository.getTotalOrganizations();
			long active = organizationDetailsRepository.getActiveOrganizations();
			long inactive = organizationDetailsRepository.getInactiveOrganizations();
			long registered = organizationDetailsRepository.getRegisteredOrganizations();

			// Prepare response body
			Map<String, Long> stats = new HashMap<>();
			stats.put("totalOrganizations", total);
			stats.put("activeOrganizations", active);
			stats.put("inactiveOrganizations", inactive);
			stats.put("registeredOrganizations", registered);

			return AppUtil.createApiResponse(true, "Organization statistics fetched successfully", stats);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} - {}: Error while fetching organization stats: {}", CLASS, e.getMessage(), e);
			return exceptionHandlerUtil.handleHttpException(e);
		}
	}



}
