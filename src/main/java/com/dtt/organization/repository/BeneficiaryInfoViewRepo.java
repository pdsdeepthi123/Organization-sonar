package com.dtt.organization.repository;

import com.dtt.organization.model.BeneficiaryInfoView;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BeneficiaryInfoViewRepo extends JpaRepository<BeneficiaryInfoView,Integer> {


    @Query("SELECT b FROM BeneficiaryInfoView b " +
            "WHERE b.beneficiaryStatus = 'ACTIVE' " +
            "AND b.validityStatus = 'ACTIVE' " +
            "AND (b.beneficiaryUgPassEmail = ?1 " +
            "OR b.beneficiaryPassport = ?2 " +
            "OR b.beneficiaryNin = ?3 " +
            "OR b.beneficiaryMobileNumber = ?4 " +
            "OR b.beneficiaryDigitalId = ?5)")
    List<BeneficiaryInfoView> findByEmailOrPassportOrNinOrMobileNumberOrBeneficiaryDigitalId(
            String email, String passport, String nin, String mobileNumber, String beneficiaryDigitalId);

    @Query("SELECT b FROM BeneficiaryInfoView b WHERE b.sponsorExternalId = ?1")
    List<BeneficiaryInfoView> getVendorsByVendorId(String vendorId);



}
