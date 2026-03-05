/*
 * @copyright (DigitalTrust Technologies Private Limited, Hyderabad) 2021, 
 * All rights reserved.
 */
package com.dtt.organization.dto;

import java.io.Serializable;

/**
 * The Class IssueCertificateRequest.
 */
public class IssueCertificateDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subscriber unique id. */
	private String subscriberUniqueId;

	/** The key ID. */
	private String keyID;

	/** The common name. */
	private String commonName;

	/** The country name. */
	private String countryName;
	
	/** The wallet certificate boolean. */
	private boolean walletCertificateUsed;

	/**
	 * Gets the subscriber unique id.
	 *
	 * @return the subscriber unique id
	 */
	public String getSubscriberUniqueId() {
		return subscriberUniqueId;
	}

	/**
	 * Sets the subscriber unique id.
	 *
	 * @param subscriberUniqueId
	 *            the new subscriber unique id
	 */
	public void setSubscriberUniqueId(String subscriberUniqueId) {
		this.subscriberUniqueId = subscriberUniqueId;
	}

	/**
	 * Gets the key ID.
	 *
	 * @return the key ID
	 */
	public String getKeyID() {
		return keyID;
	}

	/**
	 * Sets the key ID.
	 *
	 * @param keyID
	 *            the new key ID
	 */
	public void setKeyID(String keyID) {
		this.keyID = keyID;
	}

	/**
	 * Gets the common name.
	 *
	 * @return the common name
	 */
	public String getCommonName() {
		return commonName;
	}

	/**
	 * Sets the common name.
	 *
	 * @param commonName
	 *            the new common name
	 */
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	/**
	 * Gets the country name.
	 *
	 * @return the country name
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * Sets the country name.
	 *
	 * @param countryName
	 *            the new country name
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public boolean isWalletCertificateUsed() {
		return walletCertificateUsed;
	}

	public void setWalletCertificateUsed(boolean walletCertificateUsed) {
		this.walletCertificateUsed = walletCertificateUsed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    return "{" + 
	           "\"subscriberDigitalID\"" + ":" + "\"" + subscriberUniqueId + "\"," + 
	           "\"keyID\"" + ":" + "\"" + keyID + "\"," + 
	           "\"commonName\"" + ":" + "\"" + commonName + "\"," + 
	           "\"countryName\"" + ":" + "\"" + countryName + "\"," + 
	           "\"walletCertificateUsed\"" + ":" + walletCertificateUsed + 
	           "}";
	}



}