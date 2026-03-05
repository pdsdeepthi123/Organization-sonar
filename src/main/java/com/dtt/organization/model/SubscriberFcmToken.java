package com.dtt.organization.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name="subscriber_fcm_token")
@NamedQuery(name="SubscriberFcmToken.findAll", query="SELECT s FROM SubscriberFcmToken s")
public class SubscriberFcmToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="created_date")
	private String createdDate;

	@Column(name="fcm_token")
	private String fcmToken;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="subscriber_fcm_token_id")
	private int subscriberFcmTokenId;

	@Column(name="subscriber_uid")
	private String subscriberUid;



	public String getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getFcmToken() {
		return this.fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public int getSubscriberFcmTokenId() {
		return this.subscriberFcmTokenId;
	}

	public void setSubscriberFcmTokenId(int subscriberFcmTokenId) {
		this.subscriberFcmTokenId = subscriberFcmTokenId;
	}

	public String getSubscriberUid() {
		return this.subscriberUid;
	}

	public void setSubscriberUid(String subscriberUid) {
		this.subscriberUid = subscriberUid;
	}

	@Override
	public String toString() {
		return "SubscriberFcmToken [createdDate=" + createdDate + ", fcmToken=" + fcmToken + ", subscriberFcmTokenId="
				+ subscriberFcmTokenId + ", subscriberUid=" + subscriberUid + "]";
	}
	

}
