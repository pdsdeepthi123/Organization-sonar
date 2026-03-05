package com.dtt.organization.service.impl;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.constant.Constant;
import com.dtt.organization.dto.*;
import com.dtt.organization.enums.CertificateStatus;
import com.dtt.organization.exception.ExceptionHandlerUtil;
import com.dtt.organization.model.*;
import com.dtt.organization.repository.*;
import com.dtt.organization.service.iface.OrgGatewayIface;
import com.dtt.organization.util.AppUtil;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.dtt.organization.constant.Constant.*;

@Service
public class OrgGatewayImpl implements OrgGatewayIface {

	Logger logger= LoggerFactory.getLogger(OrgGatewayImpl.class);

	private final OrgContactsEmailRepository orgContactsEmailRepository;
	private final OrganizationDetailsRepository organizationDetailsRepository;
	private final OrganizationSignatureTemplatesRepository signatureTemplatesRepository;
	private final OrganizationCertificatesRepository organizationCertificatesRepository;
	private final OrgEmailDomainRepository orgEmailDomainRepository;
	private final SubscriberViewRepository subscriberViewRepository;

	private final RestTemplate restTemplate;
	private final ExceptionHandlerUtil exceptionHandlerUtil;


	public OrgGatewayImpl(
			OrgContactsEmailRepository orgContactsEmailRepository,
			OrganizationDetailsRepository organizationDetailsRepository,
			OrganizationSignatureTemplatesRepository signatureTemplatesRepository,
			OrganizationCertificatesRepository organizationCertificatesRepository,
			OrgEmailDomainRepository orgEmailDomainRepository,
			SubscriberViewRepository subscriberViewRepository,

			RestTemplate restTemplate,
			ExceptionHandlerUtil exceptionHandlerUtil
	) {
		this.orgContactsEmailRepository = orgContactsEmailRepository;
		this.organizationDetailsRepository = organizationDetailsRepository;
		this.signatureTemplatesRepository = signatureTemplatesRepository;
		this.organizationCertificatesRepository = organizationCertificatesRepository;
		this.orgEmailDomainRepository = orgEmailDomainRepository;
		this.subscriberViewRepository = subscriberViewRepository;

		this.restTemplate = restTemplate;
		this.exceptionHandlerUtil = exceptionHandlerUtil;
	}



	@Value("${orgLink.notifyurl}")
	private String orgLinkUrl;

	@Override
	public ApiResponses getBusinessUsers(String orgId) {

		if (orgId == null || orgId.isEmpty()) {
			return exceptionHandlerUtil.createErrorResponse("api.error.org.id.required");
		}

		List<OrgContactsEmail> list =
				orgContactsEmailRepository.findByOrganizationUid(orgId);

		if (list == null || list.isEmpty()) {
			return exceptionHandlerUtil.createErrorResponse("api.error.users.not.found");
		}

		List<OrgUser> responseList = new ArrayList<>();

		for (OrgContactsEmail entity : list) {
			OrgUser user = new OrgUser();
			user.setStatus(CertificateStatus.ACTIVE.toString());
			user.setUgpassEmail(entity.getUgpassEmail());
			user.setSignaturePhoto(entity.getSignaturePhoto());
			user.setTemplate(entity.isTemplate());
			user.setDelegate(entity.isDelegate());
			user.setOrgContactsEmailId(entity.getOrgContactsEmailId());
			user.setSignatory(entity.isSignatory());
			user.setSubscriberUid(entity.getSubscriberUid());
			user.setUgpassUserLinkApproved(entity.isUgpassUserLinkApproved());
			user.setOrganizationUid(entity.getOrganizationUid());
			user.setNationalIdNumber(entity.getNationalIdNumber());
			user.setEmployeeEmail(entity.getEmployeeEmail());
			user.setBulksign(entity.isBulksign());
			user.setDesignation(entity.getDesignation());
			user.seteSealPrepatory(entity.iseSealPreparatory());
			user.setMobileNumber(entity.getMobileNumber());
			user.setPassportNumber(entity.getPassportNumber());
			user.seteSealSignatory(entity.iseSealSignatory());
			user.setDigitalFormPrivilege(entity.isDigitalFormPrivilege());
			responseList.add(user);
		}

		return exceptionHandlerUtil.createSuccessResponse(
				"api.success.users.list",
				responseList
		);
	}

