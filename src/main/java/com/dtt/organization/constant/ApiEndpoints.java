package com.dtt.organization.constant;

public class ApiEndpoints {

	private ApiEndpoints() {
	}

	public static final String ORGANIZATION_BASE = "/api";

	public static final String GET_COUNT = "/get/status";

	// GET endpoints
	public static final String GET_SERVICE_STATUS = "/get/service/status";
	public static final String GET_ORGANIZATION_DETAILS_BY_ID = "/get/organization/detailsById/{organizationUid}";
	public static final String GET_ORGANIZATION_DETAILS_BY_NAME = "/get/organization/detailsBy/organizationName";
	public static final String IS_ORGANIZATION_EXIST = "/get/organization-exist";
	public static final String GET_SUBSCRIBER_EMAIL_BY_SEARCH_TYPE = "/get/subscriber/email-By-searchType";

	public static final String GET_ORGANIZATION_LIST = "/get/org/list";
	public static final String GET_PREPARATORY_STATUS = "/get/prepertry/status";
	public static final String GET_BULKSIGNER_LIST = "/get/bulksignerlist";
	public static final String GET_AGENT_URL = "/get/agent/url";
	public static final String GET_ORGANIZATION = "/get/organiztion";
	public static final String GET_SIGNTORY_LIST = "/get/signtory/list/{organizationUid}";
	public static final String GET_SIGNTORY_LIST_BY_ORGID = "/get/signtorylist/{organizationUid}";
	public static final String GET_ALL_TEMPLATES = "/get/all/templates";
	public static final String GET_ALL_TEMPLATES_WITHOUT_IMAGES = "/get/all/template/names";
	public static final String GET_TEMPLATE_IMAGE = "/get/template/image";
	public static final String GET_ORG_LIST_BY_SUID = "/get/org/list/by-suid";
	public static final String GET_ORG_STATUS = "/get/orgStatus";
	public static final String GET_CERTFICATE_DETAILS = "/get/certificate/details";

	// POST endpoints
	public static final String REGISTER_ORGANIZATION = "/post/service/register/organization";
	public static final String UPDATE_ORGANIZATION = "/post/update-organization";
	public static final String ADD_AUTHORIZED_USER = "/post/add/authorizeduser";
	public static final String VERIFY_SIGNED_DOCUMENT = "/verify";
	public static final String GET_TEMPLATES_BY_DTO = "/get/templates";
	public static final String GET_USER_TEMPLATE_DETAILS = "/get/user/template/details";
	public static final String LINK_UGPASS_EMAIL = "/post/linkEmail";
	public static final String SEND_OTPS = "/post/otp";
	public static final String DEACTIVE_BUSINESS_USER = "/post/deactive";
	public static final String GET_ORG_LIST = "/get/orglist";
	public static final String GET_SUBSCRIBER_CERT_BY_CERT_SERIAL_NUMBER = "/get/subscriber/by/certificate/{certificateSerialNumber}";
	public static final String SEND_EMAIL_SPOC_ORG_NAME = "/post/sendEmail/{spocemail}/{orgName}";
	public static final String GET_TEMPLATES_ENCRYPT = "/get/templates/encrypt";
	
	// OrganizationController END
	
	// OrganizationCertificatesController START
	
	public static final String ISSUE_CERIFICATES_V1 = "/post/service/issue/certificates/{organizationUid}/{isPostPaid}";
	public static final String ISSUE_CERIFICATES_V2 = "/post/service/issue/certificates";
	public static final String ISSUE_WALLET_CERIFICATES = "/issue-wallet-certificate";



	public static final String DUPLICATE_TRANNSACTION_REFERENCE="/api/find/duplicate/reference/id";

	public static final String RENEW_WALLET_CERTIFICATE="/api/renew/wallet/certificate/by/ouid/{ouid}";

	public static final String REVOKE_CERIFICATES = "/post/service/certificate/revoke";
	public static final String GET_CERIFICATES_STATUS = "/get/servcie/certificate/status";
	public static final String GENERATE_SIGNATURE_ORG = "post/service/certificate/generate-signature-org";
	public static final String SEND_EMAIL_TO_SPOC_ESEAL_CERIFICATES_GENRATED = "/api/post/eseal-certificate-generated";
	public static final String GET_ALL_ORG_CERT = "/api/get/all/organizations/cert";

	public static final String GET_WALLET_CERT_BY_OUID = "/api/get/wallet/cert/by/ouid/{ouid}";

	public static final String GET_ALL_ORG_LIST="/get/organization/searchType";

	public static final String GET_ORG_LIST_FOR_SEARCH="/get/org/list/by/admin";

	public static  final String TOGGLE_MANAGE_BY_ADMIN="/update/manage/by/admin";

	public static  final String CHECK_TEMPLATES="/check/templates/admin";

	public static final String DELETE_ORG_BY_OUID="/delete/by/{ouid}";
}
