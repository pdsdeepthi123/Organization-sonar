package com.dtt.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.SoftwareLicenseApprovalRequests;


@Repository
public interface SoftwareLicenseApprovalRequestsRepo extends JpaRepository<SoftwareLicenseApprovalRequests,Integer>{

    @Query("SELECT s FROM SoftwareLicenseApprovalRequests s")
    SoftwareLicenseApprovalRequests getSoftwareLicenseApprovalRequests();


    @Query("SELECT s FROM SoftwareLicenseApprovalRequests s WHERE s.ouid = ?1 AND s.licenseType = ?2 AND s.appid = ?3")
    SoftwareLicenseApprovalRequests getSoftwareDetails(String orguid, String type, String softwareName);

}
