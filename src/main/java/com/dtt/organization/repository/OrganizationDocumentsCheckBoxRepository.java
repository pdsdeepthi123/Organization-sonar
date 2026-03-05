package com.dtt.organization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dtt.organization.model.OrganizationDocumentsCheckBox;

@Repository
public interface OrganizationDocumentsCheckBoxRepository extends JpaRepository<OrganizationDocumentsCheckBox, Integer> {
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("DELETE FROM OrganizationDocumentsCheckBox o WHERE o.organizationUid = :organizationUid")
    int deleteOrganizationDocumentsCheckBoxById(@Param("organizationUid") String organizationUid);

    List<OrganizationDocumentsCheckBox> findByOrganizationUid(String organizationUid);
}
