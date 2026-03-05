package com.dtt.organization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.dtt.organization.model.OrganizationService;

public interface OrganizationServiceRepository extends JpaRepository<OrganizationService, Integer> {

	List<OrganizationService> findByOrganizationUid(String organizationUid);

	@Modifying
	@Transactional(rollbackFor = Exception.class)
	@Query("DELETE FROM OrganizationService o WHERE o.organizationUid = :organizationUid")
	int deleteOrganizationServiceEmailById(String organizationUid);
}
