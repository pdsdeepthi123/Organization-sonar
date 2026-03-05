package com.dtt.organization.restcontroller;


import com.dtt.organization.config.SentryClientExceptions;
import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.util.AppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class SentryController {


	Logger logger= LoggerFactory.getLogger(SentryController.class);
	private final SentryClientExceptions sentryClientExceptions;

	public SentryController(SentryClientExceptions sentryClientExceptions) {
		this.sentryClientExceptions = sentryClientExceptions;
	}

	@GetMapping("api/get/service/sentry")
	public ApiResponses getServiceStatusSentry() {
		String suid = null;
		try {

			suid = generateSubscriberUniqueId();
			sentryClientExceptions.captureTags(suid, "SentryController","getServiceStatusSentry" );

			return AppUtil.createApiResponse(false, "Service is down", null);

		} catch (Exception e) {
			logger.error("Error occurred while processing request", e);

			sentryClientExceptions.captureExceptions(e);

			return AppUtil.createApiResponse(false, "down", null);
		}

	}

	public String generateSubscriberUniqueId() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}



}
