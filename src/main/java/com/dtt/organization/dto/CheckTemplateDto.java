package com.dtt.organization.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckTemplateDto implements Serializable {

    private String suid;
    private String organizationId;
    private int signatureTemplateId;
    private int esealTemplateId;
    private boolean hasEsealTemplate;
    private boolean hasSignatureTemplate;
    private String email;

    public String getSuid() {
        return suid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public int getSignatureTemplateId() {
        return signatureTemplateId;
    }

    public void setSignatureTemplateId(int signatureTemplateId) {
        this.signatureTemplateId = signatureTemplateId;
    }

    public int getEsealTemplateId() {
        return esealTemplateId;
    }

    public void setEsealTemplateId(int esealTemplateId) {
        this.esealTemplateId = esealTemplateId;
    }

    public boolean isHasEsealTemplate() {
        return hasEsealTemplate;
    }

    public void setHasEsealTemplate(boolean hasEsealTemplate) {
        this.hasEsealTemplate = hasEsealTemplate;
    }

    public boolean isHasSignatureTemplate() {
        return hasSignatureTemplate;
    }

    public void setHasSignatureTemplate(boolean hasSignatureTemplate) {
        this.hasSignatureTemplate = hasSignatureTemplate;
    }

    @Override
    public String toString() {
        return "CheckTemplateDto{" +
                "suid='" + suid + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", signatureTemplateId=" + signatureTemplateId +
                ", esealTemplateId=" + esealTemplateId +
                ", hasEsealTemplate=" + hasEsealTemplate +
                ", hasSignatureTemplate=" + hasSignatureTemplate +
                ", email='" + email + '\'' +
                '}';
    }
}
