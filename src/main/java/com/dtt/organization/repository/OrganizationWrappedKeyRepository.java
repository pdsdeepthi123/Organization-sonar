package com.dtt.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.OrganizationWrappedKey;

@Repository
public interface OrganizationWrappedKeyRepository extends JpaRepository<OrganizationWrappedKey, String>{

	
	/**
	 * Find bycertificate serial number.
	 *
	 * @param certificateSerialNumber
	 *            the certificate serial number
	 * @return the subscriber wrapped key
	 */
	OrganizationWrappedKey findBycertificateSerialNumber(String certificateSerialNumber);
}
