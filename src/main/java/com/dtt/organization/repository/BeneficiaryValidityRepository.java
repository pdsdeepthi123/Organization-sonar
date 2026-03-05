package com.dtt.organization.repository;


import com.dtt.organization.model.BeneficiaryValidity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface BeneficiaryValidityRepository  extends JpaRepository<BeneficiaryValidity, Integer> {

    @Query("SELECT b FROM BeneficiaryValidity b WHERE b.beneficiaryId = ?1")
    List<BeneficiaryValidity> findAllBeneficiaryValiditybybenefeciaryId(int beneficiaryId);

    @Modifying
    @Transactional
    @Query("DELETE FROM BeneficiaryValidity b WHERE b.beneficiaryId = ?1")
    int deleteAllBeneficiaryValiditybybenefeciaryId(int beneficiaryId);


    @Modifying
    @Query("UPDATE BeneficiaryValidity b SET b.status = 'INACTIVE' WHERE b.beneficiaryId = ?1")
    int changeStatusByBeneficiaryId(int beneficiaryId);


}
