package com.dtt.organization.dto;

import com.dtt.organization.model.OrganizationCertificates;

import java.util.List;

public class OrgCertResponse {

    private List<OrganizationCertificates> organizationCertificates;

    private float qrFaceMatchThreshold;

    public List<OrganizationCertificates> getOrganizationCertificates() {
        return organizationCertificates;
    }

    public void setOrganizationCertificates(List<OrganizationCertificates> organizationCertificates) {
        this.organizationCertificates = organizationCertificates;
    }

    public float getQrFaceMatchThreshold() {
        return qrFaceMatchThreshold;
    }

    public void setQrFaceMatchThreshold(float qrFaceMatchThreshold) {
        this.qrFaceMatchThreshold = qrFaceMatchThreshold;
    }

    @Override
    public String toString() {
        return "OrgCertResponse{" +
                "organizationCertificates=" + organizationCertificates +
                ", qrFaceMatchThreshold=" + qrFaceMatchThreshold +
                '}';
    }
}
