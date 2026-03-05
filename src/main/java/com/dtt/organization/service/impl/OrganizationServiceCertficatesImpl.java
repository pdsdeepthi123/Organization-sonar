package com.dtt.organization.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.dtt.organization.constant.Constant;
import com.dtt.organization.dto.*;
import com.dtt.organization.dto.RequestEntity;
import com.dtt.organization.response.entity.NativeCertResponse;
import com.dtt.organization.util.KafkaSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.dtt.organization.asserts.OrgnizationServiceAsserts;
import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.enums.CertificateStatus;
import com.dtt.organization.enums.CertificateType;
import com.dtt.organization.enums.LogMessageType;
import com.dtt.organization.enums.RevokeReason;
import com.dtt.organization.enums.ServiceName;
import com.dtt.organization.enums.TransactionType;
import com.dtt.organization.exception.ErrorCode;
import com.dtt.organization.exception.ExceptionHandlerUtil;
import com.dtt.organization.exception.OrgnizationServiceException;
import com.dtt.organization.model.OrgCertificateEmailCounter;
import com.dtt.organization.model.OrganizationCertificateLifeCycle;
import com.dtt.organization.model.OrganizationCertificates;
import com.dtt.organization.model.OrganizationDetails;
import com.dtt.organization.model.OrganizationStatusModel;
import com.dtt.organization.model.OrganizationWrappedKey;
import com.dtt.organization.model.SubscriberView;
import com.dtt.organization.model.WalletSignCertificate;
import com.dtt.organization.repository.OrgCertificateEmailCounterRepository;
import com.dtt.organization.repository.OrganizationCertificateLifeCycleReository;
import com.dtt.organization.repository.OrganizationCertificatesRepository;
import com.dtt.organization.repository.OrganizationDetailsRepository;
import com.dtt.organization.repository.OrganizationStatusRepository;
import com.dtt.organization.repository.OrganizationWrappedKeyRepository;
import com.dtt.organization.repository.SubscriberViewRepository;
import com.dtt.organization.repository.WalletSignCertRepo;
import com.dtt.organization.request.entity.GenerateSignature;
import com.dtt.organization.request.entity.LogModel;
import com.dtt.organization.request.entity.RevokeCertificateRequest;
import com.dtt.organization.response.entity.ServiceResponse;
import com.dtt.organization.service.iface.OrganizationCertificatesIface;
import com.dtt.organization.util.AppUtil;
import com.dtt.organization.util.NativeUtils;
import com.dtt.organization.util.Utility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;
import ug.daes.PKICoreServiceException;

@Service
@EnableScheduling
public class OrganizationServiceCertficatesImpl implements OrganizationCertificatesIface {

	private static final String CLASS = OrganizationServiceCertficatesImpl.class.getSimpleName();
	Logger logger = LoggerFactory.getLogger(OrganizationServiceCertficatesImpl.class);

	/**
	 * The Constant objectMapper.
	 */
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Value("${ra.service.pki}")
	private String pkiURL;

	@Value(value = "${url.debit}")
	private String debitBaseUrl;

	@Value(value = "${email.url}")
	private String emailBaseUrl;

	@Value(value = "${face.threshold}")
	private float faceThreshold;

	@Value("${native.wallet.url}")
	private String nativeWalletUrl;

	@Value("${native.cert.url}")
	private String nativeCertUrl;

	/**
	 * The rabbit MQ sender.
	 */
	private final KafkaSender rabbitMQSender;

	private final OrganizationDetailsRepository organizationDetailsRepository;

	private final OrganizationCertificatesRepository organizationCertificatesRepository;

	private final OrganizationCertificateLifeCycleReository organizationCertificateLifeCycleReository;

	private final OrganizationStatusRepository organizationStatusRepository;

	private final OrganizationWrappedKeyRepository organizationWrappedKeyRepository;

	private final SubscriberViewRepository subscriberViewRepository;

	private final OrgCertificateEmailCounterRepository orgCertificateEmailCounterRepository;

	private final WalletSignCertRepo walletSignCertRepo;
	private final MessageSource messageSource;

	private final ExceptionHandlerUtil exceptionHandlerUtil;
	private final RestTemplate restTemplate;
	@Value("${validation.transaction.reference.enabled}")
	private boolean transactionRefValidation;


	Locale locale = LocaleContextHolder.getLocale();

	public OrganizationServiceCertficatesImpl(KafkaSender rabbitMQSender,
											  OrganizationDetailsRepository organizationDetailsRepository,
											  OrganizationCertificatesRepository organizationCertificatesRepository,
											  OrganizationCertificateLifeCycleReository organizationCertificateLifeCycleReository,
											  OrganizationStatusRepository organizationStatusRepository,
											  OrganizationWrappedKeyRepository organizationWrappedKeyRepository,
											  SubscriberViewRepository subscriberViewRepository,
											  OrgCertificateEmailCounterRepository orgCertificateEmailCounterRepository,
											  WalletSignCertRepo walletSignCertRepo, MessageSource messageSource,
											  ExceptionHandlerUtil exceptionHandlerUtil, RestTemplate restTemplate) {
		super();
		this.rabbitMQSender = rabbitMQSender;
		this.organizationDetailsRepository = organizationDetailsRepository;
		this.organizationCertificatesRepository = organizationCertificatesRepository;
		this.organizationCertificateLifeCycleReository = organizationCertificateLifeCycleReository;
		this.organizationStatusRepository = organizationStatusRepository;
		this.organizationWrappedKeyRepository = organizationWrappedKeyRepository;
		this.subscriberViewRepository = subscriberViewRepository;
		this.orgCertificateEmailCounterRepository = orgCertificateEmailCounterRepository;
		this.walletSignCertRepo = walletSignCertRepo;
		this.messageSource = messageSource;
		this.exceptionHandlerUtil = exceptionHandlerUtil;
		this.restTemplate = restTemplate;
	}

