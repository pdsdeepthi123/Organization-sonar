package com.dtt.organization.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.dtt.organization.dto.NotificationContextDTO;
import com.dtt.organization.dto.NotificationDTO;
import com.dtt.organization.dto.NotificationDataDTO;
import com.dtt.organization.model.Benificiaries;
import com.dtt.organization.model.Subscriber;
import com.dtt.organization.model.SubscriberFcmToken;
import com.dtt.organization.repository.BeneficiariesRepo;
import com.dtt.organization.repository.SubscriberFcmTokenRepoIface;
import com.dtt.organization.repository.SubscriberRepository;

public class LinkedBeneficiraryWorkerThread implements Runnable {

	private final Benificiaries benificiariesDb;
	private final SubscriberRepository subscriberRepository;
	private final BeneficiariesRepo beneficiariesRepo;
	private final SubscriberFcmTokenRepoIface subscriberFcmTokenRepoIface;
	private final String sendNotificationURL;
	private final String sponsorLinkedMessage;

	Logger logger= LoggerFactory.getLogger(LinkedBeneficiraryWorkerThread.class);

	public LinkedBeneficiraryWorkerThread(
			Benificiaries benificiariesDb,
			SubscriberRepository subscriberRepository,
			BeneficiariesRepo beneficiariesRepo,
			SubscriberFcmTokenRepoIface subscriberFcmTokenRepoIface,
			String sendNotificationURL,
			String sponsorLinkedMessage) {

		this.benificiariesDb = benificiariesDb;
		this.subscriberRepository = subscriberRepository;
		this.beneficiariesRepo = beneficiariesRepo;
		this.subscriberFcmTokenRepoIface = subscriberFcmTokenRepoIface;
		this.sendNotificationURL = sendNotificationURL;
		this.sponsorLinkedMessage = sponsorLinkedMessage;
	}
	@Override
	public void run() {
		try {
			


			Subscriber subscriber = subscriberRepository.findSubscriberDetails(
					benificiariesDb.getBeneficiaryNin(),
					benificiariesDb.getBeneficiaryPassport(),
					benificiariesDb.getBeneficiaryUgPassEmail(),
					benificiariesDb.getBeneficiaryMobileNumber()
			).stream().findFirst().orElse(null);


			if (subscriber != null) {
				SubscriberFcmToken subscriberFcmToken = subscriberFcmTokenRepoIface.findBysubscriberUid(subscriber.getSubscriberUid());

				benificiariesDb.setBeneficiaryDigitalId(subscriber.getSubscriberUid());
				benificiariesDb.setBeneficiaryName(subscriber.getFullName());
				benificiariesDb.setBeneficiaryConsentAcquired(true);
				benificiariesDb.setBeneficiaryMobileNumber(subscriber.getMobileNumber());
				benificiariesDb.setBeneficiaryUgPassEmail(subscriber.getEmailId());
				benificiariesDb.setBeneficiaryNin(subscriber.getNationalId());
				benificiariesDb.setBeneficiaryPassport(subscriber.getIdDocNumber());

				beneficiariesRepo.save(benificiariesDb);
				logger.info("subscriber.getFullName(): {}", subscriber.getFullName());
				logger.info("subscriberView.getFcmToken(): {}", subscriberFcmToken.getFcmToken());
				logger.info("sendNotificationURL: {}", sendNotificationURL);
				sendNotification(subscriber.getFullName(), subscriberFcmToken.getFcmToken(), sendNotificationURL,sponsorLinkedMessage);

			}

		} catch (Exception e) {
			logger.info("{}",e.getMessage());
		}

	}

	public void sendNotification(String fullName, String fcmToken, String sendNotificationURL,String sponsorLinkedMessage) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			NotificationDTO notificationBody = new NotificationDTO();
			NotificationDataDTO dataDTO = new NotificationDataDTO();
		    NotificationContextDTO contextDTO = new NotificationContextDTO();
			notificationBody.setTo(fcmToken);
			notificationBody.setPriority("high");
			dataDTO.setTitle("Hi " + fullName);
			Map<String, String> orgLinkStatus = new HashMap<>();

			dataDTO.setBody(sponsorLinkedMessage);
			orgLinkStatus.put("beneficiaryLinkedStatus", "Success");

			contextDTO.setPrefBeneficiaryLink(orgLinkStatus);
			dataDTO.setNotificationContext(contextDTO);
			notificationBody.setData(dataDTO);
			HttpEntity<Object> requestEntity = new HttpEntity<>(notificationBody, headers);

			ResponseEntity<Object> res = restTemplate.exchange(sendNotificationURL, HttpMethod.POST, requestEntity,
					Object.class);
			if (res.getStatusCode().value() == 200) {
				logger.info("Notification sent");
			} else {
				logger.info("Notification failed");
			}

		} catch (Exception e) {
			logger.info("{}",e.getMessage());
		}
	}

}
