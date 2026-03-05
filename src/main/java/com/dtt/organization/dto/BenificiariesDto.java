package com.dtt.organization.dto;


import com.dtt.organization.enums.BeneficiaryType;
import com.dtt.organization.enums.SponsorType;
import com.dtt.organization.model.BeneficiariedPrivilegeService;
import com.dtt.organization.model.BeneficiaryValidity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.List;

public class BenificiariesDto {

    private int id;

    private String sponsorDigitalId;

    private String sponsorName;

    @Enumerated(EnumType.STRING)
    private SponsorType sponsorType;

    private String sponsorExternalId;

    private String beneficiaryName;

    private String beneficiaryDigitalId;

    @Enumerated(EnumType.STRING)
    private BeneficiaryType beneficiaryType;

    private int sponsorPaymentPriorityLevel;

    private String beneficiaryNin;

    private String beneficiaryPassport;

    private String beneficiaryMobileNumber;

    private String beneficiaryOfficeEmail;

    private String beneficiaryUgpassEmail;

    private boolean beneficiaryConsentAcquired;

    private String signaturePhoto;

    private  String designation;

    private String status;

    private String createdOn;

    private String updatedOn;

    private List<BeneficiaryValidity> beneficiaryValidities;

   private List<BeneficiariedPrivilegeService> beneficiariedPrivilegeList;




    public List<BeneficiariedPrivilegeService> getBeneficiariedPrivilegeList() {
        return beneficiariedPrivilegeList;
    }

    public void setBeneficiariedPrivilegeList(List<BeneficiariedPrivilegeService> beneficiariedPrivilegeList) {
        this.beneficiariedPrivilegeList = beneficiariedPrivilegeList;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSponsorDigitalId() {
        return sponsorDigitalId;
    }


    public List<BeneficiaryValidity> getBeneficiaryValidities() {
        return beneficiaryValidities;
    }

    public void setBeneficiaryValidities(List<BeneficiaryValidity> beneficiaryValidities) {
        this.beneficiaryValidities = beneficiaryValidities;
    }

    public void setSponsorDigitalId(String sponsorDigitalId) {
        this.sponsorDigitalId = sponsorDigitalId;
    }



    public String getSponsorExternalId() {
        return sponsorExternalId;
    }

    public void setSponsorExternalId(String sponsorExternalId) {
        this.sponsorExternalId = sponsorExternalId;
    }

    public String getBeneficiaryDigitalId() {
        return beneficiaryDigitalId;
    }

    public void setBeneficiaryDigitalId(String beneficiaryDigitalId) {
        this.beneficiaryDigitalId = beneficiaryDigitalId;
    }

    public SponsorType getSponsorType() {
        return sponsorType;
    }

    public void setSponsorType(SponsorType sponsorType) {
        this.sponsorType = sponsorType;
    }

    public BeneficiaryType getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(BeneficiaryType beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public int getSponsorPaymentPriorityLevel() {
        return sponsorPaymentPriorityLevel;
    }

    public void setSponsorPaymentPriorityLevel(int sponsorPaymentPriorityLevel) {
        this.sponsorPaymentPriorityLevel = sponsorPaymentPriorityLevel;
    }

    public String getBeneficiaryNin() {
        return beneficiaryNin;
    }

    public void setBeneficiaryNin(String beneficiaryNin) {
        this.beneficiaryNin = beneficiaryNin;
    }

    public String getBeneficiaryPassport() {
        return beneficiaryPassport;
    }

    public void setBeneficiaryPassport(String beneficiaryPassport) {
        this.beneficiaryPassport = beneficiaryPassport;
    }

    public String getBeneficiaryMobileNumber() {
        return beneficiaryMobileNumber;
    }

    public void setBeneficiaryMobileNumber(String beneficiaryMobileNumber) {
        this.beneficiaryMobileNumber = beneficiaryMobileNumber;
    }

    public String getBeneficiaryOfficeEmail() {
        return beneficiaryOfficeEmail;
    }

    public void setBeneficiaryOfficeEmail(String beneficiaryOfficeEmail) {
        this.beneficiaryOfficeEmail = beneficiaryOfficeEmail;
    }

    public String getBeneficiaryUgpassEmail() {
        return beneficiaryUgpassEmail;
    }

    public void setBeneficiaryUgpassEmail(String beneficiaryUgpassEmail) {
        this.beneficiaryUgpassEmail = beneficiaryUgpassEmail;
    }

    public boolean isBeneficiaryConsentAcquired() {
        return beneficiaryConsentAcquired;
    }

    public void setBeneficiaryConsentAcquired(boolean beneficiaryConsentAcquired) {
        this.beneficiaryConsentAcquired = beneficiaryConsentAcquired;
    }

    public String getSignaturePhoto() {
        return signaturePhoto;
    }

    public void setSignaturePhoto(String signaturePhoto) {
        this.signaturePhoto = signaturePhoto;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }



    @Override
    public String toString() {
        return "BenificiariesDto{" +
                "id=" + id +
                ", sponsorDigitalId='" + sponsorDigitalId + '\'' +
                ", sponsorName='" + sponsorName + '\'' +
                ", sponsorType=" + sponsorType +
                ", sponsorExternalId='" + sponsorExternalId + '\'' +
                ", beneficiaryName='" + beneficiaryName + '\'' +
                ", beneficiaryDigitalId='" + beneficiaryDigitalId + '\'' +
                ", beneficiaryType=" + beneficiaryType +
                ", sponsorPaymentPriorityLevel=" + sponsorPaymentPriorityLevel +
                ", beneficiaryNin='" + beneficiaryNin + '\'' +
                ", beneficiaryPassport='" + beneficiaryPassport + '\'' +
                ", beneficiaryMobileNumber='" + beneficiaryMobileNumber + '\'' +
                ", beneficiaryOfficeEmail='" + beneficiaryOfficeEmail + '\'' +
                ", beneficiaryUgpassEmail='" + beneficiaryUgpassEmail + '\'' +
                ", beneficiaryConsentAcquired=" + beneficiaryConsentAcquired +
                ", signaturePhoto='" + signaturePhoto + '\'' +
                ", designation='" + designation + '\'' +
                ", status='" + status + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", beneficiaryValidities=" + beneficiaryValidities +
                ", beneficiariedPrivilegeList=" + beneficiariedPrivilegeList +
                '}';
    }
}