package com.dtt.organization.repository;

import com.dtt.organization.model.OrganizationEmailDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgEmailDomainRepository extends JpaRepository<OrganizationEmailDomain, Integer> {

    OrganizationEmailDomain findByOrganizationUid(String organizationUid);

}
