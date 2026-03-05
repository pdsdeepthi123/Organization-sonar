package com.dtt.organization.dto;

public class SignatureTemplateUpdateDto {

    private String organizationUid;

    private int signatureTemplateId;

    private int esealSignatureTemplateId;

    public String getOrganizationUid() {
        return organizationUid;
    }

    public void setOrganizationUid(String organizationUid) {
        this.organizationUid = organizationUid;
    }

    public int getSignatureTemplateId() {
        return signatureTemplateId;
    }

    public void setSignatureTemplateId(int signatureTemplateId) {
        this.signatureTemplateId = signatureTemplateId;
    }

    public int getEsealSignatureTemplateId() {
        return esealSignatureTemplateId;
    }

    public void setEsealSignatureTemplateId(int esealSignatureTemplateId) {
        this.esealSignatureTemplateId = esealSignatureTemplateId;
    }

    @Override
    public String toString() {
        return "SignatureTemplateUpdateDto{" +
                "organizationUid='" + organizationUid + '\'' +
                ", signatureTemplateId=" + signatureTemplateId +
                ", esealSignatureTemplateId=" + esealSignatureTemplateId +
                '}';
    }
}
