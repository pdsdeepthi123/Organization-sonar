package com.dtt.organization.service.iface;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.OrganizationIssueCetificatesDto;
import com.dtt.organization.dto.RARequestDTO;
import com.dtt.organization.request.entity.GenerateSignature;


public interface OrganizationCertificatesIface {

	public String issueOrganizationCertificates(String organizationId, Boolean isPostPaid) ;
	
	public String revokeCertificate(RARequestDTO requestBody) ;
	
	public String checkCertificateStatus() ;
	

	public String generateSignature(GenerateSignature generateSignature);

	public String issueOrganizationCertificatesNew(OrganizationIssueCetificatesDto cetificatesDto) ;

	public String issueWalletOrganizationCertificates(OrganizationIssueCetificatesDto cetificatesDto) ;

	ApiResponses sendEmailEsealCertificateGenerated(String orgId);

	ApiResponses getAllOrganizationsAndCert(String orgId);

	ApiResponses getWalletCertByOuid(String ouid);



}
