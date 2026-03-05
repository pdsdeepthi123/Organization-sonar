package com.dtt.organization.util;

import java.util.Locale;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.dtt.organization.model.SubscriberDevice;
import com.dtt.organization.model.SubscriberDeviceHistory;
import com.dtt.organization.repository.SubscriberDeviceHistoryRepoIface;
import com.dtt.organization.repository.SubscriberDeviceRepoIface;




@Aspect
@Component
public class ExecutionTimeAspectPolicy {

	private final MessageSource messageSource;
	private final SubscriberDeviceRepoIface subscriberDeviceRepoIface;
	private final SubscriberDeviceHistoryRepoIface subscriberDeviceHistoryRepoIface;

	Logger logger= LoggerFactory.getLogger(ExecutionTimeAspectPolicy.class);

	public ExecutionTimeAspectPolicy(
			MessageSource messageSource,
			SubscriberDeviceRepoIface subscriberDeviceRepoIface,
			SubscriberDeviceHistoryRepoIface subscriberDeviceHistoryRepoIface) {

		this.messageSource = messageSource;
		this.subscriberDeviceRepoIface = subscriberDeviceRepoIface;
		this.subscriberDeviceHistoryRepoIface = subscriberDeviceHistoryRepoIface;
	}

	
	
	@Around("forgetOrgList() || forlinkEmail() || forsendOtp()")																																											// methods
	public Object controllerPolicy(ProceedingJoinPoint joinPoint) throws Throwable {
		return checkPolicy(joinPoint);
	}

	private Object checkPolicy(ProceedingJoinPoint joinPoint) throws Throwable {

		String deviceUid = getHeader(joinPoint, "deviceId");
		String appVersion = getHeader(joinPoint, "appVersion");

		if (isEmpty(appVersion)) {
			logger.info("appVersion is empty");
			return AppUtil.createApiResponse(false,
					messageSource.getMessage("api.error.please.update.your.app", null, Locale.ENGLISH),
					null);
		}

		SubscriberDevice subscriberDeviceDetails = subscriberDeviceRepoIface.findBydeviceUid(deviceUid);
		Optional<SubscriberDeviceHistory> history =
				Optional.ofNullable(subscriberDeviceHistoryRepoIface.findBydeviceUid(deviceUid));

		SubscriberDevice checkSubscriberDetails = null;

		boolean checkPolicy;

		if (history.isPresent()) {

			checkSubscriberDetails = subscriberDeviceRepoIface
					.getSubscriber(history.get().getSubscriberUid());

			SubscriberDevice activeDevice =
					subscriberDeviceRepoIface.findBydeviceUidAndStatus(deviceUid, "ACTIVE");

			checkPolicy = isDeviceAllowed(activeDevice);

		} else {

			checkPolicy = isDeviceAllowed(subscriberDeviceDetails);
		}

		if (checkPolicy) {
			return joinPoint.proceed();
		}

		return getPolicyFailureResponse(subscriberDeviceDetails, checkSubscriberDetails);
	}

	private String getHeader(ProceedingJoinPoint joinPoint, String headerName) {

		for (Object arg : joinPoint.getArgs()) {

			if (arg instanceof HttpServletRequest request) {
				return request.getHeader(headerName);
			}
		}

		return null;
	}

	private boolean isDeviceAllowed(SubscriberDevice device) {

		if (device == null) {
			logger.info("subscriberDeviceDetails is null");
			return false;
		}

		String status = device.getDeviceStatus();

		if ("DISABLED".equalsIgnoreCase(status)) {
			logger.info("Device is disabled");
			return false;
		}

		if ("ACTIVE".equalsIgnoreCase(status)) {
			logger.info("Device is active");
			return true;
		}

		logger.info("Unknown device status");
		return false;
	}
	private Object getPolicyFailureResponse(SubscriberDevice device,
											SubscriberDevice checkSubscriberDetails) {

		if (device == null && checkSubscriberDetails == null) {

			return AppUtil.createApiResponse(false,
					messageSource.getMessage("api.error.subscriber.not.found", null, Locale.ENGLISH),
					null);
		}

		return AppUtil.createApiResponse(false,
				messageSource.getMessage(
						"api.error.account.registered.on.new.device.services.disabled.on.this.device",
						null,
						Locale.ENGLISH),
				null);
	}
	private boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}

}
