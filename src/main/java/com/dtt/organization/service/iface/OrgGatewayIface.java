package com.dtt.organization.service.iface;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.*;

import java.util.List;

public interface OrgGatewayIface {
    ApiResponses getBusinessUsers(String orgId);

    ApiResponses getBusinessUserById(Integer id);

    ApiResponses updateBusinessUser(OrgUser orgUser);

    ApiResponses getEsealLogo(String orgUid);

    ApiResponses updateEsealLogo(UpdateEsealDto updateEsealDto);

    ApiResponses updateSignatureTemplatesById(SignatureTemplateUpdateDto signatureTemplateUpdateDto);

    ApiResponses getSignatureTemplateById(String orgUid);

    ApiResponses addMultipleBusinessUsers(List<OrgUser> orgUsersList);


    ApiResponses updateOrganisationEGSpoc(EnterpriseGatewayOrganisationUpdateDto gatewayOrganisationUpdateDto);
    ApiResponses updateOrganisationEGAgent(EnterpriseGatewayOrganisationUpdateDto gatewayOrganisationUpdateDto);
    
    ApiResponses getOrganizationCertificateDetailsByOrgUid(String orgUid);


    ApiResponses deleteBusinessUser(String orgId, String email);

    ApiResponses updateEmailDomain(UpdateEmailDomainDto updateEmailDomainDto);

    ApiResponses getEmailDomain(String ouid);
}
