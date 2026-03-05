
package com.dtt.organization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;


public class NotificationContextDTO implements Serializable {

	@JsonProperty("PREF_PAYMENT_STATUS")
	private Map<String, String> prefPaymentStatus;

	@JsonProperty("PREF_TRANSACTION_ID")
	private Map<String, String> prefTransactionId;

	@JsonProperty("PREF_ORG_LINK")
	private Map<String, String> prefOrgLink;

	@JsonProperty("PREF_BENEFICIARY_LINK")
	private Map<String, String> prefBeneficiaryLink;

	public Map<String, String> getPrefPaymentStatus() {
		return prefPaymentStatus;
	}

	public void setPrefPaymentStatus(Map<String, String> prefPaymentStatus) {
		this.prefPaymentStatus = prefPaymentStatus;
	}

	public Map<String, String> getPrefTransactionId() {
		return prefTransactionId;
	}

	public void setPrefTransactionId(Map<String, String> prefTransactionId) {
		this.prefTransactionId = prefTransactionId;
	}

	public Map<String, String> getPrefOrgLink() {
		return prefOrgLink;
	}

	public void setPrefOrgLink(Map<String, String> prefOrgLink) {
		this.prefOrgLink = prefOrgLink;
	}

	public Map<String, String> getPrefBeneficiaryLink() {
		return prefBeneficiaryLink;
	}

	public void setPrefBeneficiaryLink(Map<String, String> prefBeneficiaryLink) {
		this.prefBeneficiaryLink = prefBeneficiaryLink;
	}

	@Override
	public String toString() {
		return "NotificationContextDTO{" +
				"prefPaymentStatus=" + prefPaymentStatus +
				", prefTransactionId=" + prefTransactionId +
				", prefOrgLink=" + prefOrgLink +
				", prefBeneficiaryLink=" + prefBeneficiaryLink +
				'}';
	}
}
