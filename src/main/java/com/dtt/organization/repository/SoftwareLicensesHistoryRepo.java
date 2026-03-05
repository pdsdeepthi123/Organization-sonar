package com.dtt.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.SoftwareLicensesHistory;

@Repository
public interface SoftwareLicensesHistoryRepo extends JpaRepository<SoftwareLicensesHistory,Integer>{

}
