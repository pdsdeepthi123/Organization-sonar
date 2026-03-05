package com.dtt.organization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.sentry.Sentry;
import io.sentry.SentryOptions;

@Configuration
public class SentryConfig {

	@Bean
	public Sentry.OptionsConfiguration<SentryOptions> sentryOptions() {
		return options -> {			
			options.setDsn("https://0a799ae8507b42fa86093a8aa288a53e@monitor.digitaltrusttech.com/15");
			options.setDebug(false); // Enable debug mode to log Sentry activity
			options.setTracesSampleRate(1.0); // Capture 20% of transactions

		};
	}
}
