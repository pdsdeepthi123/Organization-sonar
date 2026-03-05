package com.dtt.organization.repository;

import com.dtt.organization.model.TrustedStakeholder;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface TrustedStakeholdersRepository extends JpaRepository<TrustedStakeholder, Integer> {

	TrustedStakeholder findByReferenceId(String referenceId);

	@Query("SELECT t FROM TrustedStakeholder t")
	List<TrustedStakeholder> getAllTrustedStakeHolder();

	@Query("SELECT t FROM TrustedStakeholder t WHERE t.referredBy = ?1")
	List<TrustedStakeholder> getAllTrustedStakeHolderByOrgId(String organizationId);

	@Query("SELECT t FROM TrustedStakeholder t WHERE t.stakeholderType = ?1")
	List<TrustedStakeholder> getAllTrustedStakeHolderByStakeHolderType(String stakeHolderType);

	@Query("SELECT t FROM TrustedStakeholder t WHERE t.spocUgpassEmail = ?1")
	List<TrustedStakeholder> getStakeHolderList(String spocEmail);

}