package com.dtt.organization.repository;


import com.dtt.organization.model.Benificiaries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface BeneficiariesRepo extends JpaRepository<Benificiaries, Integer> {





    @Query("SELECT b FROM Benificiaries b WHERE b.sponsorDigitalId = ?1 AND b.beneficiaryType = 'INDIVIDUAL' ORDER BY b.id DESC")
    List<Benificiaries> getAllBeneficiariesBySponsor(String sponsorId);


    @Query("SELECT b FROM Benificiaries b WHERE b.sponsorDigitalId = ?1 ORDER BY b.id DESC")
    List<Benificiaries> getAllBeneficiariesBySponsorByDigitalId(String sponsorId);


    @Query("SELECT b FROM Benificiaries b WHERE b.sponsorDigitalId = ?1 AND (b.beneficiaryMobileNumber = ?2 OR b.beneficiaryPassport = ?3 OR b.beneficiaryOfficeEmail = ?4)")
    List<Benificiaries> findDuplicateBeneficiariesByPassport(String sponsorId, String number, String passport, String officeEmail);




    @Query("SELECT b FROM Benificiaries b WHERE b.sponsorDigitalId = ?1 AND b.beneficiaryUgPassEmail = ?2")
    Benificiaries findDuplicateBeneficiariesByUgPassEmail(String sponsorId, String ugPassEmail);


    @Query("SELECT b FROM Benificiaries b WHERE b.sponsorDigitalId = ?1 AND b.beneficiaryMobileNumber = ?2")
    Benificiaries findDuplicateBeneficiariesByMobileNumber(String sponsorId, String mobileNumber);


    @Query("SELECT b FROM Benificiaries b WHERE b.sponsorDigitalId = ?1 AND b.beneficiaryPassport = ?2")
    Benificiaries findDuplicateBeneficiariesByPassport(String sponsorId, String passport);


    @Query("SELECT b FROM Benificiaries b WHERE b.sponsorDigitalId = ?1 AND b.beneficiaryNin = ?2")
    Benificiaries findDuplicateBeneficiariesByNIN(String sponsorId, String nin);


    @Query("SELECT b FROM Benificiaries b WHERE b.sponsorDigitalId = ?1 AND b.beneficiaryOfficeEmail = ?2")
    Benificiaries findDuplicateBeneficiariesByOfficeEmail(String sponsorId, String officeEmail);


    @Query("SELECT b FROM Benificiaries b WHERE b.sponsorDigitalId = ?1 AND b.beneficiaryDigitalId = ?2")
    Benificiaries findDuplicateBeneficiariesByBeneficiaryDigitalId(String sponsorId, String beneficiaryDigitalId);


    @Modifying
    @Transactional
    @Query("UPDATE Benificiaries b SET b.status = 'INACTIVE' WHERE b.id = ?1")
    void changeStatusById(int id);


    @Query("SELECT e FROM Benificiaries e " +
            "JOIN BeneficiaryValidity v ON v.beneficiaryId = e.id " +
            "WHERE (e.beneficiaryUgPassEmail = :email " +
            "       OR e.beneficiaryPassport = :passport " +
            "       OR e.beneficiaryNin = :nin " +
            "       OR e.beneficiaryMobileNumber = :mobileNumber " +
            "       OR e.beneficiaryDigitalId = :beneficiaryDigitalId) " +
            "AND e.status = 'ACTIVE' " +
            "AND ((v.validityApplicable = TRUE AND CURRENT_DATE BETWEEN " +
            "       FUNCTION('TO_DATE', v.validFrom, 'YYYY-MM-DD') AND FUNCTION('TO_DATE', v.validUpTo, 'YYYY-MM-DD')) " +
            "       OR v.validityApplicable = FALSE) " +
            "AND v.privilegeServiceId = 3 " +
            "AND v.status = 'ACTIVE'")
    List<Benificiaries> findByEmailOrPassportOrNinOrMobileNumberorBeneficiaryDigitalId(
            @Param("email") String email,
            @Param("passport") String passport,
            @Param("nin") String nin,
            @Param("mobileNumber") String mobileNumber,
            @Param("beneficiaryDigitalId") String beneficiaryDigitalId);


    @Query("SELECT b FROM Benificiaries b " +
            "WHERE b.status = 'ACTIVE' " +
            "AND (b.beneficiaryUgPassEmail = :email " +
            "     OR b.beneficiaryPassport = :passport " +
            "     OR b.beneficiaryNin = :nin " +
            "     OR b.beneficiaryMobileNumber = :mobileNumber " +
            "     OR b.beneficiaryDigitalId = :beneficiaryDigitalId)")
    List<Benificiaries> findAllSponsor(@Param("email") String email,
                                       @Param("passport") String passport,
                                       @Param("nin") String nin,
                                       @Param("mobileNumber") String mobileNumber,
                                       @Param("beneficiaryDigitalId") String beneficiaryDigitalId);

    @Modifying
    @Transactional
    @Query("UPDATE Benificiaries b SET b.status = 'ACTIVE' WHERE b.id = ?1")
    int changeStatusForSSP(int id);




}