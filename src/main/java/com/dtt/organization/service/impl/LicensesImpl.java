package com.dtt.organization.service.impl;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.dtt.organization.exception.ExceptionHandlerUtil;
import com.dtt.organization.util.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import org.springframework.web.client.RestTemplate;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.EmailDto;
import com.dtt.organization.dto.SoftwareLicensesDTO;
import com.dtt.organization.model.LicenseDeviceList;
import com.dtt.organization.model.OrganizationDetails;
import com.dtt.organization.model.OrganizationDetailsForClient;
import com.dtt.organization.model.SoftwareLicenseApprovalRequests;
import com.dtt.organization.model.SoftwareLicenses;

import com.dtt.organization.model.Subscriber;
import com.dtt.organization.repository.LicenseDeviceListRepo;
import com.dtt.organization.repository.OrganizationDetailsForClientRepoIface;
import com.dtt.organization.repository.OrganizationDetailsRepository;
import com.dtt.organization.repository.SoftwareLicenseApprovalRequestsRepo;

import com.dtt.organization.repository.SoftwareLicensesRepository;
import com.dtt.organization.repository.SubscriberRepository;
import com.dtt.organization.service.iface.LicensesIface;
import com.dtt.organization.util.AppUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ug.daes.DAESService;
import ug.daes.Result;

import static com.dtt.organization.constant.Constant.*;

@Service
public class LicensesImpl implements LicensesIface {

	@Value(value = "${apply.forgenerate.licenses}")
	private boolean generateLicensesAdmin;

	private final SoftwareLicensesRepository softwareLicensesRepository;

	private final OrganizationDetailsForClientRepoIface organizationDetailsForClientRepoIface;
	private final SoftwareLicenseApprovalRequestsRepo softwareLicenseApprovalRequestsRepo;
	private final OrganizationDetailsRepository organizationDetailsRepository;
	private final SubscriberRepository subscriberRepository;
	private final LicenseDeviceListRepo licenseDeviceListRepo;
	private final RestTemplate restTemplate;
	private final ExceptionHandlerUtil exceptionHandlerUtil;

	public LicensesImpl(
			SoftwareLicensesRepository softwareLicensesRepository,

			OrganizationDetailsForClientRepoIface organizationDetailsForClientRepoIface,
			SoftwareLicenseApprovalRequestsRepo softwareLicenseApprovalRequestsRepo,
			OrganizationDetailsRepository organizationDetailsRepository,
			SubscriberRepository subscriberRepository,
			LicenseDeviceListRepo licenseDeviceListRepo,
			RestTemplate restTemplate,
			ExceptionHandlerUtil exceptionHandlerUtil) {

		this.softwareLicensesRepository = softwareLicensesRepository;

		this.organizationDetailsForClientRepoIface = organizationDetailsForClientRepoIface;
		this.softwareLicenseApprovalRequestsRepo = softwareLicenseApprovalRequestsRepo;
		this.organizationDetailsRepository = organizationDetailsRepository;
		this.subscriberRepository = subscriberRepository;
		this.licenseDeviceListRepo = licenseDeviceListRepo;
		this.restTemplate = restTemplate;
		this.exceptionHandlerUtil = exceptionHandlerUtil;
	}

	@Value(value = "${url.admin.emaillist}")
	private String url;

	@Value(value = "${max.adminEmails}")
	private int noOfAdminEmail;

	@Value(value = "${privatekey.for.license}")
	private String privatekey;

	@Value(value = "${send.email.url}")
	private String sendEmail;

	@Value(value = "${send.email.adminURL}")
	private String sendEmailAdmin;

	@Value("${save.client}")
	String saveClient;

	@Value("${application.uri}")
	String applicationUri;

	@Value("${logout.uri}")
	String logoutUri;

	@Value("${redirect.uri}")
	String redirectUri;

	@Value("${file.crt}")
	String crtFile;


	private static final String CLASS = LicensesImpl.class.getSimpleName();
	Logger logger = LoggerFactory.getLogger(LicensesImpl.class);


