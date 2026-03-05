package com.dtt.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dtt.organization.model.OrgCertificateEmailCounter;

public interface OrgCertificateEmailCounterRepository extends JpaRepository<OrgCertificateEmailCounter, String>{

	@Query("SELECT o FROM OrgCertificateEmailCounter o WHERE o.organizationUid = ?1")
	OrgCertificateEmailCounter findByOrganizationUid(String orgUid);

}
