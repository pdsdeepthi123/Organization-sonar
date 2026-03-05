package com.dtt.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dtt.organization.model.SubscriberCertificate;
import org.springframework.data.jpa.repository.Query;

public interface SubscriberCertificatesRepoIface extends JpaRepository<SubscriberCertificate,String>{

    SubscriberCertificate findTop1ByCertificateSerialNumberOrderByCreatedDateDesc(String serialNumber);

    default String findSuidByCertSerialNumber(String serialNumber) {
        SubscriberCertificate cert = findTop1ByCertificateSerialNumberOrderByCreatedDateDesc(serialNumber);
        return cert != null ? cert.getSubscriberUid() : null;
    }

    @Query("SELECT s FROM SubscriberCertificate s WHERE s.certificateSerialNumber = ?1")
    SubscriberCertificate findByCertificateSerialNumber(String serialNumber);
}
