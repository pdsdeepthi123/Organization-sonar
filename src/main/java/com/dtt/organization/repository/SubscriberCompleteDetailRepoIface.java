package com.dtt.organization.repository;

import java.util.Date;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.SubscriberCompleteDetail;


@Transactional
@Repository
public interface SubscriberCompleteDetailRepoIface extends JpaRepository<SubscriberCompleteDetail, String>{

	@Query("SELECT s FROM SubscriberCompleteDetail s WHERE s.createdDate >= :startDate AND s.createdDate <= :endDate ORDER BY s.createdDate DESC")
	List<SubscriberCompleteDetail> getSubscriberReports(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Query("SELECT COUNT(s.deviceStatus) FROM SubscriberCompleteDetail s WHERE (s.deviceStatus = :status AND s.emailId = :email) OR (s.deviceStatus = :status AND s.mobileNumber = :mobileNo)")
	int getActiveDeviceCountStatusByEmailAndMobileNo(@Param("status") String status, @Param("email") String email, @Param("mobileNo") String mobileNo);

	@Query("SELECT s FROM SubscriberCompleteDetail s WHERE s.emailId = :email")
	SubscriberCompleteDetail getSubscriberSelfieThumbnailByEmail(@Param("email") String email);

	@Query("SELECT s FROM SubscriberCompleteDetail s WHERE s.subscriberUid = :suid")
	SubscriberCompleteDetail getSubscriberSelfieThumbnailBySuid(@Param("suid") String suid);
}