	@Override
	public ApiResponses applyForGenerateLicenses(
			SoftwareLicensesDTO dto,
			HttpHeaders httpHeaders) {

		try {

			ApiResponses validationResponse = validateRequest(dto);
			if (validationResponse != null) {
				return validationResponse;
			}

			OrganizationDetails organizationDetails =
					organizationDetailsRepository
							.findByOrganizationUid(dto.getOuid());

			if (organizationDetails == null) {
				return exceptionHandlerUtil
						.createErrorResponse("api.error.organization.not.found");
			}

			SoftwareLicenses softwareLicenses =
					softwareLicensesRepository.findByOuidAndLicenseType(
							dto.getOuid(),
							dto.getLicenseType());

			ApiResponses activeValidation =
					validateExistingActiveLicense(softwareLicenses);
			if (activeValidation != null) {
				return activeValidation;
			}

			return generateLicensesAdmin
					? handleAdminFlow(dto, softwareLicenses, organizationDetails)
					: handleUserFlow(dto, softwareLicenses, organizationDetails);

		} catch (Exception e) {
			logger.error("{} - {} : Exception occurred during APPLY LICENSE",
					CLASS, Utility.getMethodName(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	private ApiResponses handleAdminFlow(
			SoftwareLicensesDTO dto,
			SoftwareLicenses softwareLicenses,
			OrganizationDetails organizationDetails) {

		LocalDate currentDate = LocalDate.now();
		String issuedOn = currentDate.toString();
		String validUptoDate =
				"COMMERICAL".equalsIgnoreCase(dto.getLicenseType())
						? currentDate.plusDays(365).toString()
						: currentDate.plusDays(30).toString();

		String licenseBase64 = generateLicenses(
				dto, dto.getLicenseType());

		if (softwareLicenses == null) {
			softwareLicenses = new SoftwareLicenses();
			softwareLicenses.setCreatedDateTime(AppUtil.getDate());
		}

		populateCommonFields(
				softwareLicenses, dto, organizationDetails);

		softwareLicenses.setIssuedOn(issuedOn);
		softwareLicenses.setValidUpTo(validUptoDate);
		softwareLicenses.setLicenceStatus("ACTIVE");
		softwareLicenses.setLicenseInfo(licenseBase64);

		softwareLicensesRepository.save(softwareLicenses);
		sendEmail(dto);

		return exceptionHandlerUtil.createSuccessResponse(
				"api.response.license.generated",
				softwareLicenses);
	}

	private ApiResponses handleUserFlow(
			SoftwareLicensesDTO dto,
			SoftwareLicenses softwareLicenses,
			OrganizationDetails organizationDetails) {

		if (softwareLicenses != null &&
				APPLIED.equalsIgnoreCase(
						softwareLicenses.getLicenceStatus())) {

			return exceptionHandlerUtil
					.createErrorResponse(
							"api.error.license.already.applied");
		}

		if (softwareLicenses == null) {
			softwareLicenses = new SoftwareLicenses();
			softwareLicenses.setCreatedDateTime(AppUtil.getDate());
		}

		populateCommonFields(
				softwareLicenses, dto, organizationDetails);

		softwareLicenses.setLicenceStatus(APPLIED);

		softwareLicensesRepository.save(softwareLicenses);
		sendEmailToAdmin(dto);

		return exceptionHandlerUtil.createSuccessResponse(
				"api.response.license.request.submitted",
				null);
	}


	private ApiResponses validateRequest(SoftwareLicensesDTO dto) {

		if (!StringUtils.hasText(dto.getOuid())) {
			return exceptionHandlerUtil
					.createErrorResponse(API_ERROR_ORGANIZATION_ID_REQUIRED);
		}
		return null;

	}

	private ApiResponses validateExistingActiveLicense(
			SoftwareLicenses softwareLicenses) {

		if (softwareLicenses != null &&
				"ACTIVE".equalsIgnoreCase(
						softwareLicenses.getLicenceStatus())) {

			LocalDate validUpto =
					LocalDate.parse(softwareLicenses.getValidUpTo());

			if (validUpto.isAfter(LocalDate.now())) {
				return exceptionHandlerUtil
						.createErrorResponse(
								"api.error.license.already.active");
			}
		}

		return null;
	}
	@Override
	public ApiResponses downloadLicense(String ouid, String type) {
		try {

			if (ouid == null || ouid.isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.organization.id.required");
			}

			if (type == null || type.isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.license.type.required");
			}

			SoftwareLicenses softwareLicenses =
					softwareLicensesRepository.findByOuidAndLicenseType(ouid, type);

			if (softwareLicenses == null) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.license.not.found");
			}

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.license.download.success",
					softwareLicenses.getLicenseInfo());

		} catch (Exception e) {
			logger.error("{} - {} : Exception occurred during DOWNLOAD LICENSE: {}",
					CLASS, Utility.getMethodName(), e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}



	@Override
	public ApiResponses getLicenseByOuid(String ouid) {
		try {

			if (ouid == null || ouid.isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.organization.id.required");
			}

			List<SoftwareLicenses> licenses =
					softwareLicensesRepository.findByOuid(ouid);

			if (licenses == null || licenses.isEmpty()) {
				return exceptionHandlerUtil.createSuccessResponse(
						"api.response.license.not.available",
						new ArrayList<>());
			}

			LocalDate today = LocalDate.now();

			for (SoftwareLicenses s : licenses) {

				if (!"APPLIED".equalsIgnoreCase(s.getLicenceStatus())
						&& s.getValidUpTo() != null) {

					LocalDate expiryDate = LocalDate.parse(s.getValidUpTo());

					if (expiryDate.isBefore(today)) {
						s.setLicenceStatus(EXPIRED);
						softwareLicensesRepository.save(s);
					}

					s.setValidUpTo(
							expiryDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
				}
			}

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.licenses.fetched",
					licenses);

		} catch (Exception e) {
			logger.error("{} - {} : Exception in getLicenseByOuid: {}",
					CLASS, Utility.getMethodName(), e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}



	@Override
	public ApiResponses getLicenseByOuidVG(String ouid) {
		try {
			if (ouid == null || ouid.isEmpty()) {
				return AppUtil.createApiResponse(false, "Organisation Id cannot be null", null);
			}

			SoftwareLicenses s = softwareLicensesRepository.findByOuidVG(ouid);
			SoftwareLicenses softwareLicenses = new SoftwareLicenses();

			SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat apiFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date currentDate = dbFormat.parse(LocalDate.now().toString());
			if (!"APPLIED".equalsIgnoreCase(s.getLicenceStatus()) && s.getValidUpTo() != null) {
				Date expiredDate = dbFormat.parse(s.getValidUpTo());

				if (expiredDate.before(currentDate)) {
					String[] result = s.getApplicationName().split("_");
					String lastRecord = result[result.length - 1];
					String[] applicationName = s.getApplicationName().split("_" + lastRecord);
					String softwareName = applicationName[0];
					softwareLicenses.setLicenceStatus(EXPIRED);
					SoftwareLicenseApprovalRequests softwareLicenseApprovalRequestsmodel =
							softwareLicenseApprovalRequestsRepo.getSoftwareDetails(ouid, s.getLicenseType(), softwareName);
					if (softwareLicenseApprovalRequestsmodel != null) {
						softwareLicenseApprovalRequestsmodel.setApprovalStatus(EXPIRED);
						softwareLicenseApprovalRequestsRepo.save(softwareLicenseApprovalRequestsmodel);
					}
					softwareLicensesRepository.save(s);
				}
				softwareLicenses.setValidUpTo(apiFormat.format(expiredDate));
			}

			softwareLicenses.setLicenceStatus(s.getLicenceStatus());

            return AppUtil.createApiResponse(true, "Licenses Fetched Successfully", softwareLicenses);

		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, "An Exception Occurred", null);
		}
	}



	private String generateLicenses(SoftwareLicensesDTO softwareLicensesDTO,
									String type) {
		try {
			logger.info(" type {}" ,type);
			OrganizationDetailsForClient organizationDetailsForClient ;


			organizationDetailsForClient = organizationDetailsForClientRepoIface
					.getClientId(softwareLicensesDTO.getApplicationType(), softwareLicensesDTO.getOuid());

			if(organizationDetailsForClient != null) {
				softwareLicensesDTO.setClientId(organizationDetailsForClient.getClientId());
			}

			String s = softwareLicensesDTO.licenseInfoNew(softwareLicensesDTO.getOuid(), type, "macaddress",
					softwareLicensesDTO.getClientId());
			logger.info("json-->{}",s);
			Result res = DAESService.createSecureWireData(s);


            return new String(res.getResponse());

		} catch (Exception e) {

			return e.getMessage();
		}
	}




	@Override
	public ApiResponses getListForGenerateLicense() {
		try {

			List<SoftwareLicenses> list =
					softwareLicensesRepository.getListForGenerateLicenses();

			if (list == null || list.isEmpty()) {
				return exceptionHandlerUtil.createSuccessResponse(
						"api.response.no.records",
						null);
			}

			return exceptionHandlerUtil.createSuccessResponse(
					API_RESPONSE_FETCHES_SUCCESSFULLY,
					list);

		} catch (Exception e) {
			logger.error("{} - {} : Exception in getListForGenerateLicense: {}",
					CLASS, Utility.getMethodName(), e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	public ApiResponses sendEmail(SoftwareLicensesDTO softwareLicensesDTO) {
		try {

			OrganizationDetails organizationDetails =
					organizationDetailsRepository
							.findByOrganizationUid(softwareLicensesDTO.getOuid());

			if (organizationDetails == null) {
				return exceptionHandlerUtil.createSuccessResponse(
						"api.response.organization.not.found",
						null);
			}

			Subscriber subscriber =
					subscriberRepository.getSubscriber(
							organizationDetails.getSpocUgpassEmail());

			if (subscriber == null) {
				return exceptionHandlerUtil.createSuccessResponse(
						"api.response.subscriber.not.found",
						null);
			}

			List<String> listOfEmail = new ArrayList<>();
			listOfEmail.add(organizationDetails.getSpocUgpassEmail());

			String emailBody = "Greetings! " + subscriber.getFullName()
					+ ",<br>Your software license request for the organization \""
					+ organizationDetails.getOrganizationName()
					+ "\" has been approved. Please proceed to download it from the service provider portal.";

			EmailDto emailDto = new EmailDto();
			emailDto.setEmailBody(emailBody);
			emailDto.setRecipients(listOfEmail);
			emailDto.setSubject("Software License is generated successfully");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Object> requestEntity = new HttpEntity<>(emailDto, headers);

			ResponseEntity<ApiResponses> res =
					restTemplate.exchange(sendEmail,
							HttpMethod.POST,
							requestEntity,
							ApiResponses.class);


			if (res.getStatusCode().value() == 200) {// Extracting to a local variable guarantees null-safety for the linter
				var responseBody = res.getBody();

				return exceptionHandlerUtil.createSuccessResponse(
						API_RESPONSE_EMAIL_SENT,
						responseBody != null ? responseBody.getResult() : null);
			} else if (res.getStatusCode().value() == 400) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.response.bad.request");
			} else if (res.getStatusCode().value() == 500) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.response.internal.server.error");
			}

			return exceptionHandlerUtil.createSuccessResponse(
					API_RESPONSE_EMAIL_SENT,
					null);

		} catch (Exception e) {

			logger.error("{} - {} : Exception in sendEmail: {}",
					CLASS, Utility.getMethodName(), e.getMessage());

			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses sendEmailToAdmin(SoftwareLicensesDTO softwareLicensesDTO) {
		try {

			String[] result = softwareLicensesDTO.getApplicationType().split("_");
			String lastRecord = result[result.length - 1];
			String[] applicationName =
					softwareLicensesDTO.getApplicationType().split("_" + lastRecord);
			String softwareName = applicationName[0];

			OrganizationDetails organizationDetails =
					organizationDetailsRepository
							.findByOrganizationUid(softwareLicensesDTO.getOuid());

			ApiResponses response = getAdminEmailList();

			if (!response.isSuccess()) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.response.admin.email.fetch.failed");
			}

			EmailDto emailDto = getEmailDto(response, softwareName, organizationDetails);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Object> requestEntity = new HttpEntity<>(emailDto, headers);

			ResponseEntity<ApiResponses> res =
					restTemplate.exchange(sendEmailAdmin,
							HttpMethod.POST,
							requestEntity,
							ApiResponses.class);

			// Extracting this prevents the linter from warning about consecutive getBody() calls
			var responseBody = res.getBody();

// Replaced deprecated getStatusCodeValue() with getStatusCode().value()
			if (res.getStatusCode().value() == 200) {
				return exceptionHandlerUtil.createSuccessResponse(
						API_RESPONSE_EMAIL_SENT,
						responseBody != null ? responseBody.getResult() : null);

			} else if (res.getStatusCode().value() == 400) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.response.bad.request");

			} else if (res.getStatusCode().value() == 500) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.response.internal.server.error");
			}

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.email.sent.success",
					null);

		} catch (Exception e) {

			logger.error("{} - {} : Exception in sendEmailToAdmin: {}",
					CLASS, Utility.getMethodName(), e.getMessage());

			return exceptionHandlerUtil.handleException(e);
		}
	}

	private EmailDto getEmailDto(ApiResponses response, String softwareName, OrganizationDetails organizationDetails) {
		List<String> listOfEmail = (List<String>) response.getResult();

		EmailDto emailDto = new EmailDto();

		if (listOfEmail.size() >= noOfAdminEmail) {
			emailDto.setRecipients(
					listOfEmail.subList(0,
							Math.min(noOfAdminEmail, listOfEmail.size())));
		} else {
			emailDto.setRecipients(listOfEmail);
		}

		String emailBody = "Dear Admin,<br>Greetings!<br><br>SPOC have applied for license of the software \""
				+ softwareName.replace("_", " ")
				+ "\" for the organization \""
				+ organizationDetails.getOrganizationName()
				+ "\". Kindly do the needful.<br>";

		emailDto.setEmailBody(emailBody);
		emailDto.setSubject("Software License Request");
		return emailDto;
	}

	@Override
	public ApiResponses getAdminEmailList() {
		try {

			String adminEmailUrl = url + "admin-portal/api/UserApi/getuserslist";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

			ResponseEntity<String> response =
					restTemplate.exchange(adminEmailUrl, HttpMethod.GET,
							requestEntity, String.class);

			int status = response.getStatusCode().value();

			if (status != 200 && status != 201) {
				logger.error("{} - {} : Failed to fetch admin emails. Status: {}",
						CLASS, Utility.getMethodName(), status);

				return exceptionHandlerUtil.createErrorResponse(
						"api.error.admin.email.fetch.failed");
			}

			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(response.getBody());
			JsonNode resourceNode = jsonNode.get("resource");

			List<String> emailList = convertJsonNodeToList(resourceNode);

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.records.fetched",
					emailList);

		} catch (Exception e) {
			logger.error("{} - {} : Exception in getAdminEmailList: {}",
					CLASS, Utility.getMethodName(), e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	private static List<String> convertJsonNodeToList(JsonNode jsonNode) {
		List<String> stringList = new ArrayList<>();

		// Check if JsonNode is an array
		if (jsonNode.isArray()) {
			for (JsonNode element : jsonNode) {
				stringList.add(element.asText());
			}
		}

		return stringList;
	}

	@Override
	public ApiResponses addDeviceIdOfLicense(String applicationName,
											 List<String> deviceIDs) {
		try {

			if (applicationName == null || applicationName.isEmpty()) {
				return exceptionHandlerUtil.createErrorResponse(
						API_ERROR_APPLICATION_NAME_REQUIRED);
			}

			OrganizationDetailsForClient details =
					organizationDetailsForClientRepoIface
							.getOrganizationClientDetails(applicationName);

			if (details == null) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.organization.not.found");
			}

			deviceIDs.forEach(deviceId -> {
				LicenseDeviceList model = new LicenseDeviceList();
				model.setOrganizationName(details.getOrgName());
				model.setApplicationName(details.getApplicationName());
				model.setClientId(details.getClientId());
				model.setDeviceId(deviceId);
				model.setCreatedDate(AppUtil.getDate());
				model.setUpdatedDate(AppUtil.getDate());
				licenseDeviceListRepo.save(model);
			});

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.device.saved",
					null);

		} catch (Exception e) {
			logger.error("{} - {} : Exception in addDeviceIdOfLicense: {}",
					CLASS, Utility.getMethodName(), e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getDeviceID(String clientId) {
		try {

			if (!StringUtils.hasText(clientId)) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.client.id.required");
			}

			List<String> deviceList =
					licenseDeviceListRepo.getLicenseDeviceDetailsList(clientId);

			if (deviceList == null || deviceList.isEmpty()) {
				return exceptionHandlerUtil.createSuccessResponse(
						"api.response.no.records",
						new ArrayList<>());
			}

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.records.fetched",
					deviceList);

		} catch (Exception e) {
			logger.error("{} - {} : Exception in getDeviceID: {}",
					CLASS, Utility.getMethodName(), e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses getDeviceIdDetails(String applicationName) {
		try {
			if (applicationName == null || applicationName.isEmpty()) {
				return AppUtil.createApiResponse(false, "Application name should not be null or empty", null);
			} else {
				List<LicenseDeviceList> licenseDeviceList = licenseDeviceListRepo.getLicenseDeviceList(applicationName);
				if (!licenseDeviceList.isEmpty()) {
					return AppUtil.createApiResponse(true, "Data fetched successfully", licenseDeviceList);

				} else {
					return AppUtil.createApiResponse(true, "No record found", licenseDeviceList);
				}

			}
		} catch (Exception e) {
			logger.info("{}",e.getMessage());
			return AppUtil.createApiResponse(false, "Something went wrong. Please try after sometime", null);
		}
	}

	@Override
	public ApiResponses updateDeviceIdOfLicense(String applicationName,
												String oldDeviceId,
												String newDeviceId) {
		try {

			if (!StringUtils.hasText(applicationName)) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.application.name.required");
			}

			if (!StringUtils.hasText(oldDeviceId)) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.old.device.id.required");
			}

			if (!StringUtils.hasText(newDeviceId)) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.new.device.id.required");
			}

			LicenseDeviceList model =
					licenseDeviceListRepo.getLicenseDevice(oldDeviceId,
							applicationName);

			if (model == null) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.record.not.found");
			}

			model.setDeviceId(newDeviceId);
			model.setUpdatedDate(AppUtil.getDate());

			licenseDeviceListRepo.save(model);

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.device.updated",
					null);

		} catch (Exception e) {
			logger.error("{} - {} : Exception in updateDeviceIdOfLicense: {}",
					CLASS, Utility.getMethodName(), e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@Override
	public ApiResponses deleteRecordByDeviceID(String deviceId,
											   String applicationName) {
		try {

			if (!StringUtils.hasText(deviceId)) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.device.id.required");
			}

			if (!StringUtils.hasText(applicationName)) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.application.name.required");
			}

			int deleted =
					licenseDeviceListRepo.deleteRecordByDeviceId(
							deviceId, applicationName);

			if (deleted == 0) {
				return exceptionHandlerUtil.createErrorResponse(
						"api.error.record.not.found");
			}

			return exceptionHandlerUtil.createSuccessResponse(
					"api.response.device.deleted",
					null);

		} catch (Exception e) {
			logger.error("{} - {} : Exception in deleteRecordByDeviceID: {}",
					CLASS, Utility.getMethodName(), e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}

	private void populateCommonFields(
			SoftwareLicenses softwareLicenses,
			SoftwareLicensesDTO dto,
			OrganizationDetails organizationDetails) {

		softwareLicenses.setOuid(dto.getOuid());
		softwareLicenses.setLicenseType(dto.getLicenseType());
		softwareLicenses.setUpdatedDateTime(AppUtil.getDate());
		softwareLicenses.setApplicationName(dto.getApplicationType());
		softwareLicenses.setOrganizationName(
				organizationDetails.getOrganizationName());
	}

}
