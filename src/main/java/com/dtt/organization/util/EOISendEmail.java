package com.dtt.organization.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.EmailReqDto;
import com.dtt.organization.model.TrustedStakeholder;

public class EOISendEmail implements Runnable {

	private static final String CLASS = EOISendEmail.class.getSimpleName();
	Logger logger = LoggerFactory.getLogger(EOISendEmail.class);
	
	private final List<TrustedStakeholder> trustedStakeholdersList;

	String emailBaseUrl;
	RestTemplate restTemplate;
	String link;

	public EOISendEmail(List<TrustedStakeholder> trustedStakeholdersList, String emailBaseUrl,
			RestTemplate restTemplate, String link) {
		this.trustedStakeholdersList = trustedStakeholdersList;
		this.emailBaseUrl = emailBaseUrl;
		this.restTemplate = restTemplate;
		this.link = link;
	}


	@Override
	public void run() {
		String methodName = Utility.getMethodName();
		logger.info("{} - {}: Starting email sending process", CLASS, methodName);

		try {
			processTrustedStakeholders(methodName);
		} catch (Exception e) {
			logger.error("{} - {}: Unexpected error in email sending process",
					CLASS, methodName, e);
		}
	}

	private void processTrustedStakeholders(String methodName) {

		String url = emailBaseUrl;

		if (trustedStakeholdersList == null || trustedStakeholdersList.isEmpty()) {
			logger.warn("{} - {}: No trusted stakeholders found to process",
					CLASS, methodName);
			return;
		}

		for (TrustedStakeholder trustedStakeholder : trustedStakeholdersList) {

			if (handleSleep(methodName)) {
				logger.warn("{} - {}: Stopping email processing due to thread interruption",
						CLASS, methodName);
				return;
			}

			sendEmailToStakeholder(trustedStakeholder, url, methodName);
		}
	}

	private boolean handleSleep(String methodName) {
		try {
			TimeUnit.SECONDS.sleep(4);
			return false;
		} catch (InterruptedException ie) {
			logger.error("{} - {}: Thread interrupted during delay",
					CLASS, methodName, ie);

			Thread.currentThread().interrupt(); // restore interrupt flag
			return true; // signal to stop execution
		}
	}

	private EmailReqDto createEmailRequest(TrustedStakeholder trustedStakeholder) {
	    EmailReqDto emailReqDto = new EmailReqDto();
	    TrustedStakeholder stakeholder = new TrustedStakeholder();
	    stakeholder.setName(trustedStakeholder.getName());
	    stakeholder.setReferenceId(trustedStakeholder.getReferenceId());
	    emailReqDto.setLink(link);
	    emailReqDto.setEmailId(trustedStakeholder.getSpocUgpassEmail());
	    emailReqDto.setTrustedStakeholder(stakeholder);
	    return emailReqDto;
	}

	private void sendEmailToStakeholder(TrustedStakeholder trustedStakeholder,
										String url,
										String methodName) {

		try {

			EmailReqDto emailReqDto = createEmailRequest(trustedStakeholder);
			HttpEntity<Object> requestEntity = new HttpEntity<>(emailReqDto);

			logger.info("{} - {}: Sending email to: {}",
					CLASS, methodName, emailReqDto.getEmailId());

			ResponseEntity<ApiResponses> response =
					restTemplate.exchange(url,
							HttpMethod.POST,
							requestEntity,
							ApiResponses.class);

			handleResponse(response, emailReqDto);

		} catch (RestClientException ex) {

			logger.error("{} - {}: REST error while sending email to stakeholder",
					CLASS, methodName, ex);

		} catch (Exception ex) {

			logger.error("{} - {}: Unexpected error while sending email",
					CLASS, methodName, ex);
		}
	}

	private void handleResponse(ResponseEntity<ApiResponses> res,
								EmailReqDto emailReqDto) {

		HttpStatusCode status = res.getStatusCode();

		if (status.is2xxSuccessful()) {
			logger.info("{} - Email sent successfully to: {}",
					CLASS, emailReqDto.getEmailId());
			return;
		}

		logger.error("{} - Failed to send email to: {}. Status: {}",
				CLASS, emailReqDto.getEmailId(), status.value());
	}


}
