package com.dtt.organization.repository;


import com.dtt.organization.model.OrganizationPrivileges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrganisationPrivilegesRepository extends JpaRepository<OrganizationPrivileges,Integer> {

    @Query("SELECT o.privilege FROM OrganizationPrivileges o WHERE o.organizationId = ?1 AND o.status = 'APPROVED'")
    List<String> fetchPrivilegesByOrganisation(String orgId);


    @Query("SELECT o FROM OrganizationPrivileges o WHERE o.id = ?1")
    OrganizationPrivileges fetchById(int id);


    @Query("SELECT o FROM OrganizationPrivileges o WHERE o.organizationId = ?1 AND o.privilege = ?2 AND o.status <> 'REJECTED'")
    OrganizationPrivileges fetchByPrivilege(String orgId, String privilege);


    @Query("SELECT o FROM OrganizationPrivileges o ORDER BY o.createdOn DESC")
    List<OrganizationPrivileges> getAll();


    @Modifying
    @Transactional
    @Query("UPDATE OrganizationPrivileges o SET o.status = 'SUSPENDED' WHERE o.organizationId = ?1 AND o.status = 'APPROVED'")
    int updatePrivileges(String orgId);

}
