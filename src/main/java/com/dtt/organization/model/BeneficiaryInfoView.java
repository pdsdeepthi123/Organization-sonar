package com.dtt.organization.model;

import jakarta.persistence.*;

@Entity
@Table(name="beneficiary_info_view")
public class BeneficiaryInfoView {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beneficiary_id")
    private  int beneficiaryId;

    @Column(name = "sponsor_name")
    private String sponsorName;

    @Column(name = "beneficiary_name")
    private String beneficiaryName;
    @Column(name = "sponsor_digital_id")
    private String sponsorDigitalId;
    @Column(name = "sponsor_type")
    private String sponsorType;
    @Column(name = "sponsor_external_id")
    private  String sponsorExternalId;
    @Column(name = "beneficiary_digital_id")
    private String beneficiaryDigitalId;
    @Column(name = "beneficiary_type")
    private String beneficiaryType;
    @Column(name = "sponsor_payment_priority_level")
    private int sponsorPaymentPriorityLevel;
    @Column(name = "beneficiary_nin")
    private String beneficiaryNin;
    @Column(name = "beneficiary_passport")
    private String beneficiaryPassport;
    @Column(name = "beneficiary_mobile_number")
    private String beneficiaryMobileNumber;
    @Column(name = "beneficiary_office_email")
    private String beneficiaryOfficeEmail;
    @Column(name = "beneficiary_ugpass_email")
    private String beneficiaryUgPassEmail;
    @Column(name = "beneficiary_consent_acquired")
    private boolean beneficiaryConsentAcquired;
    @Column(name = "signature_photo")
    private String signaturePhoto;
    @Column(name = "designation")
    private String designation;
    @Column(name = "beneficiary_status")
    private String beneficiaryStatus;
    @Column(name = "beneficiary_created_on")
    private String beneficiaryCreatedOn;
    @Column(name = "beneficiary_updated_on")
    private String beneficiaryUpdatedOn;
    @Column(name = "validity_id")
    private int validityId;
    @Column(name = "privilege_service_id")
    private String privilegeServiceId;
    @Column(name = "validity_applicable")
    private String validityApplicable;
    @Column(name = "valid_from")
    private String validFrom;
    @Column(name = "valid_upto")
    private String validUpTo;
    @Column(name = "validity_status")
    private String validityStatus;
    @Column(name = "validity_created_on")
    private String validityCreatedOn;
    @Column(name = "validity_updated_on")
    private String validityUpdatedOn;
    @Column(name = "privilege_id")
    private int privilegeId;
    @Column(name = "privilege_service_name")
    private String privilegeServiceName;
    @Column(name = "privilege_service_display_name")
    private String  privilegeServiceDisplayName;
    @Column(name = "privilege_status")
    private String privilegeStatus;
    @Column(name = "is_chargeable")
    private int isChargeable;

