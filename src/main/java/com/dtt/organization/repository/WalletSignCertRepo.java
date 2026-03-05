package com.dtt.organization.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dtt.organization.model.WalletSignCertificate;

@Repository
public interface WalletSignCertRepo extends JpaRepository<WalletSignCertificate, String>{

	@Query("SELECT w FROM WalletSignCertificate w WHERE w.certificateStatus = ?1 AND w.organizationUid = ?2")
	WalletSignCertificate findByOrganizationId(String status, String organizationId);

	WalletSignCertificate findTopByOrganizationUidOrderByUpdatedDateDesc(String organizationId);


	@Query("SELECT w FROM WalletSignCertificate w WHERE w.certificateEndDate <= CURRENT_DATE AND w.certificateStatus = 'ACTIVE' AND w.organizationUid = ?1")
	WalletSignCertificate findByCertificateStatusExpiredVG(String ouid);

	WalletSignCertificate findByTransactionReferenceId(String walletTransactionRefId);

}
