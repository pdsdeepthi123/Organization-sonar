package com.dtt.organization.service.iface;

import java.util.List;

import org.springframework.http.HttpHeaders;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.SoftwareLicensesDTO;

public interface LicensesIface {
	
	ApiResponses applyForGenerateLicenses(SoftwareLicensesDTO softwareLicensesDTO, HttpHeaders httpHeaders);

	ApiResponses downloadLicense(String ouid, String type);

	ApiResponses getLicenseByOuid(String ouid);

	ApiResponses getLicenseByOuidVG(String ouid);
	
	ApiResponses getListForGenerateLicense();
	
	ApiResponses sendEmailToAdmin(SoftwareLicensesDTO softwareLicensesDTO);
	
	ApiResponses getAdminEmailList();
	
	ApiResponses addDeviceIdOfLicense(String applicationName, List<String> deviceID);
	
	ApiResponses updateDeviceIdOfLicense(String applicationName, String olddeviceID, String newdeviceID);
	
	ApiResponses getDeviceID(String clientId);
	
	ApiResponses getDeviceIdDetails(String applicationName);
	
	ApiResponses deleteRecordByDeviceID(String deviceId, String applicationName);
}