    public int getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(int beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
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

    public String getSponsorDigitalId() {
        return sponsorDigitalId;
    }

    public void setSponsorDigitalId(String sponsorDigitalId) {
        this.sponsorDigitalId = sponsorDigitalId;
    }

    public String getSponsorType() {
        return sponsorType;
    }

    public void setSponsorType(String sponsorType) {
        this.sponsorType = sponsorType;
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

    public String getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(String beneficiaryType) {
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

    public String getBeneficiaryUgPassEmail() {
        return beneficiaryUgPassEmail;
    }

    public void setBeneficiaryUgPassEmail(String beneficiaryUgPassEmail) {
        this.beneficiaryUgPassEmail = beneficiaryUgPassEmail;
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

    public String getBeneficiaryStatus() {
        return beneficiaryStatus;
    }

    public void setBeneficiaryStatus(String beneficiaryStatus) {
        this.beneficiaryStatus = beneficiaryStatus;
    }

    public String getBeneficiaryCreatedOn() {
        return beneficiaryCreatedOn;
    }

    public void setBeneficiaryCreatedOn(String beneficiaryCreatedOn) {
        this.beneficiaryCreatedOn = beneficiaryCreatedOn;
    }

    public String getBeneficiaryUpdatedOn() {
        return beneficiaryUpdatedOn;
    }

    public void setBeneficiaryUpdatedOn(String beneficiaryUpdatedOn) {
        this.beneficiaryUpdatedOn = beneficiaryUpdatedOn;
    }

    public int getValidityId() {
        return validityId;
    }

    public void setValidityId(int validityId) {
        this.validityId = validityId;
    }

    public String getPrivilegeServiceId() {
        return privilegeServiceId;
    }

    public void setPrivilegeServiceId(String privilegeServiceId) {
        this.privilegeServiceId = privilegeServiceId;
    }

    public String getValidityApplicable() {
        return validityApplicable;
    }

    public void setValidityApplicable(String validityApplicable) {
        this.validityApplicable = validityApplicable;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidUpTo() {
        return validUpTo;
    }

    public void setValidUpTo(String validUpTo) {
        this.validUpTo = validUpTo;
    }

    public String getValidityStatus() {
        return validityStatus;
    }

    public void setValidityStatus(String validityStatus) {
        this.validityStatus = validityStatus;
    }

    public String getValidityCreatedOn() {
        return validityCreatedOn;
    }

    public void setValidityCreatedOn(String validityCreatedOn) {
        this.validityCreatedOn = validityCreatedOn;
    }

    public String getValidityUpdatedOn() {
        return validityUpdatedOn;
    }

    public void setValidityUpdatedOn(String validityUpdatedOn) {
        this.validityUpdatedOn = validityUpdatedOn;
    }

    public int getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(int privilegeId) {
        this.privilegeId = privilegeId;
    }

    public String getPrivilegeServiceName() {
        return privilegeServiceName;
    }

    public void setPrivilegeServiceName(String privilegeServiceName) {
        this.privilegeServiceName = privilegeServiceName;
    }

    public String getPrivilegeServiceDisplayName() {
        return privilegeServiceDisplayName;
    }

    public void setPrivilegeServiceDisplayName(String privilegeServiceDisplayName) {
        this.privilegeServiceDisplayName = privilegeServiceDisplayName;
    }

    public String getPrivilegeStatus() {
        return privilegeStatus;
    }

    public void setPrivilegeStatus(String privilegeStatus) {
        this.privilegeStatus = privilegeStatus;
    }

    public int getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(int isChargeable) {
        this.isChargeable = isChargeable;
    }

    @Override
    public String toString() {
        return "BeneficiaryInfoView{" +
                "beneficiaryId=" + beneficiaryId +
                ", sponsorName='" + sponsorName + '\'' +
                ", beneficiaryName='" + beneficiaryName + '\'' +
                ", sponsorDigitalId='" + sponsorDigitalId + '\'' +
                ", sponsorType='" + sponsorType + '\'' +
                ", sponsorExternalId='" + sponsorExternalId + '\'' +
                ", beneficiaryDigitalId='" + beneficiaryDigitalId + '\'' +
                ", beneficiaryType='" + beneficiaryType + '\'' +
                ", sponsorPaymentPriorityLevel=" + sponsorPaymentPriorityLevel +
                ", beneficiaryNin='" + beneficiaryNin + '\'' +
                ", beneficiaryPassport='" + beneficiaryPassport + '\'' +
                ", beneficiaryMobileNumber='" + beneficiaryMobileNumber + '\'' +
                ", beneficiaryOfficeEmail='" + beneficiaryOfficeEmail + '\'' +
                ", beneficiaryUgPassEmail='" + beneficiaryUgPassEmail + '\'' +
                ", beneficiaryConsentAcquired=" + beneficiaryConsentAcquired +
                ", signaturePhoto='" + signaturePhoto + '\'' +
                ", designation='" + designation + '\'' +
                ", beneficiaryStatus='" + beneficiaryStatus + '\'' +
                ", beneficiaryCreatedOn='" + beneficiaryCreatedOn + '\'' +
                ", beneficiaryUpdatedOn='" + beneficiaryUpdatedOn + '\'' +
                ", validityId=" + validityId +
                ", privilegeServiceId='" + privilegeServiceId + '\'' +
                ", validityApplicable='" + validityApplicable + '\'' +
                ", validFrom='" + validFrom + '\'' +
                ", validUpTo='" + validUpTo + '\'' +
                ", validityStatus='" + validityStatus + '\'' +
                ", validityCreatedOn='" + validityCreatedOn + '\'' +
                ", validityUpdatedOn='" + validityUpdatedOn + '\'' +
                ", privilegeId=" + privilegeId +
                ", privilegeServiceName='" + privilegeServiceName + '\'' +
                ", privilegeServiceDisplayName='" + privilegeServiceDisplayName + '\'' +
                ", privilegeStatus='" + privilegeStatus + '\'' +
                ", isChargeable=" + isChargeable +
                '}';
    }
}
