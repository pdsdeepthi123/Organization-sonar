package com.dtt.organization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dtt.organization.model.OrgContactsEmail;

@Repository
@Transactional
public interface OrgContactsEmailRepository extends JpaRepository<OrgContactsEmail, Integer> {



	List<OrgContactsEmail> findByOrganizationUid(String organizationUid);

	OrgContactsEmail findByOrgContactsEmailId(int id);

	@Query("SELECT o FROM OrgContactsEmail o WHERE o.organizationUid = ?1 AND o.employeeEmail = ?2")
	List<OrgContactsEmail> findByOrganizationUidAndEmail(String ouid, String email);

	@Query("SELECT o.employeeEmail FROM OrgContactsEmail o WHERE o.organizationUid = ?1")
	List<String> findByOrganizationUidSubscriberList(String organizationUid);

	@Query("SELECT o FROM OrgContactsEmail o WHERE o.ugpassEmail = ?1")
	List<OrgContactsEmail> getPreprtyStatusByEmailId(String email);

	@Query("SELECT o FROM OrgContactsEmail o WHERE o.subscriberUid = ?1")
	List<OrgContactsEmail> getPreprtyStatusBySuid(String suid);

	@Query("SELECT o.employeeEmail FROM OrgContactsEmail o WHERE o.organizationUid = ?1 AND o.bulksign = true AND o.ugpassUserLinkApproved = true")
	List<String> getPreprtyStatusByOrgId(String orgId);


	@Query("SELECT o.employeeEmail FROM OrgContactsEmail o WHERE o.organizationUid = ?1 AND o.bulksign = true AND o.eSealSignatory = true AND o.ugpassUserLinkApproved = true")
	List<String> getBulkEsealListByOrgId(String orgId);


	@Modifying
	@Transactional
	@Query("DELETE FROM OrgContactsEmail o WHERE o.organizationUid = :organizationUid")
	int deleteOrganizationEmailById(@Param("organizationUid") String organizationUid);


	@Query("SELECT o FROM OrgContactsEmail o WHERE o.employeeEmail = ?1 AND o.organizationUid = ?2")
	OrgContactsEmail getOrganizationSigntoryDetails(String email, String orgId);

	@Query("SELECT o FROM OrgContactsEmail o WHERE o.subscriberUid = ?1 AND o.organizationUid = ?2")
	OrgContactsEmail getOrganizationSigntoryDetailsBySuidAndOuid(String suid, String orgId);

	@Query("SELECT o FROM OrgContactsEmail o WHERE o.ugpassEmail = ?1 OR o.mobileNumber = ?2 OR o.nationalIdNumber = ?3 OR o.passportNumber = ?4 OR o.subscriberUid = ?5")
	List<OrgContactsEmail> getSubscriberDetails(String email, String mobileNo, String nationalIdNo, String passportNo, String suid);

	@Query("SELECT o FROM OrgContactsEmail o WHERE o.organizationUid = ?1")
	List<OrgContactsEmail> getOrganizationSignatoryList(String ouid);

	@Query("SELECT o FROM OrgContactsEmail o WHERE (o.ugpassEmail = ?1 OR o.mobileNumber = ?2 OR o.nationalIdNumber = ?3 OR o.passportNumber = ?4 OR o.subscriberUid = ?5) AND o.organizationUid = ?6")
	List<OrgContactsEmail> getSubScriberDetailsByOuid(String email, String mobileNo, String nationalIdNo, String passportNo, String suid, String ouid);

	@Query("SELECT o FROM OrgContactsEmail o WHERE o.ugpassEmail = ?1 AND o.organizationUid = ?2")
	List<OrgContactsEmail> getSubscriberDetailsByOuidAndEmail(String email, String ouid);


	@Query("SELECT o FROM OrgContactsEmail o WHERE o.passportNumber = ?1 AND o.organizationUid = ?2")
	List<OrgContactsEmail> getSubscriberDetailsByOuidAndPassport(String passport, String ouid);

	@Query("SELECT o FROM OrgContactsEmail o WHERE o.mobileNumber = ?1 AND o.organizationUid = ?2")
	List<OrgContactsEmail> getSubscriberDetailsByOuidAndNUMBER(String mobileNumber, String ouid);


	@Query("SELECT o FROM OrgContactsEmail o WHERE o.nationalIdNumber = ?1 AND o.organizationUid = ?2")
	List<OrgContactsEmail> getSubscriberDetailsByOuidAndNin(String nin, String ouid);


	@Query("SELECT o FROM OrgContactsEmail o WHERE o.organizationUid = ?1 AND o.employeeEmail = ?2")
	OrgContactsEmail getOrganisationByUidAndEmail(String id, String email);




}