	@Override
	public String issueOrganizationCertificates(String organizationUid, Boolean isPostPaid)  {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Starting certificate issuance for organizationUid: {}, isPostPaid: {}", CLASS, methodName,
				organizationUid, isPostPaid);
		try {
			if (!StringUtils.hasText(organizationUid)) {
				throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_ERROR_ORGUID_CANT_BE_NULL_OR_EMPTY);
			}
			if (isPostPaid == null) {
				throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_ERROR_ISPOSTPAID_CANT_BE_NULL_OR_EMPTY);
			}

			if (!Boolean.TRUE.equals(isPostPaid)) {
				throw exceptionHandlerUtil.orgnizationServiceException(
						Constant.API_EX_ORGANIZATION_NOT_POSTPAID
				);
			}

			OrganizationDetails organizationDetails = validateOrganizationDetails(organizationUid);
			validateOrganizationStatus(organizationUid);

			OrganizationCertificates existingCertificates = organizationCertificatesRepository
					.findByCertificateStatusAndOrganizationUniqueId(Constant.ACTIVE, organizationUid);
			if (existingCertificates != null) {
				throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_EX_CERTIFICATES_ALREADY_ISSUED);
			}

			String keyId = AppUtil.generatePKIKeyId();
			IssueCertificateDTO issueCertificateDTO = createIssueCertificateDTO(organizationUid, keyId,
					organizationDetails);

			var requestEntity = createRequestEntity(issueCertificateDTO, keyId);

			logger.info("{} - {}: Sending certificate issuance request to PKI service for organizationUid: {}", CLASS,
					methodName, organizationUid);
			ResponseEntity<String> httpResponse = restTemplate.postForEntity(pkiURL, requestEntity, String.class);
			ServiceResponse serviceResponse = objectMapper.readValue(httpResponse.getBody(), ServiceResponse.class);

			if (Constant.SUCCESS_PKI.equalsIgnoreCase(serviceResponse.getStatus())) {
				handlePostPaidTransaction(organizationUid);
				saveCertificates(organizationUid, keyId, serviceResponse);
				logCertificateActivity(LogMessageType.SUCCESS, organizationUid, null);
				logger.info("{} - {}: Certificates issued successfully for organizationUid: {}", CLASS, methodName,
						organizationUid);
				return messageSource.getMessage(Constant.API_RESPONSE_CERTIFICATES_ISSUED, null,locale);
			} else {
				logCertificateActivity(LogMessageType.FAILURE, organizationUid, null);
				logger.error("{} - {}: Certificate issuance failed for organizationUid: {}", CLASS, methodName,
						organizationUid);
				throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_EX_CERTIFICATES_ISSUE_FAILED);
			}
		} catch (Exception e) {
			logger.error("{} - {}: Unexpected exception occurred: {}", CLASS, methodName, e.getMessage(), e);
			return "";
		}
	}

	private OrganizationDetails validateOrganizationDetails(String organizationUid) throws OrgnizationServiceException {
		OrganizationDetails organizationDetails = organizationDetailsRepository.findByOrganizationUid(organizationUid);
		logger.info("Validating organization details for organizationUid: {}", organizationUid);
		OrgnizationServiceAsserts.notNullorEmpty(organizationDetails, ErrorCode.E_ORGANIZATION_DATA_NOT_FOUND.getMessage());
		return organizationDetails;
	}

	private void validateOrganizationStatus(String organizationUid) throws OrgnizationServiceException {
		OrganizationStatusModel organizationStatus = organizationStatusRepository.findByorganizationUid(organizationUid);
		logger.info("Validating organization status for organizationUid: {}", organizationUid);
		OrgnizationServiceAsserts.notNullorEmpty(organizationStatus, ErrorCode.E_ORGANIZATION_STATUS_DATA_NOT_FOUND.getMessage());
		if (!Constant.ACTIVE.equalsIgnoreCase(organizationStatus.getOrganizationStatus()) &&
				!"CERT_EXPIRED".equalsIgnoreCase(organizationStatus.getOrganizationStatus())) {
			throw new OrgnizationServiceException(ErrorCode.E_ORGANIZATION_STATUS_DATA_NOT_FOUND.getMessage());
		}
	}

	private IssueCertificateDTO createIssueCertificateDTO(String organizationUid, String keyId,
														  OrganizationDetails organizationDetails) {
		IssueCertificateDTO issueCertificateDTO = new IssueCertificateDTO();
		issueCertificateDTO.setSubscriberUniqueId(organizationUid);
		issueCertificateDTO.setKeyID(keyId);
		issueCertificateDTO.setCommonName(organizationDetails.getOrganizationName());
		issueCertificateDTO.setCountryName(Constant.UGA);
		return issueCertificateDTO;
	}

	private RequestEntity<PostRequest> createRequestEntity(IssueCertificateDTO issueCertificateDTO, String keyId) {
		PostRequest postDTO = new PostRequest();
		postDTO.setRequestBody(issueCertificateDTO.toString());
		postDTO.setHashdata(issueCertificateDTO.toString().hashCode());
		postDTO.setPkiKeyID(keyId);
		postDTO.setCertificateType(CertificateType.SIGN.toString());
		postDTO.setCallbackURI("null");

		RequestEntity<PostRequest> requestEntity = new RequestEntity<>();
		requestEntity.setPostRequest(postDTO);
		requestEntity.setTransactionType(Constant.ISSUE_CERTIFICATE_ORG);
		return requestEntity;
	}

	private void handlePostPaidTransaction(String organizationUid)  {
		PostpaidReqest postpaidRequest = new PostpaidReqest();
		postpaidRequest.setOrganizationId(organizationUid);
		postpaidRequest.setTransactionForOrganization(true);
		postpaidRequest.setServiceName(Constant.ESEAL_CERTIFICATE);
		ApiResponses postPaidRes = debitForPostpaid(postpaidRequest);
		if (!postPaidRes.isSuccess()) {
			logger.warn("Postpaid transaction failed for organizationUid: {}", organizationUid);
		} else {
			logger.info("Postpaid transaction successful for organizationUid: {}", organizationUid);
		}
	}

	private void saveCertificates(String organizationUid, String keyId, ServiceResponse serviceResponse
								  ) throws ParseException {
		OrganizationCertificates organizationCertificates = new OrganizationCertificates();
		organizationCertificates.setPkiKeyId(keyId);
		organizationCertificates.setCertificateData(serviceResponse.getCertificate());
		organizationCertificates.setCertificateStatus(Constant.ACTIVE);
		organizationCertificates.setCertificateSerialNumber(serviceResponse.getCertificateSerialNumber());
		organizationCertificates.setCertificateStartDate(AppUtil.getTimeStamp(serviceResponse.getIssueDate()));
		organizationCertificates.setCertificateEndDate(AppUtil.getTimeStamp(serviceResponse.getExpiryDate()));
		organizationCertificates.setOrganizationUid(organizationUid);
		organizationCertificates.setCreationDate(AppUtil.getTimeStamp());
		organizationCertificates.setWrappedKey(serviceResponse.getWrappedKey());
		organizationCertificates.setCertificateType(CertificateType.SIGN.toString());
		organizationCertificatesRepository.save(organizationCertificates);
		logger.info("Certificates saved successfully for organizationUid: {}", organizationUid);
	}

	private void logCertificateActivity(LogMessageType messageType, String organizationUid, String callStack) throws PKICoreServiceException, JsonProcessingException {
		LogModelDTO logModelDTO = new LogModelDTO();
		logModelDTO.setLogMessageType(messageType.toString());
		logModelDTO.setTimestamp(AppUtil.getDate());
		logModelDTO.setIdentifier(organizationUid);
		logModelDTO.setCallStack(callStack);
		rabbitMQSender.send(NativeUtils.getLogModel(logModelDTO));
		logger.info("Logged certificate activity for organizationUid: {}, type: {}", organizationUid, messageType);
	}

	public ApiResponses debitForPostpaid(PostpaidReqest postpaidReqest) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Initiating debit for postpaid request: {}", CLASS, methodName, postpaidReqest);
		try {
			String url = debitBaseUrl + "/api/add-credit-usage";
			logger.info("{} - {}: Debit usage API URL: {}", CLASS, methodName, url);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Object> reqEntity = new HttpEntity<>(postpaidReqest, headers);

			ResponseEntity<ApiResponses> response = restTemplate.exchange(url, HttpMethod.POST, reqEntity,
					ApiResponses.class);
			logger.info("{} - {}: Response received from debit usage API: {}", CLASS, methodName, response.getBody());
			return response.getBody();
		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			logger.error("{} - {}: Exception occurred HttpClientErrorException while processing postpaid debit: {}",
					CLASS, methodName, e.getMessage(), e);
			return exceptionHandlerUtil.handleHttpException(e);
		}
	}

	@Override
	public String revokeCertificate(RARequestDTO requestBody)  {

		try {
			String methodName = Utility.getMethodName();
			logger.info("{} - {}: Starting certificate revocation for request: {}", CLASS, methodName, requestBody);

			LogModelDTO logModelDTO = initializeLogModel(requestBody);
			validateOrganizationData(requestBody.getOrganizationUid());

			List<OrganizationCertificates> organizationCertificates = organizationCertificatesRepository
					.findByCertificateStatusAndOrganizationUid(CertificateStatus.ACTIVE.toString(),
							requestBody.getOrganizationUid());
			if (organizationCertificates.isEmpty()) {
				logger.info(ErrorCode.E_ORGANIZATION_CERTIFICATES_ARE_REVOKED.getMessage());
				return "";
			}

			boolean result = processCertificatesRevocation(organizationCertificates, requestBody, logModelDTO);

			if (result) {
				updateOrganizationStatus(requestBody, logModelDTO);
				logger.info("{} - {}: Certificates revoked successfully for organizationUid: {}", CLASS, methodName,
						requestBody.getOrganizationUid());
				return messageSource.getMessage("api.response.certificates.revoked", null,locale);
			} else {
				logger.error("{} - {}: Failed to revoke certificates for organizationUid: {}", CLASS, methodName,
						requestBody.getOrganizationUid());
				throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_EX_CERTIFICATES_REVOKE_FAILED);
			}
		}catch (Exception ex) {
			logger.error("{} - Exception occurred: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
			return " ";
		}
	}

	private LogModelDTO initializeLogModel(RARequestDTO requestBody)  {
		LogModelDTO logModelDTO = new LogModelDTO();
		logModelDTO.setStartTime(NativeUtils.getTimeStampString());
		logModelDTO.setIdentifier(requestBody.getOrganizationUid());
		return logModelDTO;
	}

	private void validateOrganizationData(String organizationUid) throws OrgnizationServiceException {
		logger.info("{} - Validating organization data for organizationUid: {}", CLASS, organizationUid);

		OrganizationDetails organizationDetails = organizationDetailsRepository.findByOrganizationUid(organizationUid);
		OrgnizationServiceAsserts.notNullorEmpty(organizationDetails, ErrorCode.E_ORGANIZATION_DATA_NOT_FOUND.getMessage());

		OrganizationStatusModel organizationStatus = organizationStatusRepository.findByorganizationUid(organizationUid);
		OrgnizationServiceAsserts.notNullorEmpty(organizationStatus,
				ErrorCode.E_ORGANIZATION_STATUS_DATA_NOT_FOUND.getMessage());
	}

	// I completely removed "throws Exception" from the signature
	private boolean processCertificatesRevocation(List<OrganizationCertificates> certificates, RARequestDTO requestBody, LogModelDTO logModelDTO) throws OrgnizationServiceException, ParseException, JsonProcessingException {

		String methodName = Utility.getMethodName();
		boolean result = false;

		for (OrganizationCertificates certificate : certificates) {
			String reason = mapRevocationReason(requestBody.getReasonId());
			certificate.setRevocationReason(reason);

			logger.info("{} - {}: Revoking certificate with serial number: {}", CLASS, methodName,
					certificate.getCertificateSerialNumber());

			logModelDTO.setCallStack(createRevokeRequestLog(requestBody, certificate));

			ResponseEntity<String> response = sendRevocationRequest(logModelDTO);
			handleRevocationResponse(response, certificate, logModelDTO);

			result = true;
		}
		return result;
	}

	private String mapRevocationReason(String reasonId) throws OrgnizationServiceException {
		switch (reasonId) {
			case "1":
				return RevokeReason.KEY_COMPROMISED.toString();
			case "-2":
				return RevokeReason.NO_REASON.toString();
			case "3":
				return RevokeReason.AFFILIATION_CHANGED.toString();
			case "4":
				return RevokeReason.SUPERSEDED.toString();
			case "5":
				return RevokeReason.CESSATION_OF_OPERATION.toString();
			case "6":
				return RevokeReason.CERTIFICATE_HOLD.toString();
			case "9":
				return RevokeReason.PRIVILEGE_WITHDRAWN.toString();
			default:
				throw new OrgnizationServiceException(ErrorCode.E_REVOKE_REASON_NOT_FOUND.getMessage());
		}
	}

	private String createRevokeRequestLog(RARequestDTO requestBody, OrganizationCertificates certificate) {
		RevokeCertificateRequest revokeRequest = new RevokeCertificateRequest();
		revokeRequest.setReasonId(requestBody.getReasonId());
		revokeRequest.setSerialNumber(certificate.getCertificateSerialNumber());
		return revokeRequest.toString();
	}

	private ResponseEntity<String> sendRevocationRequest(LogModelDTO logModelDTO) {
		String baseUrl = pkiURL;
		PostRequest postRequest = new PostRequest();
		postRequest.setRequestBody(logModelDTO.toString());
		postRequest.setHashdata(logModelDTO.toString().hashCode());

		// Fix applied here: changed String to PostRequest
		RequestEntity<PostRequest> requestEntity = new RequestEntity<>();
		requestEntity.setPostRequest(postRequest);
		requestEntity.setTransactionType(Constant.REVOKE_CERTIFICATE);

		logger.info("{} - Sending revocation request to PKI service: {}", CLASS, postRequest);
		return restTemplate.postForEntity(baseUrl, requestEntity, String.class);
	}

	private void handleRevocationResponse(ResponseEntity<String> response, OrganizationCertificates certificate,
										  LogModelDTO logModelDTO) throws JsonProcessingException, OrgnizationServiceException, ParseException {
		ServiceResponse serviceResponse = objectMapper.readValue(response.getBody(), ServiceResponse.class);

		if ("fail".equalsIgnoreCase(serviceResponse.getStatus())) {
			logModelDTO.setLogMessageType(LogMessageType.ERROR.toString());
			logModelDTO.setEndTime(NativeUtils.getTimeStampString());
			logger.error("{} - Revocation failed with error: {}", CLASS, serviceResponse.getErrorMessage());
			throw new OrgnizationServiceException(
					"Something went wrong. (Code : " + serviceResponse.getErrorMessage() + ")");
		}

		logger.info("{} - Revocation successful for certificate: {}", CLASS, certificate.getCertificateSerialNumber());
		certificate.setCertificateStatus(CertificateStatus.REVOKED.toString());
		certificate.setUpdatedDate(NativeUtils.getTimeStamp());
		organizationCertificatesRepository.save(certificate);

		OrganizationCertificateLifeCycle lifeCycle = new OrganizationCertificateLifeCycle();
		lifeCycle.setCertificateSerialNumber(certificate.getCertificateSerialNumber());
		lifeCycle.setCertificateStatus(CertificateStatus.REVOKED.toString());
		lifeCycle.setRevokedReason(certificate.getRevocationReason());
		lifeCycle.setOrganizationUid(certificate.getOrganizationUid());
		lifeCycle.setCertificateType(certificate.getCertificateType());
		lifeCycle.setCreationDate(NativeUtils.getTimeStamp());
		organizationCertificateLifeCycleReository.save(lifeCycle);
	}

	private void updateOrganizationStatus(RARequestDTO requestBody, LogModelDTO logModelDTO) throws ParseException {
		logger.info("{} - Updating organization status to REVOKED for organizationUid: {}", CLASS,
				requestBody.getOrganizationUid());

		OrganizationDetails organizationDetails = organizationDetailsRepository
				.findByOrganizationUid(requestBody.getOrganizationUid());
		organizationDetails.setStatus(Constant.REVOKED);
		organizationDetails.setUpdatedBy(AppUtil.getDate());
		organizationDetailsRepository.save(organizationDetails);

		OrganizationStatusModel organizationStatus = organizationStatusRepository
				.findByorganizationUid(requestBody.getOrganizationUid());
		organizationStatus.setOrganizationStatus(Constant.CERT_REVOKED);
		organizationStatus.setUpdatedDate(NativeUtils.getTimeStamp());
		organizationStatus.setOrganizationStatusDescription(requestBody.getDescription());
		organizationStatusRepository.save(organizationStatus);

		logModelDTO.setLogMessage(Constant.RESPONSE);
		logModelDTO.setLogMessageType(LogMessageType.SUCCESS.toString());
		logModelDTO.setEndTime(NativeUtils.getTimeStampString());
		logger.info("{} - Organization status updated to REVOKED successfully", CLASS);
	}

	@Override
	public String generateSignature(GenerateSignature generateSignature) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Starting generateSignature request: {}", CLASS, methodName, generateSignature);

		try {
			OrganizationDetails organizationDetails = validateOrganizationDetails(
					generateSignature.getSubscriberUniqueId());
			LogModelDTO logModelDTO = initializeLogModel(generateSignature, organizationDetails);

			OrganizationCertificates organizationCertificates = validateActiveCertificate(
					generateSignature.getSubscriberUniqueId());
			validateCertificateType(generateSignature, organizationCertificates);

			enrichGenerateSignatureData(generateSignature, organizationCertificates);

			logModelDTO.setCallStack(generateSignature.getGenerateSignatureData());
			String response = sendGenerateSignatureRequest(logModelDTO);

			logger.info("{} - {}: Signature generated successfully", CLASS, methodName);
			return response;
		} catch (Exception e) {
			logger.error("{} - {}: Unexpected exception occurred: {}", CLASS, methodName, e.getMessage(), e);
			return "";
		}
	}

	private OrganizationCertificates validateActiveCertificate(String organizationUid) throws OrgnizationServiceException {
		OrganizationCertificates organizationCertificates = organizationCertificatesRepository
				.findByCertificateStatusAndOrganizationUniqueId(CertificateStatus.ACTIVE.toString(), organizationUid);
		logger.info("{} - Validating active certificates for organizationUid: {}", CLASS, organizationUid);
		OrgnizationServiceAsserts.notNullorEmpty(organizationCertificates, ErrorCode.E_ACTIVE_CERTIFICATE_NOT_FOUND.getMessage());
		return organizationCertificates;
	}

	private void validateCertificateType(GenerateSignature generateSignature,
										 OrganizationCertificates organizationCertificates) throws OrgnizationServiceException {
		if (generateSignature.getCertType() != 0
				|| !organizationCertificates.getCertificateType().equals(CertificateType.SIGN.toString())) {
			logger.error("{} - Invalid certificate type for organizationUid: {}", CLASS,
					generateSignature.getSubscriberUniqueId());
			throw new OrgnizationServiceException(ErrorCode.E_CERTIFICATE_TYPE_NOT_FOUND.getMessage());
		}
	}

	private void enrichGenerateSignatureData(GenerateSignature generateSignature,
											 OrganizationCertificates organizationCertificates) throws OrgnizationServiceException {
		generateSignature.setKeyId(organizationCertificates.getPkiKeyId());

		OrganizationWrappedKey wrappedKey = organizationWrappedKeyRepository
				.findBycertificateSerialNumber(organizationCertificates.getCertificateSerialNumber());
		OrgnizationServiceAsserts.notNullorEmpty(wrappedKey, ErrorCode.E_WRAPPED_KEY_NOT_FOUND.getMessage());

		generateSignature.setWrappedKey(wrappedKey.getWrappedKey());
		generateSignature.setCertificate(organizationCertificates.getCertificateData());
		generateSignature.setSerialNumber(organizationCertificates.getCertificateSerialNumber());
	}

	private LogModelDTO initializeLogModel(GenerateSignature generateSignature, OrganizationDetails organizationDetails)
			 {
		LogModelDTO logModelDTO = new LogModelDTO();
		logModelDTO.setStartTime(NativeUtils.getTimeStampString());
		logModelDTO.setIdentifier(organizationDetails.getOrganizationUid());
		logModelDTO.setLogMessage(Constant.REQUEST);
		logModelDTO.setLogMessageType(LogMessageType.INFO.toString());
		logModelDTO.setTransactionType(TransactionType.BUSINESS.toString());
		logModelDTO.setServiceName(ServiceName.OTHER.toString());
		logModelDTO.setCorrelationID(generateSignature.getCorrelationId());
		logModelDTO.setTransactionID(NativeUtils.getUUId());
		logModelDTO.setSignatureType(Constant.DATA);
		logModelDTO.seteSealUsed(false);
		return logModelDTO;
	}

	private String sendGenerateSignatureRequest(LogModelDTO logModelDTO)
			throws OrgnizationServiceException, JsonProcessingException {
		String baseUrl = pkiURL;
		logger.info("{} - Sending generate signature request to PKI service: {}", CLASS, logModelDTO);

		PostRequest postRequest = new PostRequest();
		postRequest.setRequestBody(logModelDTO.toString());
		postRequest.setHashdata(logModelDTO.toString().hashCode());

		var requestEntity = new RequestEntity<>();
		requestEntity.setPostRequest(postRequest);
		requestEntity.setTransactionType(Constant.GENERATE_SIGNATURE);

		ResponseEntity<String> httpResponse = restTemplate.postForEntity(baseUrl, requestEntity, String.class);
		validatePKIResponse(httpResponse);

		ServiceResponse serviceResponse = objectMapper.readValue(httpResponse.getBody(), ServiceResponse.class);
		if (Constant.FAIL.equals(serviceResponse.getStatus())) {
			handlePKIServiceError(serviceResponse);
		}

		logger.info("{} - Signature generated successfully by PKI service.", CLASS);
		return serviceResponse.getSignature();
	}

	private void validatePKIResponse(ResponseEntity<String> httpResponse) throws OrgnizationServiceException {
		String body = httpResponse.getBody();
		if (Constant.TRANSACTION_TYPE_BOT_FOUND.equals(body)) {
			throw new OrgnizationServiceException(ErrorCode.E_TRANSACTION_TYPE_NOT_FOUND.getMessage());
		} else if (Constant.REQUEST_IS_NOT_VALID.equals(body)) {
			throw new OrgnizationServiceException(ErrorCode.E_REQUEST_DATA_IS_NOT_VALID.getMessage());
		}
	}

	private void handlePKIServiceError(ServiceResponse serviceResponse) {

		String errorMessage = serviceResponse.getErrorMessage();
		String errorCode = serviceResponse.getErrorCode();

		logger.error("{} - PKI service error: {}", CLASS, errorMessage);

		if (errorMessage == null || errorMessage.trim().isEmpty()) {
			// Automatically throws a 500 Internal Server Error to the frontend
			throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR,
					"PKI Service failed. (Code: " + errorCode + ")"
			);
		} else {
			throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR,
					errorMessage + " (Code: " + errorCode + ")"
			);
		}
	}
	@Override
	@Scheduled(cron = "0 0 0 * * ?")
	public String checkCertificateStatus() {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Starting checkCertificateStatus request", CLASS, methodName);

		try {
			LogModelDTO logModelDTO = initializeLogModelDTO();

			// For eSeal certificate expiry email code
			sendEmailToSpocCertificateExpired();

			List<OrganizationCertificates> expiredCertificates = organizationCertificatesRepository.findByCertificateStatusExpired();
			logger.info("{} - {}: Found {} expired certificates", CLASS, methodName, expiredCertificates.size());

			for (OrganizationCertificates certificate : expiredCertificates) {
				processExpiredCertificate(certificate, logModelDTO);
			}
			logger.info("{} - {}: checkCertificateStatus completed successfully", CLASS, methodName);
			return Constant.COMPLETED;

		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred during checkCertificateStatus: {}", CLASS, methodName,
					e.getMessage(), e);
			return "";
		}
	}

	private LogModelDTO initializeLogModelDTO()  {
		LogModelDTO logModelDTO = new LogModelDTO();
		logModelDTO.setStartTime(NativeUtils.getTimeStampString());
		logModelDTO.setLogMessage(Constant.REQUEST);
		logModelDTO.setLogMessageType(LogMessageType.INFO.toString());
		logModelDTO.setTransactionType(TransactionType.BUSINESS.toString());
		logModelDTO.setTransactionSubType(null);
		logModelDTO.setCorrelationID(NativeUtils.getUUId());
		logModelDTO.setTransactionID(NativeUtils.getUUId());
		logModelDTO.setSubTransactionID(null);
		logModelDTO.setGeoLocation(null);
		logModelDTO.setServiceProviderName(null);
		logModelDTO.setServiceProviderAppName(null);
		logModelDTO.setSignatureType(null);
		logModelDTO.seteSealUsed(false);
		return logModelDTO;
	}

	private void processExpiredCertificate(OrganizationCertificates certificate, LogModelDTO logModelDTO) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Processing expired certificate for organizationUid: {}", CLASS, methodName,
				certificate.getOrganizationUid());

		try {
			updateCertificateStatusToExpired(certificate);
			updateOrganizationDetailsStatus(certificate);
			updateOrganizationStatus(certificate);

			logModelDTO.setLogMessage(Constant.RESPONSE);
			logModelDTO.setLogMessageType(LogMessageType.SUCCESS.toString());
			logModelDTO.setEndTime(NativeUtils.getTimeStampString());


			logger.info("{} - {}: Certificate expired and status updated for organizationUid: {}", CLASS, methodName,
					certificate.getOrganizationUid());

		} catch (Exception e) {
			logger.error("{} - {}: Error processing expired certificate for organizationUid: {}", CLASS, methodName,
					certificate.getOrganizationUid(), e);
		}
	}

	private void updateCertificateStatusToExpired(OrganizationCertificates certificate) throws ParseException {
		certificate.setCertificateStatus(CertificateStatus.EXPIRED.toString());
		certificate.setUpdatedDate(NativeUtils.getTimeStamp());
		organizationCertificatesRepository.save(certificate);

		OrganizationCertificateLifeCycle lifeCycle = new OrganizationCertificateLifeCycle();
		lifeCycle.setCertificateSerialNumber(certificate.getCertificateSerialNumber());
		lifeCycle.setCertificateStatus(CertificateStatus.EXPIRED.toString());
		lifeCycle.setOrganizationUid(certificate.getOrganizationUid());
		lifeCycle.setCertificateType(certificate.getCertificateType());
		lifeCycle.setCreationDate(NativeUtils.getTimeStamp());
		organizationCertificateLifeCycleReository.save(lifeCycle);
	}

	private void updateOrganizationDetailsStatus(OrganizationCertificates certificate) {
		OrganizationDetails organizationDetails = organizationDetailsRepository
				.findByOrganizationUid(certificate.getOrganizationUid());
		organizationDetails.setStatus(CertificateStatus.EXPIRED.toString());
		organizationDetails.setUpdatedBy(AppUtil.getDate());
		organizationDetailsRepository.save(organizationDetails);
	}

	private void updateOrganizationStatus(OrganizationCertificates certificate) throws ParseException {
		OrganizationStatusModel organizationStatus = organizationStatusRepository
				.findByorganizationUid(certificate.getOrganizationUid());
		organizationStatus.setOrganizationStatus(Constant.CERT_EXPIRED);
		organizationStatus.setUpdatedDate(NativeUtils.getTimeStamp());
		organizationStatus.setOrganizationStatusDescription(Constant.CERTIFICATES_ARE_EXPIRED);
		organizationStatusRepository.save(organizationStatus);
	}

	@Override
	public String issueOrganizationCertificatesNew(OrganizationIssueCetificatesDto issueCetificatesDto) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Starting issueOrganizationCertificatesNew with request: {}", CLASS, methodName,
				issueCetificatesDto);

		try {
			validateInput(issueCetificatesDto);

			// Start transaction-reference id validation
			if (!isValidTransactionReference(issueCetificatesDto)) {
				throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_EX_INVALID_TRANSACTIONID);
			}

			// Check for duplicate transaction reference
			if (isDuplicateTransactionReference(issueCetificatesDto)) {
				throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_EX_DUPLICATE_TRANSACTIONID);
			}

			OrganizationDetails organizationDetails = organizationDetailsRepository
					.findByOrganizationUid(issueCetificatesDto.getOrganizationUid());
			OrgnizationServiceAsserts.notNullorEmpty(organizationDetails, ErrorCode.E_ORGANIZATION_DATA_NOT_FOUND.getMessage());

			OrganizationStatusModel organizationStatus = organizationStatusRepository.findByorganizationUid(issueCetificatesDto.getOrganizationUid());
			OrgnizationServiceAsserts.notNullorEmpty(organizationStatus,
					ErrorCode.E_ORGANIZATION_STATUS_DATA_NOT_FOUND.getMessage());

			validateOrganizationStatus(organizationStatus);

			if (isCertificateAlreadyIssued(issueCetificatesDto)) {
				throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_EX_CERTIFICATES_ALREADY_ISSUED);
			}

			String pkiKeyId = AppUtil.generatePKIKeyId();
			LogModelDTO logModelDTO = new LogModelDTO();
			logModelDTO.setStartTime(AppUtil.getTimeStamp().toString());
			logModelDTO.setEndTime(null);
			logModelDTO.setIdentifier(issueCetificatesDto.getOrganizationUid());
			logModelDTO.setServiceName(ServiceName.CERTIFICATE_GENERATED.toString());
			logModelDTO.setLogMessage("REQUEST");
			logModelDTO.setLogMessageType("INFO");
			logModelDTO.setTransactionType("BUSINESS");
			logModelDTO.setTransactionSubType(null);
			logModelDTO.setCorrelationID(AppUtil.getUUId());
			logModelDTO.setTransactionID(AppUtil.getUUId());
			logModelDTO.setSubTransactionID(null);
			logModelDTO.setGeoLocation(null);
			logModelDTO.setServiceProviderName(null);
			logModelDTO.setServiceProviderAppName(null);
			logModelDTO.setSignatureType(null);
			logModelDTO.seteSealUsed(true);
			CertReqDto issueCertificateDTO = prepareIssueCertificateDTO(
					organizationDetails, pkiKeyId);

			logModelDTO.setTimestamp(null);
			logModelDTO.setCallStack(issueCertificateDTO.toString());
			logModelDTO.setChecksum(null);


			// 1. Replaced string concatenation (+) with clean {} parameterized logging.
