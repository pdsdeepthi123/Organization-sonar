package com.dtt.organization.restcontroller;
import com.dtt.organization.service.iface.LicensesIface;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.SoftwareLicensesDTO;

@RestController
public class LicensesController {
	
	public static final String CLASS = "LicensesController";
	Logger logger = LoggerFactory.getLogger(LicensesController.class);


	 private final LicensesIface licensesIface;


	public LicensesController(LicensesIface licensesIface) {
		this.licensesIface = licensesIface;
	}
	@PostMapping("/api/post/generatelicenses")
	public ApiResponses applyForGenerateLicenses(@RequestBody SoftwareLicensesDTO softwareLicensesDTO, @RequestHeader HttpHeaders httpHeaders) {
		logger.info("{} apply for generate license :: {}", CLASS, softwareLicensesDTO);
		return licensesIface.applyForGenerateLicenses(softwareLicensesDTO,httpHeaders);
	}

	@GetMapping("/api/download/license/{ouid}/{type}")
	public ApiResponses downloadLicence(@PathVariable("ouid") String ouid, @PathVariable("type") String type){
		logger.info("{} downloadLicence :: {} type :: {}", CLASS, ouid, type);
		return licensesIface.downloadLicense(ouid,type);
	}

	@GetMapping("/api/get-All/licenses/by/ouid/{Ouid}")
	public ApiResponses licensesIface(@PathVariable("Ouid") String ouid){

		logger.info("{} licensesIface ouid :: {}",
				CLASS, ouid);
		return licensesIface.getLicenseByOuid(ouid);
	}

	@GetMapping("/api/get-All/licenses/VG/by/ouid/{Ouid}")
	public ApiResponses licensesIfacevg(@PathVariable("Ouid") String ouid){

		logger.info("{} licensesIface  :: {}",
				CLASS, ouid);
		return licensesIface.getLicenseByOuidVG(ouid);
	}



	@GetMapping("/api/get/list/licenses")
	public ApiResponses getListForGenerateLicense(){
		return licensesIface.getListForGenerateLicense();
	}

	@PostMapping("/api/send/email")
	public ApiResponses sendEmailToAdmin(@RequestBody SoftwareLicensesDTO softwareLicensesDTO) {
		logger.info("{} sendEmailToAdmin :: {}", CLASS, softwareLicensesDTO);
		return licensesIface.sendEmailToAdmin(softwareLicensesDTO);
	}
	
	@GetMapping("/api/get/adminemail/list")
	public ApiResponses getAdminEmailList() {
		return  licensesIface.getAdminEmailList();
	}
	
	@PostMapping("/api/post/add/deviceid")
	public ApiResponses addDeviceIdOfLicense(@RequestParam String applicationName, @RequestParam List<String> deviceID) {
		logger.info("{} addDeviceIdOfLicense :: {} deviceID List :: {}",
				CLASS, applicationName, deviceID);

		return licensesIface.addDeviceIdOfLicense(applicationName,deviceID);
	}
	
	@PostMapping("/api/post/update/deviceid")
	public ApiResponses updateDeviceIdOfLicense(@RequestParam String applicationName, @RequestParam String olddeviceID, @RequestParam String newdeviceID) {

		logger.info("{} updateDeviceIdOfLicense :: {} oldDeviceID :: {} newDeviceID :: {}",
				CLASS, applicationName, olddeviceID, newdeviceID);
		return licensesIface.updateDeviceIdOfLicense(applicationName,olddeviceID,newdeviceID);
	}
	
	@GetMapping("/api/get/deviceid/{clientId}")
	public ApiResponses getDeviceID(@PathVariable String clientId) {
		logger.info("{} getDeviceID :: {}",
				CLASS, clientId);
		return licensesIface.getDeviceID(clientId);
	}
	
	@GetMapping("/api/get/deviceIdDetaisl/{applicationName}")
	public ApiResponses getDeviceIdDetails(@PathVariable String applicationName) {
		logger.info("{} getDeviceIdDetails :: {}",
				CLASS, applicationName);
		return licensesIface.getDeviceIdDetails(applicationName);
	}
	
	@DeleteMapping("/api/delete/license-device-record/{deviceId}/{applicationName}")
	public ApiResponses deleteSubscriberBySuid(@PathVariable String deviceId, @PathVariable String applicationName) {
		logger.info("{} deleteSubscriberBySuid :: deviceID :: {} application name :: {}",
				CLASS, deviceId, applicationName);
		return licensesIface.deleteRecordByDeviceID(deviceId,applicationName);
	}
}
