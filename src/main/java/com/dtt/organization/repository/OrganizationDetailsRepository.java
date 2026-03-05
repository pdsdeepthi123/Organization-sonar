package com.dtt.organization.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.OrganizationDetails;



@Repository
public interface OrganizationDetailsRepository extends JpaRepository<OrganizationDetails, Integer> {


	@Query("SELECT o FROM OrganizationDetails o WHERE o.organizationUid = ?1")
	OrganizationDetails findByOrganizationUid(String organizationUid);


	@Query("SELECT o FROM OrganizationDetails o WHERE o.organizationUid = ?1")
	List<OrganizationDetails> findOrganizationByUid(String organizationUid);

	@Query("SELECT o.spocUgpassEmail FROM OrganizationDetails o WHERE o.organizationUid = ?1")
	List<String> findSpocEmail(String organizationUid);


	@Query("SELECT o.organizationName FROM OrganizationDetails o WHERE LOWER(o.organizationName) LIKE LOWER(CONCAT('%', ?1, '%'))")
	List<String> getOrganizationByName(String organizationName);


	@Query("SELECT o FROM OrganizationDetails o WHERE o.organizationName = ?1")
	List<OrganizationDetails> getOrgnizationDetails(String organizationName);

	@Query("SELECT COUNT(o) FROM OrganizationDetails o WHERE o.organizationName = ?1")
	int isOrgnizationExist(String organizationName);

	@Query("SELECT o FROM OrganizationDetails o WHERE o.organizationUid IN " +
			"(SELECT e.organizationUid FROM OrgContactsEmail e WHERE e.employeeEmail = ?1)")
	List<OrganizationDetails> getOrganizationListAndUid(String email);



	@Query("SELECT o.organizationName, o.organizationUid FROM OrganizationDetails o")
	List<String> getOrganizationList();



	@Query("""
SELECT CONCAT(o.organizationName, ' - ', o.organizationUid, ' - ', o.manageByAdmin)
FROM OrganizationDetails o
WHERE o.organizationName ILIKE CONCAT('%', :search, '%')
   OR o.organizationUid ILIKE CONCAT('%', :search, '%')
""")
	List<String> getOrganizationListForSerach();

	@Query("SELECT e.employeeEmail FROM OrgContactsEmail e WHERE e.organizationUid = ?1 AND e.eSealSignatory = true")
	List<String> getOrganizationSignatoryList(String organizationUid);

	@Query("SELECT o FROM OrganizationDetails o WHERE o.spocUgpassEmail = ?1")
	List<OrganizationDetails> getOrgDeatils(String spocEmail);

	@Query("SELECT s.transactionReferenceId FROM SubscriberPaymentHistory s " +
			"WHERE s.organizationId = :organizationId " +
			"AND UPPER(s.paymentStatus) = UPPER(:paymentStatus) " +
			"AND s.paymentCategory = 'ESEAL_CERT_FEE_COLLECTION' " +
			"AND s.transactionReferenceId = :transactionReferenceId")
	List<String> getTransactionReferenceId(@Param("organizationId") String organizationId,
										   @Param("paymentStatus") String paymentStatus,
										   @Param("transactionReferenceId") String transactionReferenceId);





	@Query("""
    SELECT s.transactionReferenceId 
    FROM SubscriberPaymentHistory s
    WHERE s.organizationId = :organizationId
      AND UPPER(s.paymentStatus) = UPPER(:status)
      AND s.paymentCategory = 'WALLET_CERT_FEE_COLLECTION'
      AND s.transactionReferenceId = :transactionReferenceId
""")
	List<String> getTransactionWalletReferenceId(
			@Param("organizationId") String organizationId,
			@Param("status") String status,
			@Param("transactionReferenceId") String transactionReferenceId);


	@Query("SELECT o FROM OrganizationDetails o WHERE o.organizationUid = ?1")
	OrganizationDetails findSpocEmailByOrgUid(String orgUid);

	@Query("SELECT o FROM OrganizationDetails o WHERE o.organizationName = ?1")
	OrganizationDetails getOrgnizationDetailsByName(String organizationName);

	// Total count
	@Query("SELECT COUNT(o) FROM OrganizationDetails o")
	long getTotalOrganizations();

	// Active count
	@Query("SELECT COUNT(o) FROM OrganizationDetails o WHERE o.status = 'ACTIVE'")
	long getActiveOrganizations();

	// Inactive count
	@Query("SELECT COUNT(o) FROM OrganizationDetails o WHERE o.status = 'INACTIVE'")
	long getInactiveOrganizations();

	// Registered count
	@Query("SELECT COUNT(o) FROM OrganizationDetails o WHERE o.status = 'REGISTERED'")
	long getRegisteredOrganizations();


}

