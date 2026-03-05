package com.dtt.organization.repository;


import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.SubscriberView;

@Transactional
@Repository
public interface SubscriberViewRepository extends JpaRepository<SubscriberView, String>{

	@Query("SELECT s FROM SubscriberView s WHERE s.subscriberUid = ?1")
	SubscriberView getSubscriberDetailsBySuid(String suid);

	@Query("SELECT s FROM SubscriberView s WHERE s.emailId = ?1")
	SubscriberView findByUgpassMail(String mail);

	@Query("SELECT s FROM SubscriberView s WHERE s.idDocNumber = ?1")
	SubscriberView findByIdDocNumber(String id);

	@Query("SELECT s FROM SubscriberView s WHERE s.mobileNumber = ?1")
	SubscriberView findByMobile(String mobile);
}
