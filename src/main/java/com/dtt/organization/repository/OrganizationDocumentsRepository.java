package com.dtt.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.OrganizationDocuments;
@Repository
public interface OrganizationDocumentsRepository extends JpaRepository<OrganizationDocuments, Integer> {

}
