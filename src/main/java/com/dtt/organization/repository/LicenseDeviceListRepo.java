package com.dtt.organization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dtt.organization.model.LicenseDeviceList;

@Repository
public interface LicenseDeviceListRepo extends JpaRepository<LicenseDeviceList, Integer> {

	@Query("SELECT l FROM LicenseDeviceList l WHERE l.clientId = ?1")
	LicenseDeviceList getLicenseDeviceDetails(String clientId);

	@Query("SELECT l FROM LicenseDeviceList l WHERE l.deviceId = ?1 AND l.applicationName = ?2")
	LicenseDeviceList getLicenseDevice(String oldDeviceId, String applicationName);


	@Query("SELECT l FROM LicenseDeviceList l WHERE l.applicationName = ?1")
	List<LicenseDeviceList> getLicenseDeviceList(String applicationName);


	@Modifying
	@Transactional
	@Query("DELETE FROM LicenseDeviceList l WHERE l.deviceId = ?1 AND l.applicationName = ?2")
	int deleteRecordByDeviceId(String suid, String applicationName);


	@Query("SELECT l.deviceId FROM LicenseDeviceList l WHERE l.clientId = ?1")
	List<String> getLicenseDeviceDetailsList(String clientId);

}
