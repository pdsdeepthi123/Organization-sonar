package com.dtt.organization.config;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;


@Service
public class SentryClientExceptions {
	

	public void captureExceptions(Throwable e) {
		Sentry.captureException(e);
	}

	public void captureTags(String suid, String methodName, String controller)  {
		Sentry.setTag("subscriber_id", suid);
		Sentry.setTag("Method Name",methodName);
		Sentry.setTag("controller",controller);

	}
}
