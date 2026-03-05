package com.dtt.organization.repository;

import com.dtt.organization.model.SoftwareLicenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoftwareLicensesRepository extends JpaRepository<SoftwareLicenses,Integer> {



    @Query("SELECT s FROM SoftwareLicenses s WHERE s.ouid = ?1 AND s.licenseType = ?2")
    SoftwareLicenses findByOuidAndLicenseType(String ouid, String licenseType);


    @Query("SELECT s FROM SoftwareLicenses s WHERE s.ouid = ?1")
    SoftwareLicenses findByOrgUid(String ouid);



    @Query(value = "SELECT * FROM software_licenses WHERE ouid = ?1 " +
            "ORDER BY (CASE WHEN licence_status = 'APPLIED' THEN 0 ELSE 1 END), " +
            "updated_date_time DESC",
            nativeQuery = true)
    List<SoftwareLicenses> findByOuid(String ouid);


    @Query(value = "SELECT * FROM software_licenses WHERE ouid = ?1 " +
            "ORDER BY (CASE WHEN licence_status = 'APPLIED' THEN 0 ELSE 1 END), " +
            "updated_date_time DESC",
            nativeQuery = true)
    SoftwareLicenses findByOuidVG(String ouid);

    @Query("SELECT s FROM SoftwareLicenses s ORDER BY s.updatedDateTime DESC")
    List<SoftwareLicenses> getListForGenerateLicenses();




}
