package com.dtt.organization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dtt.organization.model.OrganizationSignatureTemplates;

@Repository
public interface OrganizationSignatureTemplatesRepository extends JpaRepository<OrganizationSignatureTemplates, Integer>{


	List<OrganizationSignatureTemplates> findByOrganizationUid(String organizationUid);


	@Modifying
	@Transactional
	@Query("DELETE FROM OrganizationSignatureTemplates o WHERE o.organizationUid = :organizationUid")
	int deleteSignatureTemplateByOrgId(@Param("organizationUid") String organizationUid);

	@Query("SELECT o FROM OrganizationSignatureTemplates o WHERE o.organizationUid = :orgId ORDER BY o.type DESC")
	List<OrganizationSignatureTemplates> getOrgSignatureTemplates(@Param("orgId") String orgId);

	@Query("SELECT o FROM OrganizationSignatureTemplates o WHERE o.organizationUid = :orgId")
	List<OrganizationSignatureTemplates> getUserSignatureTemplatesDetails(@Param("orgId") String orgId);


	@Query("SELECT o FROM OrganizationSignatureTemplates o WHERE o.organizationUid = :orgId AND o.type = :type")
	OrganizationSignatureTemplates getOrgSignatureDetailsByType(@Param("orgId") String orgId, @Param("type") String type);


}
