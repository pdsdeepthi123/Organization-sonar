package com.dtt.organization.repository;

import com.dtt.organization.model.OrganizationStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface OrganizationStatusRepository extends JpaRepository<OrganizationStatusModel, Integer> {
	
	
	OrganizationStatusModel findByorganizationUid(String organizationUniqueId);

}
