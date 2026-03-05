package com.dtt.organization.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrganizationDeletionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void deleteOrganization(String ouid) {
        String[] deleteQueries = {
                "DELETE FROM OrgContactsEmail e WHERE e.organizationUid = :ouid",
                "DELETE FROM OrganizationEmailDomain e WHERE e.organizationUid = :ouid",
                "DELETE FROM OrganizationPricingSlabDefinitions e WHERE e.organizationId = :ouid",
                "DELETE FROM OrganizationDocuments e WHERE e.organizationUid = :ouid",
                "DELETE FROM OrganizationSignatureTemplates e WHERE e.organizationUid = :ouid",
                "DELETE FROM OrganizationDirectors e WHERE e.organizationUid = :ouid",
                "DELETE FROM OrganizationDocumentsCheckBox e WHERE e.organizationUid = :ouid",
                "DELETE FROM OrganizationCertificates e WHERE e.organizationUid = :ouid",
                "DELETE FROM OrganizationCertificateLifeCycle e WHERE e.organizationUid = :ouid",
                "DELETE FROM OrganizationStatus e WHERE e.organizationUid = :ouid",
                "DELETE FROM SoftwareLicenseApprovalRequests e WHERE e.ouid = :ouid",
                "DELETE FROM WalletSignCertificate e WHERE e.organizationUid = :ouid",
                "DELETE FROM SoftwareLicenses e WHERE e.ouid = :ouid",
                "DELETE FROM OrgSubscriberEmailOld e WHERE e.organizationUid = :ouid",
                "DELETE FROM OrganizationDetails e WHERE e.organizationUid = :ouid"
        };

        for (String query : deleteQueries) {
            entityManager.createQuery(query)
                    .setParameter("ouid", ouid)
                    .executeUpdate();
        }
    }
}


