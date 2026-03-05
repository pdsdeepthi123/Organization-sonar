package com.dtt.organization.repository;

import com.dtt.organization.model.OrganizationDetailsForClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationDetailsForClientRepoIface extends JpaRepository<OrganizationDetailsForClient,String> {

    @Query("SELECT o FROM OrganizationDetailsForClient o WHERE o.clientId = ?1")
    OrganizationDetailsForClient getOrganizationDetails(String clientId);

    @Query("SELECT o FROM OrganizationDetailsForClient o WHERE o.applicationName = ?1 AND o.organizationUid = ?2")
    OrganizationDetailsForClient getClientId(String appName, String ouid);

    @Query("SELECT o FROM OrganizationDetailsForClient o WHERE o.applicationName = ?1")
    OrganizationDetailsForClient getOrganizationClientDetails(String appName);

}