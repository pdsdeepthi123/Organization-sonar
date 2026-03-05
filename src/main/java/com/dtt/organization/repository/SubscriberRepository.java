package com.dtt.organization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.Subscriber;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, String> {


	@Query("SELECT s.subscriberUid FROM Subscriber s WHERE s.emailId = ?1")
	String findByemailId(String emailId);

	/**
	 * Gets the subscriber list by email id.
	 *
	 * @param emailId the email id
	 * @return the subscriber list by email id
	 */

	@Query("SELECT s.emailId FROM Subscriber s WHERE s.emailId LIKE CONCAT('%', ?1, '%')")
	List<String> getSubscriberListByEmailId(String emailId);


	@Query("SELECT s FROM Subscriber s WHERE s.subscriberUid = ?1")
	Subscriber getSubscriberEmail(String suid);


	@Query("SELECT s FROM Subscriber s WHERE s.emailId = ?1")
	Subscriber getSubscriber(String emailId);

	/**
	 * Find bysubscriber uid.
	 *
	 * @param subscriberUniqueId the subscriber unique id
	 * @return the subscriber
	 */

	@Query("SELECT s FROM Subscriber s WHERE s.subscriberUid = ?1")
	Subscriber findBysubscriberUid(String subscriberUniqueId);

	@Query("SELECT s FROM Subscriber s " +
			"WHERE s.nationalId = ?1 " +
			"   OR s.idDocNumber = ?2 " +
			"   OR s.emailId = ?3 " +
			"   OR s.mobileNumber = ?4")
	List<Subscriber> findSubscriberDetails(String ninNumber,
										   String passportNumber,
										   String ugPassEmailId,
										   String mobileNumber);




}