	@Override
	public ApiResponses getBusinessUserById(Integer id) {
		try {
				if (id == null) {
					return exceptionHandlerUtil.createErrorResponse("api.error.user.id.required");
				}

				Optional<OrgContactsEmail> optional =
						orgContactsEmailRepository.findById(id);

				if (!optional.isPresent()) {
					return exceptionHandlerUtil.createErrorResponse("api.error.user.not.found");
				}

				OrgContactsEmail entity = optional.get();

				OrgUser user = new OrgUser();
				user.setStatus(CertificateStatus.ACTIVE.toString());
				user.setUgpassEmail(entity.getUgpassEmail());
				user.setSignaturePhoto(entity.getSignaturePhoto());
				user.setTemplate(entity.isTemplate());
				user.setDelegate(entity.isDelegate());
				user.setOrgContactsEmailId(entity.getOrgContactsEmailId());
				user.setSignatory(entity.isSignatory());
				user.setSubscriberUid(entity.getSubscriberUid());
				user.setUgpassUserLinkApproved(entity.isUgpassUserLinkApproved());
				user.setOrganizationUid(entity.getOrganizationUid());
				user.setNationalIdNumber(entity.getNationalIdNumber());
				user.setEmployeeEmail(entity.getEmployeeEmail());
				user.setBulksign(entity.isBulksign());
				user.setDesignation(entity.getDesignation());
				user.seteSealPrepatory(entity.iseSealPreparatory());
				user.setMobileNumber(entity.getMobileNumber());
				user.setPassportNumber(entity.getPassportNumber());
				user.seteSealSignatory(entity.iseSealSignatory());
				user.setDigitalFormPrivilege(entity.isDigitalFormPrivilege());

				return exceptionHandlerUtil.createSuccessResponse(
						"api.success.user.found",
						user
				);
			}
		catch (Exception e) {
			logger.error("Exception occurred", e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses updateBusinessUser(OrgUser orgUser) {
		try {
			Optional<OrgContactsEmail> orgContactsEmail = Optional.ofNullable(orgContactsEmailRepository
					.getOrganisationByUidAndEmail(orgUser.getOrganizationUid(), orgUser.getEmployeeEmail()));

			if (orgContactsEmail.isPresent()) {
				OrgContactsEmail orgContactsEmail1 = orgContactsEmail.get();
				orgContactsEmail1.setDelegate(orgUser.isDelegate());
				orgContactsEmail1.setEmployeeEmail(orgUser.getEmployeeEmail());
				orgContactsEmail1.setBulksign(orgUser.isBulksign());
				orgContactsEmail1.setOrgContactsEmailId(orgContactsEmail1.getOrgContactsEmailId());
				orgContactsEmail1.setDesignation(orgUser.getDesignation());
				orgContactsEmail1.seteSealPreparatory(orgUser.iseSealPrepatory());
				orgContactsEmail1.seteSealSignatory(orgUser.iseSealSignatory());
				orgContactsEmail1.setMobileNumber(orgUser.getMobileNumber());
				orgContactsEmail1.setPassportNumber(orgUser.getPassportNumber());
				orgContactsEmail1.setNationalIdNumber(orgUser.getNationalIdNumber());
				orgContactsEmail1.setOrganizationUid(orgUser.getOrganizationUid());
				orgContactsEmail1.setSignatory(orgUser.isSignatory());
				orgContactsEmail1.setTemplate(orgUser.isTemplate());
				orgContactsEmail1.setSignaturePhoto(orgUser.getSignaturePhoto());
				orgContactsEmail1.setUgpassEmail(orgUser.getUgpassEmail());
				orgContactsEmail1.setInitial(orgUser.getInitial());
				orgContactsEmail1.setDigitalFormPrivilege(orgUser.isDigitalFormPrivilege());

				OrgContactsEmail contactsEmail = orgContactsEmailRepository.save(orgContactsEmail1);

				return exceptionHandlerUtil.createSuccessResponse( "api.response.Business.user.updated", contactsEmail);
			} else {
				return exceptionHandlerUtil.createErrorResponse( "api.error.no.subscribers.found");
			}

		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getEsealLogo(String orgUid) {
		try {

			if (orgUid == null || orgUid.trim().isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(
						API_ERROR_ORGANIZATION_ID_CANNOT_BE_NULL
				);
			}

			OrganizationDetails orgDetails =
					organizationDetailsRepository.findByOrganizationUid(orgUid);

			if (orgDetails == null) {
				return exceptionHandlerUtil.createErrorResponse(
						API_ERROR_ORGANIZATION_NOT_FOUND
				);
			}

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.eseal.logo.fetch.success",
					orgDetails.geteSealImage()
			);

		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses updateEsealLogo(UpdateEsealDto updateEsealDto) {
		try {

			if (updateEsealDto.getOrgUid() == null ||
					updateEsealDto.getOrgUid().trim().isEmpty()) {

				return exceptionHandlerUtil.createErrorResponse(
						API_ERROR_ORGANIZATION_ID_CANNOT_BE_NULL
				);
			}

			if (updateEsealDto.geteSealImage() == null) {

				return exceptionHandlerUtil.createErrorResponse(
						"api.error.eseal.image.null"
				);
			}

			OrganizationDetails organizationDetails =
					organizationDetailsRepository
							.findByOrganizationUid(updateEsealDto.getOrgUid());

			if (organizationDetails == null) {

				return exceptionHandlerUtil.createErrorResponse(
						API_ERROR_ORGANIZATION_NOT_FOUND
				);
			}

			organizationDetails.seteSealImage(updateEsealDto.geteSealImage());
			organizationDetails.setAuthorizedLetterForSignatories(
					updateEsealDto.getAuthorizedLetterForSignatories()
			);

			organizationDetailsRepository.save(organizationDetails);

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.eseal.logo.update.success",
					null
			);

		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses updateSignatureTemplatesById(
			SignatureTemplateUpdateDto signatureTemplateUpdateDto) {

		try {

			if (signatureTemplateUpdateDto.getOrganizationUid() == null ||
					signatureTemplateUpdateDto.getOrganizationUid().trim().isEmpty()) {

				return exceptionHandlerUtil.createErrorResponse(
						API_ERROR_ORGANIZATION_ID_CANNOT_BE_NULL
				);
			}

			if (signatureTemplateUpdateDto.getEsealSignatureTemplateId() == 0 ||
					signatureTemplateUpdateDto.getSignatureTemplateId() == 0) {

				return exceptionHandlerUtil.createErrorResponse(
						"api.error.signature.template.id.null"
				);
			}

			if (signatureTemplatesRepository
					.getUserSignatureTemplatesDetails(
							signatureTemplateUpdateDto.getOrganizationUid()) == null) {

				return exceptionHandlerUtil.createErrorResponse(
						"api.error.signature.template.record.not.found"
				);
			}

			OrganizationSignatureTemplates signatureTemplates =
					signatureTemplatesRepository
							.getOrgSignatureDetailsByType(
									signatureTemplateUpdateDto.getOrganizationUid(),
									"SIGN");

			signatureTemplates.setTemplateId(
					signatureTemplateUpdateDto.getSignatureTemplateId());

			signatureTemplatesRepository.save(signatureTemplates);

			OrganizationSignatureTemplates templates =
					signatureTemplatesRepository
							.getOrgSignatureDetailsByType(
									signatureTemplateUpdateDto.getOrganizationUid(),
									"ESEAL");

			templates.setTemplateId(
					signatureTemplateUpdateDto.getEsealSignatureTemplateId());

			signatureTemplatesRepository.save(templates);

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.signature.templates.update.success",
					null
			);

		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getSignatureTemplateById(String orgUid) {
		try {

			if (orgUid == null || orgUid.trim().isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(
						API_ERROR_ORGANIZATION_ID_CANNOT_BE_NULL
				);
			}

			OrganizationSignatureTemplates signatureTemplates =
					signatureTemplatesRepository
							.getOrgSignatureDetailsByType(orgUid, "SIGN");

			OrganizationSignatureTemplates templates =
					signatureTemplatesRepository
							.getOrgSignatureDetailsByType(orgUid, "ESEAL");

			if (signatureTemplates == null || templates == null) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.signature.template.record.not.found"
				);
			}

			SignatureTemplateUpdateDto signatureTemplateUpdateDto =
					new SignatureTemplateUpdateDto();

			signatureTemplateUpdateDto.setEsealSignatureTemplateId(
					templates.getTemplateId());

			signatureTemplateUpdateDto.setSignatureTemplateId(
					signatureTemplates.getTemplateId());

			signatureTemplateUpdateDto.setOrganizationUid(orgUid);

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.signature.templates.fetch.success",
					signatureTemplateUpdateDto
			);

		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}



	private ApiResponses validateUserDuplicates(OrgUser user) {

		if (exists(orgContactsEmailRepository
				.findByOrganizationUidAndEmail(user.getOrganizationUid(), user.getEmployeeEmail()))) {
			return exceptionHandlerUtil.createErrorResponse("api.error.business.user.duplicate.email");
		}

		if (hasValue(user.getPassportNumber()) &&
				exists(orgContactsEmailRepository.getSubscriberDetailsByOuidAndPassport(
						user.getPassportNumber(), user.getOrganizationUid()))) {
			return exceptionHandlerUtil.createErrorResponse(API_ERROR_BUSINESS_USER_DUPLICATE_FOUND);
		}

		if (hasValue(user.getNationalIdNumber()) &&
				exists(orgContactsEmailRepository.getSubscriberDetailsByOuidAndNin(
						user.getNationalIdNumber(), user.getOrganizationUid()))) {
			return exceptionHandlerUtil.createErrorResponse(API_ERROR_BUSINESS_USER_DUPLICATE_FOUND);
		}

		if (hasValue(user.getUgpassEmail()) &&
				exists(orgContactsEmailRepository.getSubscriberDetailsByOuidAndEmail(
						user.getUgpassEmail(), user.getOrganizationUid()))) {
			return exceptionHandlerUtil.createErrorResponse("api.error.business.user.duplicate");
		}

		if (hasValue(user.getMobileNumber()) &&
				exists(orgContactsEmailRepository.getSubscriberDetailsByOuidAndNUMBER(
						user.getMobileNumber(), user.getOrganizationUid()))) {
			return exceptionHandlerUtil.createErrorResponse("api.error.business.user.duplicate");
		}

		return null; // No errors
	}

	private OrgContactsEmail mapToOrgContactsEmail(OrgUser user) {
		OrgContactsEmail c = new OrgContactsEmail();

		c.setStatus(CertificateStatus.ACTIVE.toString());
		c.setUgpassEmail(user.getUgpassEmail());
		c.setSignaturePhoto(user.getSignaturePhoto());
		c.setTemplate(user.isTemplate());
		c.setDelegate(user.isDelegate());
		c.setOrgContactsEmailId(user.getOrgContactsEmailId());
		c.setSignatory(user.isSignatory());
		c.setSubscriberUid(user.getSubscriberUid());
		c.setUgpassUserLinkApproved(user.isUgpassUserLinkApproved());
		c.setOrganizationUid(user.getOrganizationUid());
		c.setNationalIdNumber(user.getNationalIdNumber());
		c.setEmployeeEmail(user.getEmployeeEmail());
		c.setBulksign(user.isBulksign());
		c.setDesignation(user.getDesignation());
		c.seteSealPreparatory(user.iseSealPrepatory());
		c.setMobileNumber(user.getMobileNumber());
		c.setPassportNumber(user.getPassportNumber());
		c.seteSealSignatory(user.iseSealSignatory());
		c.setInitial(user.getInitial());
		c.setDigitalFormPrivilege(user.isDigitalFormPrivilege());

		return c;
	}

	private boolean exists(List<?> list) {
		return list != null && !list.isEmpty();
	}

	private boolean hasValue(String value) {
		return value != null && !value.isEmpty();
	}

	@Override
	public ApiResponses addMultipleBusinessUsers(List<OrgUser> orgUserList) {
		try {
			List<OrgContactsEmail> savedUsers = new ArrayList<>();

			for (OrgUser user : orgUserList) {

				// Validate duplicates (email, passport, NIN, mobile, ugpassEmail)
				ApiResponses validationError = validateUserDuplicates(user);
				if (validationError != null) return validationError;

				// Convert OrgUser → OrgContactsEmail
				OrgContactsEmail newContact = mapToOrgContactsEmail(user);

				// Notification logic unchanged
				handleUserNotifications(newContact);

				orgContactsEmailRepository.save(newContact);
				savedUsers.add(newContact);
			}

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.business.users.add.success",
					savedUsers
			);

		} catch (Exception e) {
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses updateOrganisationEGSpoc(
			EnterpriseGatewayOrganisationUpdateDto gatewayOrganisationUpdateDto) {

		try {

			if (gatewayOrganisationUpdateDto.getOrganizationUid() == null ||
					gatewayOrganisationUpdateDto.getOrganizationUid().trim().isEmpty()) {

				return exceptionHandlerUtil.createErrorResponse(
						"api.error.organisation.uid.null"
				);
			}

			OrganizationDetails organizationDetails =
					organizationDetailsRepository
							.findByOrganizationUid(
									gatewayOrganisationUpdateDto.getOrganizationUid());

			if (organizationDetails == null) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.organisation.not.found"
				);
			}

			if (gatewayOrganisationUpdateDto.getSpocUgpassEmail() == null ||
					gatewayOrganisationUpdateDto.getSpocUgpassEmail().trim().isEmpty()) {

				organizationDetails.setSpocUgpassEmail(
						organizationDetails.getOrganizationEmail());

			} else {

				organizationDetails.setSpocUgpassEmail(
						gatewayOrganisationUpdateDto.getSpocUgpassEmail());
			}

			organizationDetailsRepository.save(organizationDetails);

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.organisation.update.success",
					null
			);

		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses updateOrganisationEGAgent(EnterpriseGatewayOrganisationUpdateDto gatewayOrganisationUpdateDto) {
		try {

			if (gatewayOrganisationUpdateDto.getOrganizationUid() == null
					|| gatewayOrganisationUpdateDto.getOrganizationUid().trim().isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(API_ERROR_ORGANIZATION_ID_CANNOT_BE_NULL);
			}

			OrganizationDetails organizationDetails = organizationDetailsRepository
					.findByOrganizationUid(gatewayOrganisationUpdateDto.getOrganizationUid());

			if (organizationDetails == null) {
				return exceptionHandlerUtil.createErrorResponse(API_ERROR_ORGANIZATION_NOT_FOUND );
			}

			if (gatewayOrganisationUpdateDto.getAgentUrl() != null
					&& !gatewayOrganisationUpdateDto.getAgentUrl().trim().isEmpty()) {
				organizationDetails.setAgentUrl(gatewayOrganisationUpdateDto.getAgentUrl());
			}

			organizationDetailsRepository.save(organizationDetails);

			return exceptionHandlerUtil.createSuccessResponse("api.response.organization.updated",null);

		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getOrganizationCertificateDetailsByOrgUid(String orgUid) {
		try {

			OrganizationCertificates organizationCertificates =
					organizationCertificatesRepository.findByorganizationUid(orgUid);

			if (organizationCertificates == null) {
				return exceptionHandlerUtil.createSuccessResponse(
						"api.response.organization.certificate.not.found", null);
			}

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.organization.certificate.found",
					organizationCertificates);

		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses deleteBusinessUser(String orgId, String email) {
		try {

			if (orgId == null || orgId.trim().isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.organization.id.required");
			}

			if (email == null || email.trim().isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.email.required");
			}

			Optional<OrgContactsEmail> orgContactsEmail = Optional.ofNullable(
					orgContactsEmailRepository.getOrganisationByUidAndEmail(orgId, email));

			if (orgContactsEmail.isPresent()) {
				orgContactsEmailRepository.delete(orgContactsEmail.get());
				return exceptionHandlerUtil.createSuccessResponse(
						"api.response.business.user.deleted",null);
			} else {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.business.user.not.found");
			}

		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses updateEmailDomain(UpdateEmailDomainDto updateEmailDomainDto) {
		try {

			if (updateEmailDomainDto.getOrganizationUid() == null
					|| updateEmailDomainDto.getOrganizationUid().trim().isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(
						API_ERROR_ORGANIZATION_ID_CANNOT_BE_NULL);
			}

			OrganizationDetails organizationDetails = organizationDetailsRepository
					.findByOrganizationUid(updateEmailDomainDto.getOrganizationUid());

			if (organizationDetails == null) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.organization.not.found");
			}

			OrganizationEmailDomain organizationEmailDomain = orgEmailDomainRepository
					.findByOrganizationUid(updateEmailDomainDto.getOrganizationUid());

			if (organizationEmailDomain == null) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.email.domain.not.found");
			}

			organizationEmailDomain.setEmailDomain(updateEmailDomainDto.getEmailDomain());
			organizationEmailDomain.setUpdatedOn(AppUtil.getDate());

			orgEmailDomainRepository.save(organizationEmailDomain);

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.email.domain.updated",
					updateEmailDomainDto.getEmailDomain());

		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getEmailDomain(String ouid) {
		try {

			if (ouid == null || ouid.trim().isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(
						API_ERROR_ORGANIZATION_ID_CANNOT_BE_NULL);
			}

			OrganizationDetails organizationDetails =
					organizationDetailsRepository.findByOrganizationUid(ouid);

			if (organizationDetails == null) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.organization.not.found");
			}

			OrganizationEmailDomain organizationEmailDomain =
					orgEmailDomainRepository.findByOrganizationUid(ouid);

			if (organizationEmailDomain == null) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.email.domain.not.found");
			}

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.email.domain.fetched",
					organizationEmailDomain.getEmailDomain());

		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	private void handleUserNotifications(OrgContactsEmail orgUser) {
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
			logger.error("{}", e.getMessage());
		}
	}


	public void sendNotification(String fullName, String fcm, boolean link) {
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


		// Send the request
		try {
			ResponseEntity<Object> res = restTemplate.exchange(orgLinkUrl, HttpMethod.POST, requestEntity,
					Object.class);
			if (res.getStatusCode().is2xxSuccessful()) {
				logger.info("NOTIFICATION SENT");
			} else {
				logger.info("NOTIFICATION SENT FAILED");
			}
		} catch (Exception e) {
			exceptionHandlerUtil.handleHttpException(e);
			logger.error("{}", e.getMessage());
		}
	}
}