package com.dtt.organization.repository;

import com.dtt.organization.model.BeneficiariedPrivilegeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BeneficiariedPrivilegeServiceRepo extends JpaRepository<BeneficiariedPrivilegeService, Integer> {

    @Query("SELECT p FROM BeneficiariedPrivilegeService p WHERE p.status = 'ACTIVE'")
    List<BeneficiariedPrivilegeService> findPrivilegeByStatus();

    @Query("SELECT p FROM BeneficiariedPrivilegeService p WHERE p.privilegeId = ?1")
    BeneficiariedPrivilegeService findPrivilegeById(int id);
}