// (Removed .toString() because the logger handles it automatically!)
			logger.info("{} :: makePkiRequest for request :: CERT REQ: {}", CLASS, issueCertificateDTO);

			HttpEntity<Object> reqEntity = new HttpEntity<>(issueCertificateDTO, null);
			ResponseEntity<NativeCertResponse> httpResponse = restTemplate.exchange(nativeCertUrl, HttpMethod.POST, reqEntity, NativeCertResponse.class);

			NativeCertResponse responseBody = httpResponse.getBody();
			logger.info("{} :: makePkiRequest for response :: CERT RESPONSE isSuccess: {} | ERROR Message: {}",
					CLASS,
					responseBody != null ? responseBody.isSuccess() : "null",
					responseBody != null ? responseBody.getMessage() : "null"
			);
			String res = null;
			if (responseBody != null && responseBody.getResult() != null) {
				res = objectMapper.writeValueAsString(responseBody.getResult());
			}
			ServiceResponse response = objectMapper.readValue(res,ServiceResponse.class);
			 responseBody = httpResponse.getBody();
			if (responseBody != null && responseBody.isSuccess())
			 {
				handleSuccessfulCertificateIssuance(response, issueCetificatesDto, pkiKeyId);
				return messageSource.getMessage(Constant.API_RESPONSE_CERTIFICATES_ISSUED, null, locale);
			} else {

				return Constant.CERTIFICATES_ISSUANCE_FAILED;
			}
		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			logger.error("{} - {}: Error occurred in issueOrganizationCertificatesNew: {}", CLASS, methodName,
					e.getMessage(), e);
			return "";
		}
	}

	private void validateInput(OrganizationIssueCetificatesDto issueCetificatesDto) throws OrgnizationServiceException {
		if (issueCetificatesDto == null) {
			throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_ERROR_ORG_ISSUE_CERT_DTO_CANT_NULL);
		}
		if (!StringUtils.hasText(issueCetificatesDto.getOrganizationUid())) {
			throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_ERROR_ORG_ID_CANT_BE_NULL_EMPTY);
		}
		if (!StringUtils.hasText(issueCetificatesDto.getTransactionReferenceId())) {
			throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_ERROR_TRANSACTION_REFERRNCEID_ID_CANT_BE_NULL_EMPTY);
		}
	}

	private boolean isValidTransactionReference(OrganizationIssueCetificatesDto issueCetificatesDto) {
		List<String> transactionDetails = organizationDetailsRepository.getTransactionReferenceId(
				issueCetificatesDto.getOrganizationUid(), Constant.SUCCESS, issueCetificatesDto.getTransactionReferenceId());
		logger.info("rerwtkhjm::::::::::::{}",transactionDetails);
		return !transactionDetails.isEmpty();
	}

	private boolean isDuplicateTransactionReference(OrganizationIssueCetificatesDto issueCetificatesDto) {
		return organizationCertificatesRepository
				.findByTransactionReferenceId(issueCetificatesDto.getTransactionReferenceId()) != null;
	}

	private void validateOrganizationStatus(OrganizationStatusModel organizationStatus) throws OrgnizationServiceException {
		if (organizationStatus.getOrganizationStatus() == null
				|| !organizationStatus.getOrganizationStatus().equals(Constant.ACTIVE) && !"CERT_EXPIRED".equals(organizationStatus.getOrganizationStatus())) {
			throw new OrgnizationServiceException(ErrorCode.E_ORGANIZATION_STATUS_DATA_NOT_FOUND.getMessage());
		}
	}

	private boolean isCertificateAlreadyIssued(OrganizationIssueCetificatesDto issueCetificatesDto) {
		OrganizationCertificates existingCertificate = organizationCertificatesRepository
				.findByCertificateStatusAndOrganizationUniqueId(Constant.ACTIVE, issueCetificatesDto.getOrganizationUid());
		return existingCertificate != null;
	}

	private CertReqDto prepareIssueCertificateDTO(
														   OrganizationDetails organizationDetails, String pkiKeyId) {
		CertReqDto certReqDto = new CertReqDto();
		certReqDto.setCertProcedure("SHA256RSA");
		certReqDto.setCertSubject(organizationDetails.getOrganizationName());
		certReqDto.setCountry(Constant.UAE);
		certReqDto.setWalletIssuer(false);
		certReqDto.setIdentifier(pkiKeyId);
		return certReqDto;
	}



	private void handleSuccessfulCertificateIssuance(ServiceResponse serviceResponse,
													 OrganizationIssueCetificatesDto issueCetificatesDto, String pkiKeyId) throws ParseException {
		updateOrganizationCertificates(serviceResponse, issueCetificatesDto, pkiKeyId);
		updateOrganizationWrappedKey(serviceResponse);
		setStatusActive(issueCetificatesDto.getOrganizationUid());
		debitForPostpaid(issueCetificatesDto);
		sendEmailEsealCertificateGenerated(issueCetificatesDto.getOrganizationUid());
	}


	private void updateOrganizationWrappedKey(ServiceResponse serviceResponse) {
		OrganizationWrappedKey organizationWrappedKey = new OrganizationWrappedKey();
		organizationWrappedKey.setWrappedKey(serviceResponse.getWrappedKey());
		organizationWrappedKey.setCertificateSerialNumber(serviceResponse.getSerialNumber());
		organizationWrappedKeyRepository.save(organizationWrappedKey);

	}


	private void updateOrganizationCertificates(ServiceResponse serviceResponse,
												OrganizationIssueCetificatesDto issueCetificatesDto, String pkiKeyId) throws ParseException {
		OrganizationCertificates organizationCertificates = new OrganizationCertificates();
		organizationCertificates.setPkiKeyId(pkiKeyId);
		organizationCertificates.setCertificateData(serviceResponse.getCertificate());
		organizationCertificates.setCertificateStatus(Constant.ACTIVE);
		organizationCertificates.setCertificateSerialNumber(serviceResponse.getSerialNumber());
		organizationCertificates.setCertificateStartDate(AppUtil.getTimeStamp(serviceResponse.getIssueDate()));
		organizationCertificates.setCertificateEndDate(AppUtil.getTimeStamp(serviceResponse.getExpiryDate()));
		organizationCertificates.setOrganizationUid(issueCetificatesDto.getOrganizationUid());
		organizationCertificates.setCreationDate(AppUtil.getTimeStamp());
		organizationCertificates.setWrappedKey(serviceResponse.getWrappedKey());
		organizationCertificates.setCertificateType(CertificateType.SIGN.toString());
		organizationCertificates.setTransactionReferenceId(issueCetificatesDto.getTransactionReferenceId());
		organizationCertificatesRepository.save(organizationCertificates);
	}

	private void setStatusActive(String orgId) throws ParseException {
		OrganizationDetails organizationDetails = organizationDetailsRepository.findByOrganizationUid(orgId);
		organizationDetails.setStatus(Constant.ACTIVE);
		organizationDetails.setUpdatedOn(AppUtil.getDate());
		organizationDetailsRepository.save(organizationDetails);

		OrganizationStatusModel organizationStatus = organizationStatusRepository.findByorganizationUid(orgId);
		organizationStatus.setOrganizationStatus(Constant.ACTIVE);
		organizationStatus.setUpdatedDate(NativeUtils.getTimeStamp());
		organizationStatus.setOrganizationStatusDescription("Organization is active");
		organizationStatusRepository.save(organizationStatus);
	}


	private void debitForPostpaid(OrganizationIssueCetificatesDto issueCetificatesDto) {
		PostpaidReqest postpaidReqest = new PostpaidReqest();
		postpaidReqest.setOrganizationId(issueCetificatesDto.getOrganizationUid());
		postpaidReqest.setSubscriberSuid(null);
		postpaidReqest.setTransactionForOrganization(true);
		postpaidReqest.setServiceName(Constant.ESEAL_CERTIFICATE);
		ApiResponses postPaidRes = debitForPostpaid(postpaidReqest);

		if (postPaidRes.isSuccess()) {
			logger.info("{} - Debit for Postpaid successfully processed for organizationUid: {}", CLASS,
					issueCetificatesDto.getOrganizationUid());
		} else {
			logger.error("{} - Debit for Postpaid failed for organizationUid: {}", CLASS,
					issueCetificatesDto.getOrganizationUid());
		}
	}



	@Override
	public ApiResponses sendEmailEsealCertificateGenerated(String orgUid) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Sending Eseal certificate generated email for organization: {}", CLASS, methodName,
				orgUid);
		try {
			if (!StringUtils.hasText(orgUid)) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORG_ID_CANT_BE_NULL_EMPTY);
			}

			// Fetching necessary data
			OrganizationDetails organizationDetails = organizationDetailsRepository.findSpocEmailByOrgUid(orgUid);
			OrganizationCertificates organizationCertificates = organizationCertificatesRepository
					.findByCertificateStatusAndOrganizationUniqueId(Constant.ACTIVE, orgUid);

			if (organizationDetails == null || organizationCertificates == null) {
				logger.error("{} - {}: Organization or certificates not found for orgUid: {}", CLASS, methodName,
						orgUid);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORG_OR_CER_NOT_FOUND);
			}

			// Preparing the email request
			EmailReqDto emailReqDto = prepareEmailRequest(organizationDetails, organizationCertificates);

			// Sending the email
			ApiResponses res = sendEmailToSpoc(emailReqDto);
			if (res.isSuccess()) {
				logger.info("{} - {}: Email sent successfully for organization: {}", CLASS, methodName, orgUid);
				return AppUtil.createApiResponse(true, Constant.OK, null);
			} else {
				logger.error("{} - {}: Failed to send email. Response: {}", CLASS, methodName, res.getMessage());
				return exceptionHandlerUtil
						.createErrorResponse(Constant.API_ERROR_SOMETHING_WENT_WRONG_PLEASE_TRY_AFTER_SOMETIME);
			}
		} catch (HttpClientErrorException e) {
			logger.error("{}", e.getMessage());
			logger.error("{} - {}: Exception occurred HttpClientErrorException: {}", CLASS, methodName, e.getMessage(),
					e);
			return exceptionHandlerUtil.handleHttpException(e);
		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			logger.error("{} - {}: Exception occurred: {}", CLASS, methodName, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	// Method to prepare the email request
	private EmailReqDto prepareEmailRequest(OrganizationDetails organizationDetails,
											OrganizationCertificates organizationCertificates) {
		EmailReqDto emailReqDto = new EmailReqDto();

		// Setting up the SPoC email and certificate details
		emailReqDto.setEmailId(organizationDetails.getSpocUgpassEmail());

		SubscriberView subscriberView = subscriberViewRepository
				.findByUgpassMail(organizationDetails.getSpocUgpassEmail());
		EsealCertificateDto esealCertificateDto = new EsealCertificateDto();
		esealCertificateDto.setSpocFullName(subscriberView.getDisplayName());
		esealCertificateDto.setOrgName(organizationDetails.getOrganizationName());
		esealCertificateDto.setStartDate(organizationCertificates.getCertificateStartDate().toString());
		esealCertificateDto.setEndDate(organizationCertificates.getCertificateEndDate().toString());

		emailReqDto.setEsealCertificateDto(esealCertificateDto);
		emailReqDto.setEseal(true);

		return emailReqDto;
	}

	@Override
	public ApiResponses getAllOrganizationsAndCert(String orgId) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Fetching certificate details for organization: {}", CLASS, methodName, orgId);
		try {
			if (!StringUtils.hasText(orgId)) {
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORG_ID_CANT_BE_NULL_EMPTY);
			}
			OrganizationCertificates organizationCert = organizationCertificatesRepository
					.findByorganizationOuid(orgId);
			if (organizationCert == null) {
				logger.warn("{} - {}: Organization not found for orgId: {}", CLASS, methodName, orgId);
				return exceptionHandlerUtil.createErrorResponse(Constant.API_ERROR_ORGANIZATION_NOT_FOUND);
			}

			List<OrganizationCertificates> orgList = Collections.singletonList(organizationCert);

			OrgCertResponse orgCertResponse = new OrgCertResponse();
			orgCertResponse.setOrganizationCertificates(orgList);
			orgCertResponse.setQrFaceMatchThreshold(faceThreshold);

			logger.info("{} - {}: Successfully fetched organization certificate details for orgId: {}", CLASS,
					methodName, orgId);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_ORG_CERTIFICATES_LIST,
					orgCertResponse);

		} catch (Exception e) {
			logger.error("{} - {}: Exception occurred while fetching certificate details for orgId: {}: {}", CLASS,
					methodName, orgId, e.getMessage(), e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	public ApiResponses sendEmailToSpoc(EmailReqDto emailReqDto) {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Sending email to SPOC with request: {}", CLASS, methodName, emailReqDto);

		try {
			// Prepare the request headers and entity
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Object> requestEntity = new HttpEntity<>(emailReqDto, headers);

			// Send the request
			ResponseEntity<ApiResponses> res = restTemplate.exchange(emailBaseUrl, HttpMethod.POST, requestEntity,
					ApiResponses.class);
			logger.info("{} - {}: Email sent response: {}", CLASS, methodName, res);
			if (res.getStatusCode().value() == 200) {
				var responseBody = res.getBody();
				return AppUtil.createApiResponse(
						true,
						responseBody != null ? responseBody.getMessage() : "Success",
						res
				);
			} else {
				return exceptionHandlerUtil
						.createErrorResponse(Constant.API_ERROR_SOMETHING_WENT_WRONG_PLEASE_TRY_AFTER_SOMETIME);
			}
		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			logger.error("{} - {}: Error sending email to SPOC: {}", CLASS, methodName, e.getMessage(), e);
			return exceptionHandlerUtil.handleHttpException(e);
		}
	}

	public void sendEmailToSpocCertificateExpired() {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Starting sendEmailToSpocCertificateExpired process", CLASS, methodName);

		try {
			List<OrganizationCertificates> organizationCertificates = organizationCertificatesRepository
					.findByOrganizationCertificateStatusExpired();

			if (!organizationCertificates.isEmpty()) {
				for (OrganizationCertificates org : organizationCertificates) {
					handleCertificateExpiry(org);
				}
			}

			logger.info("{} - {}: Completed sendEmailToSpocCertificateExpired process", CLASS, methodName);
		} catch (Exception e) {
			logger.error("{} - {}: Error occurred in sendEmailToSpocCertificateExpired: {}", CLASS, methodName,
					e.getMessage(), e);
		}
	}

	private void handleCertificateExpiry(OrganizationCertificates org) {
		String methodName = Utility.getMethodName();
		try {
			long daysBetween = calculateDaysUntilExpiry(org.getCertificateEndDate());

			if (isValidExpiryNotificationTime(daysBetween)) {
				OrganizationDetails organizationDetails = organizationDetailsRepository
						.findSpocEmailByOrgUid(org.getOrganizationUid());

				SubscriberView subscriberView = subscriberViewRepository
						.findByUgpassMail(organizationDetails.getSpocUgpassEmail());

				EsealCertificateDto esealCertificateDto = createEsealCertificateDto(subscriberView, organizationDetails,
						org);

				EmailReqDto emailReqDto = createEmailRequest(organizationDetails, esealCertificateDto);

				// Send the email
				ApiResponses emailResponse = sendEmailToSpoc(emailReqDto);

				if (emailResponse.isSuccess()) {
					logger.info("{} - {}: Email sent successfully to Spoc for organizationUid: {}", CLASS, methodName,
							org.getOrganizationUid());
					updateEmailCounter(org, organizationDetails, daysBetween);
				} else {
					logger.error("{} - {}: Failed to send email to Spoc for organizationUid: {} - {}", CLASS,
							methodName, org.getOrganizationUid(), emailResponse.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error("{} - {}: Error processing certificate expiry for organizationUid: {} - {}", CLASS, methodName,
					org.getOrganizationUid(), e.getMessage(), e);
		}
	}

	private long calculateDaysUntilExpiry(Date expiryDate) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String expiryDateTime = sdf.format(expiryDate);
		LocalDateTime newExpiryDate = LocalDateTime.parse(expiryDateTime, dateTimeFormatter);

		String localDateTime = AppUtil.getDate();
		LocalDateTime currentDateTime = LocalDateTime.parse(localDateTime, dateTimeFormatter);

		return Duration.between(currentDateTime, newExpiryDate).toDays();
	}

	private boolean isValidExpiryNotificationTime(long daysBetween) {
		return daysBetween == 15 || daysBetween == 10 || daysBetween == 5 || daysBetween == 0;
	}

	private EsealCertificateDto createEsealCertificateDto(SubscriberView subscriberView,
														  OrganizationDetails organizationDetails, OrganizationCertificates org) {
		EsealCertificateDto esealCertificateDto = new EsealCertificateDto();
		esealCertificateDto.setSpocFullName(subscriberView.getDisplayName());
		esealCertificateDto.setOrgName(organizationDetails.getOrganizationName());
		esealCertificateDto.setEndDate(org.getCertificateEndDate().toString());
		return esealCertificateDto;
	}

	private EmailReqDto createEmailRequest(OrganizationDetails organizationDetails,
										   EsealCertificateDto esealCertificateDto) {
		EmailReqDto emailReqDto = new EmailReqDto();
		emailReqDto.setEmailId(organizationDetails.getSpocUgpassEmail());
		emailReqDto.setEsealCertificateDto(esealCertificateDto);
		emailReqDto.setEseal(true);
		return emailReqDto;
	}

	private void updateEmailCounter(OrganizationCertificates org, OrganizationDetails organizationDetails,
									long daysBetween) {
		OrgCertificateEmailCounter orgCertEmailCounter = orgCertificateEmailCounterRepository
				.findByOrganizationUid(org.getOrganizationUid());

		if (orgCertEmailCounter == null) {
			orgCertEmailCounter = new OrgCertificateEmailCounter();
			orgCertEmailCounter.setOrganizationUid(org.getOrganizationUid());
			orgCertEmailCounter.setSpocUgpassEmail(organizationDetails.getSpocUgpassEmail());
			orgCertEmailCounter.setOrganizationName(organizationDetails.getOrganizationName());
			updateCounterBasedOnDays(orgCertEmailCounter, daysBetween);
			orgCertificateEmailCounterRepository.save(orgCertEmailCounter);
		} else {
			updateCounterBasedOnDays(orgCertEmailCounter, daysBetween);
			orgCertificateEmailCounterRepository.save(orgCertEmailCounter);
		}
	}

	private void updateCounterBasedOnDays(OrgCertificateEmailCounter orgCertEmailCounter, long daysBetween) {
		if (daysBetween == 15) {
			orgCertEmailCounter.setCounter15("1");
		} else if (daysBetween == 10) {
			orgCertEmailCounter.setCounter10("1");
		} else if (daysBetween == 5) {
			orgCertEmailCounter.setCounter5("1");
		} else {
			orgCertEmailCounter.setCounter("1");
		}
	}

	@Override
	public String issueWalletOrganizationCertificates(OrganizationIssueCetificatesDto issueCetificatesDto){
		String methodName = Utility.getMethodName();
		try {
			logger.info("{} - {}: Request received: {}", CLASS, methodName, issueCetificatesDto);

			// Validate input parameters
			validateInputWalletCert(issueCetificatesDto);

			// Verify if valid transaction reference Id
			verifyTransactionReference(issueCetificatesDto);

			// Check for duplicate transaction reference ID
			checkDuplicateTransactionId(issueCetificatesDto);

			// Get organization details and check if certificate already exists
			OrganizationDetails organizationDetails = getOrganizationDetails(issueCetificatesDto);
			checkExistingCertificate(issueCetificatesDto);

			// Prepare log model for tracking
			LogModelDTO logModelDTO = prepareLogModelDTO(issueCetificatesDto);

			// Prepare certificate DTO for PKI request
			String keyId = AppUtil.generatePKIKeyId();
			CertReqDto issueCertificateDTO = prepareCertificateDTO(keyId,
					organizationDetails);

			// Make external PKI request for certificate generation
			ResponseEntity<NativeCertResponse> serviceResponse = makePkiRequest(issueCertificateDTO);

			// Process the response and issue the certificate
			return processPkiResponse(serviceResponse, issueCetificatesDto, logModelDTO, keyId);

		}catch (Exception e) {
			logger.error("{} - {}: Error occurred: {}", CLASS, methodName, e.getMessage(), e);
			return "";
		}
	}


	private void validateInputWalletCert(OrganizationIssueCetificatesDto issueCetificatesDto)
			throws OrgnizationServiceException {
		if (issueCetificatesDto.getOrganizationUid() == null || issueCetificatesDto.getOrganizationUid().isEmpty()) {
			throw new OrgnizationServiceException(Constant.ORGANIZATION_ID_CANT_BE_NULL_OR_EMPTY);
		}
		if (transactionRefValidation &&
				(issueCetificatesDto.getTransactionReferenceId() == null
						|| issueCetificatesDto.getTransactionReferenceId().isEmpty())) {

			throw new OrgnizationServiceException(
					Constant.TRANSACTION_ID_CANT_BE_NULL_OR_EMPTY);
		}
	}


	private void verifyTransactionReference(OrganizationIssueCetificatesDto issueCetificatesDto)
			throws OrgnizationServiceException {

		if (!transactionRefValidation) {
			logger.info("Skipping transaction reference validation based on config.");
			return;
		}
		List<String> transactionDetails = organizationDetailsRepository.getTransactionWalletReferenceId(
				issueCetificatesDto.getOrganizationUid(),
				Constant.SUCCESS_PKI,
				issueCetificatesDto.getTransactionReferenceId()
		);

		logger.info("Transaction details: {}", transactionDetails);

		if (transactionDetails.isEmpty()) {
			throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_EX_INVALID_TRANSACTIONID);
		}
	}

	public void checkDuplicateTransactionId(OrganizationIssueCetificatesDto issueCetificatesDto)
			throws OrgnizationServiceException {
		logger.info("1");
		if (!transactionRefValidation) {
			logger.info("Skipping duplicate Transaction Reference ID check based on config.");
			return;
		}

		List<String> transactionDetails = organizationDetailsRepository.getTransactionWalletReferenceId(
				issueCetificatesDto.getOrganizationUid(), Constant.SUCCESS, issueCetificatesDto.getTransactionReferenceId());
		logger.info("transcation::{}" , transactionDetails);
		if (!transactionDetails.isEmpty()) {
			throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_EX_DUPLICATE_TRANSACTIONID);
		}

	}


	private OrganizationDetails getOrganizationDetails(OrganizationIssueCetificatesDto issueCetificatesDto)
			throws OrgnizationServiceException {
		OrganizationDetails organizationDetails = organizationDetailsRepository
				.findByOrganizationUid(issueCetificatesDto.getOrganizationUid());
		if (organizationDetails == null) {
			throw new OrgnizationServiceException(Constant.ORGANIZATION_DETAILS_NOT_FOUND);
		}
		return organizationDetails;
	}

	private void checkExistingCertificate(OrganizationIssueCetificatesDto issueCetificatesDto)
			throws OrgnizationServiceException {
		WalletSignCertificate walletSignCertificate = walletSignCertRepo.findByOrganizationId(Constant.ACTIVE,
				issueCetificatesDto.getOrganizationUid());
		if (walletSignCertificate != null) {
			throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_EX_CERTIFICATES_ALREADY_ISSUED);
		}
	}

	private LogModelDTO prepareLogModelDTO(OrganizationIssueCetificatesDto issueCetificatesDto) throws ParseException {
		LogModelDTO logModelDTO = new LogModelDTO();
		logModelDTO.setStartTime(AppUtil.getTimeStamp().toString());
		logModelDTO.setEndTime(null);
		logModelDTO.setIdentifier(issueCetificatesDto.getOrganizationUid());
		logModelDTO.setServiceName(ServiceName.CERTIFICATE_GENERATED.toString());
		logModelDTO.setLogMessage(Constant.REQUEST);
		logModelDTO.setLogMessageType(Constant.INFO);
		logModelDTO.setTransactionType(Constant.BUSINESS);
		logModelDTO.setCorrelationID(AppUtil.getUUId());
		logModelDTO.setTransactionID(AppUtil.getUUId());
		logModelDTO.setServiceProviderAppName(null);
		logModelDTO.setSignatureType(null);
		logModelDTO.seteSealUsed(false);
		return logModelDTO;
	}

	private CertReqDto prepareCertificateDTO(String keyId,
												  OrganizationDetails organizationDetails) {
		CertReqDto certReqDto = new CertReqDto();
		certReqDto.setCertProcedure("EC256");
		certReqDto.setCertSubject(organizationDetails.getOrganizationName());
		certReqDto.setCountry(Constant.UAE);
		certReqDto.setWalletIssuer(true);
		certReqDto.setIdentifier(keyId);
		return certReqDto;
	}

	private ResponseEntity<NativeCertResponse> makePkiRequest(CertReqDto issueCertificateDTO) {

		// 1. Fixed placeholder syntax. (No need for .toString(), the logger handles objects automatically)
		logger.info("{} :: makePkiRequest :: REQ: {}", CLASS, issueCertificateDTO);

		HttpEntity<Object> reqEntity = new HttpEntity<>(issueCertificateDTO, null);
		ResponseEntity<NativeCertResponse> httpResponse = restTemplate.exchange(nativeWalletUrl, HttpMethod.POST, reqEntity, NativeCertResponse.class);

		// 2. Extract the body to guarantee null-safety for the linter
		NativeCertResponse responseBody = httpResponse.getBody();

		// 3. Replaced string concatenation (+) with clean {} placeholders
		logger.info("{} :: makePkiRequest :: RESPONSE isSuccess: {} | Message: {}",
				CLASS,
				responseBody != null ? responseBody.isSuccess() : "null",
				responseBody != null ? responseBody.getMessage() : "null"
		);

		return httpResponse;
	}

	private String processPkiResponse(ResponseEntity<NativeCertResponse> serviceResponse,
									  OrganizationIssueCetificatesDto issueCetificatesDto, LogModelDTO logModelDTO, String keyId) throws PKICoreServiceException, JsonProcessingException, OrgnizationServiceException, ParseException {

		// 2. Extract the body ONCE
		var responseBody = serviceResponse.getBody();

		// 3. Check for success ONCE
		if (responseBody != null && responseBody.isSuccess()) {

			// 4. Process transaction and save certificate ONCE (No more duplicates!)
			processPostPaidTransaction(issueCetificatesDto);
			saveWalletCertificate(responseBody, issueCetificatesDto, keyId);

			// Log success to RabbitMQ
			logModelDTO.setLogMessageType(LogMessageType.SUCCESS.toString());
			logModelDTO.setEndTime(NativeUtils.getTimeStampString());
			LogModel logModel = NativeUtils.getLogModel(logModelDTO);
			rabbitMQSender.send(logModel);

			return messageSource.getMessage(Constant.API_RESPONSE_CERTIFICATES_ISSUED, null, locale);

		} else {

			// Log failure to RabbitMQ
			logModelDTO.setLogMessageType(LogMessageType.FAILURE.toString());
			logModelDTO.setEndTime(NativeUtils.getTimeStampString());
			LogModel logModel = NativeUtils.getLogModel(logModelDTO);
			rabbitMQSender.send(logModel);

			// 5. Throwing your specific custom exception here is exactly what the linter wants!
			throw exceptionHandlerUtil.orgnizationServiceException(Constant.API_EX_CERTIFICATES_ISSUE_FAILED);
		}
	}

	private void processPostPaidTransaction(OrganizationIssueCetificatesDto issueCetificatesDto) {
		PostpaidReqest postpaidReqest = new PostpaidReqest();
		postpaidReqest.setOrganizationId(issueCetificatesDto.getOrganizationUid());
		postpaidReqest.setTransactionForOrganization(true);
		postpaidReqest.setServiceName(Constant.WALLET_CERTIFICATE);
		ApiResponses postPaidRes = debitForPostpaid(postpaidReqest);
		if (postPaidRes.isSuccess()) {
			logger.info("{} Wallet Sign Certificate PostPaid Debits Added Successfully: ", postPaidRes.getResult());
		}
	}

	private WalletSignCertificate saveWalletCertificate(NativeCertResponse serviceResponse,
														OrganizationIssueCetificatesDto issueCetificatesDto, String keyId) throws ParseException, JsonProcessingException {
		String res = objectMapper.writeValueAsString(serviceResponse.getResult());
		ServiceResponse response = objectMapper.readValue(res,ServiceResponse.class);
		WalletSignCertificate walletSignCertificate = new WalletSignCertificate();
		walletSignCertificate.setPkiKeyId(keyId);
		walletSignCertificate.setCertificateData(response.getCertificate());
		walletSignCertificate.setCertificateStatus(Constant.ACTIVE);
		walletSignCertificate.setCertificateSerialNumber(response.getSerialNumber());
		walletSignCertificate.setCertificateStartDate(AppUtil.getTimeStamp(response.getIssueDate()));
		walletSignCertificate.setCertificateEndDate(AppUtil.getTimeStamp(response.getExpiryDate()));
		walletSignCertificate.setOrganizationUid(issueCetificatesDto.getOrganizationUid());
		walletSignCertificate.setCreationDate(AppUtil.getTimeStamp());
		walletSignCertificate.setUpdatedDate(AppUtil.getTimeStamp());
		walletSignCertificate.setWrappedKey(response.getWrappedKey());
		walletSignCertificate.setCertificateType(CertificateType.SIGN.toString());
		walletSignCertificate.setTransactionReferenceId(issueCetificatesDto.getTransactionReferenceId());
		walletSignCertRepo.save(walletSignCertificate);
		return walletSignCertificate;
	}


	@Override
	public ApiResponses getWalletCertByOuid(String ouid) {
		try {
			WalletSignCertificate walletSignCertificate = walletSignCertRepo.findTopByOrganizationUidOrderByUpdatedDateDesc(ouid);
			logger.info("response:{}" , walletSignCertificate);
			if (walletSignCertificate == null) {
				return exceptionHandlerUtil.createSuccessResponse(Constant.API_ERROR_ORGANIZATION_DETAILS_NOT_FOUND,
						walletSignCertificate);
			}
			if (walletSignCertificate.getOrganizationUid() == null) {
				return exceptionHandlerUtil.createSuccessResponse(Constant.ORGANIZATION_DETAILS_NOT_FOUND,
						walletSignCertificate);
			}
			if (!walletSignCertificate.getCertificateStatus().equals("ACTIVE")) {
				return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_WALLET_CERTIFICATE,
						walletSignCertificate);
			}

			WalletSignCertificate walletSignCertificate1 = walletSignCertRepo.findByCertificateStatusExpiredVG(ouid);
			if (walletSignCertificate1 == null) {
				return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_WALLET_CERTIFICATE,
						walletSignCertificate);

			}
			walletSignCertificate1.setCertificateStatus("EXPIRED");
			walletSignCertRepo.save(walletSignCertificate1);
			return exceptionHandlerUtil.createSuccessResponse(Constant.API_RESPONSE_WALLET_CERTIFICATE,
					walletSignCertificate1);
		} catch (Exception e) {
			logger.error("{}", e.getMessage());
			return exceptionHandlerUtil.handleException(e);
		}
	}





}
