package com.dtt.organization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.SubscriberDeviceHistory;


@Repository
public interface SubscriberDeviceHistoryRepoIface extends JpaRepository<SubscriberDeviceHistory, Integer>{

    SubscriberDeviceHistory findTop1ByDeviceUidOrderByUpdatedDateDesc(String deviceUid);
    default SubscriberDeviceHistory findBydeviceUid(String deviceUid) {
        return findTop1ByDeviceUidOrderByUpdatedDateDesc(deviceUid);
    }

    @Query("SELECT s FROM SubscriberDeviceHistory s WHERE s.deviceUid = :deviceUid AND s.subscriberUid = :subscriberUid")
    List<SubscriberDeviceHistory> findByDeviceUidAndSubscriberUid(@Param("deviceUid") String deviceUid, @Param("subscriberUid") String subscriberUid);

    SubscriberDeviceHistory findTop1ByDeviceUidAndSubscriberUidOrderByUpdatedDateDesc(String deviceUid, String subscriberUid);
    default SubscriberDeviceHistory findByDeviceUidAndSubUid(String deviceUid, String subscriberUid) {
        return findTop1ByDeviceUidAndSubscriberUidOrderByUpdatedDateDesc(deviceUid, subscriberUid);
    }




}