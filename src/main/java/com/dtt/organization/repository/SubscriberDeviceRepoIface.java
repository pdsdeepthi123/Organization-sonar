package com.dtt.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.SubscriberDevice;


@Repository
public interface SubscriberDeviceRepoIface extends JpaRepository<SubscriberDevice, Integer>{


	SubscriberDevice findTop1ByDeviceUidOrderByUpdatedDateDesc(String deviceUid);
	default SubscriberDevice findBydeviceUid(String deviceUid) {
		return findTop1ByDeviceUidOrderByUpdatedDateDesc(deviceUid);
	}

	@Query("SELECT s FROM SubscriberDevice s WHERE s.deviceUid = :deviceUid AND s.deviceStatus = :status")
	SubscriberDevice findBydeviceUidAndStatus(@Param("deviceUid") String deviceUid, @Param("status") String status);

	@Query("SELECT s FROM SubscriberDevice s WHERE s.subscriberUid = :subscriberUid AND s.updatedDate = (SELECT MAX(s2.updatedDate) FROM SubscriberDevice s2 WHERE s2.subscriberUid = :subscriberUid)")
	SubscriberDevice getSubscriber(@Param("subscriberUid") String subscriberUid);
}
