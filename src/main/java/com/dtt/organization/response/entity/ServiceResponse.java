package com.dtt.organization.response.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceResponse {

    private String status;
    private String certificate;

    private String wrappedKey;




    private String serialNumber;

    private String keyLabel;




    private String issueDate;


    private String expiryDate;


    private String signature;


    @JsonProperty("certificate_serial_number")
    private String certificateSerialNumber;

    @JsonProperty("error_code")
    private String errorCode;

    /**
     * The error message.
     */
    @JsonProperty("error_message")
    private String errorMessage;

    /**
     * The certificate status.
     */
    @JsonProperty("certificate_status")
    private String certificateStatus;

    /**
     * The revocation reason.
     */
    @JsonProperty("revocation_reason")
    private String revocationReason;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getWrappedKey() {
        return wrappedKey;
    }

    public void setWrappedKey(String wrappedKey) {
        this.wrappedKey = wrappedKey;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getKeyLabel() {
        return keyLabel;
    }

    public void setKeyLabel(String keyLabel) {
        this.keyLabel = keyLabel;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCertificateSerialNumber() {
        return certificateSerialNumber;
    }

    public void setCertificateSerialNumber(String certificateSerialNumber) {
        this.certificateSerialNumber = certificateSerialNumber;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(String certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public String getRevocationReason() {
        return revocationReason;
    }

    public void setRevocationReason(String revocationReason) {
        this.revocationReason = revocationReason;
    }

    @Override
    public String toString() {
        return "ServiceResponse{" +
                "status='" + status + '\'' +
                ", certificate='" + certificate + '\'' +
                ", wrappedKey='" + wrappedKey + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", keyLabel='" + keyLabel + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", signature='" + signature + '\'' +
                ", certificateSerialNumber='" + certificateSerialNumber + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", certificateStatus='" + certificateStatus + '\'' +
                ", revocationReason='" + revocationReason + '\'' +
                '}';
    }
}
