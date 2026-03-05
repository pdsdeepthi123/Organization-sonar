package com.dtt.organization.model;




import com.dtt.organization.enums.BeneficiaryType;
import com.dtt.organization.enums.SponsorType;

import jakarta.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="beneficiaries")
public class Benificiaries implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "sponsor_digital_id")
    private String sponsorDigitalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "sponsor_type")
    private SponsorType sponsorType;

    @Column(name = "sponsor_external_id")
    private String sponsorExternalId;



    @Column(name = "sponsor_name")
    private String sponsorName;

    @Column(name = "beneficiary_digital_id")
    private String beneficiaryDigitalId;

    @Column(name="beneficiary_name")
    private String beneficiaryName;

    @Enumerated(EnumType.STRING)
    @Column(name = "beneficiary_type")
    private BeneficiaryType beneficiaryType;

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
    private  String designation;

    @Column(name = "status")
    private String status;

    @Column(name = "created_on")
    private String createdOn;

    @Column(name = "updated_on")
    private String updatedOn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSponsorDigitalId() {
        return sponsorDigitalId;
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

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

	@Override
	public String toString() {
		return "Benificiaries [id=" + id + ", sponsorDigitalId=" + sponsorDigitalId + ", sponsorType=" + sponsorType
				+ ", sponsorExternalId=" + sponsorExternalId + ", sponsorName=" + sponsorName
				+ ", beneficiaryDigitalId=" + beneficiaryDigitalId + ", beneficiaryName=" + beneficiaryName
				+ ", beneficiaryType=" + beneficiaryType + ", sponsorPaymentPriorityLevel="
				+ sponsorPaymentPriorityLevel + ", beneficiaryNin=" + beneficiaryNin + ", beneficiaryPassport="
				+ beneficiaryPassport + ", beneficiaryMobileNumber=" + beneficiaryMobileNumber
				+ ", beneficiaryOfficeEmail=" + beneficiaryOfficeEmail + ", beneficiaryUgPassEmail="
				+ beneficiaryUgPassEmail + ", beneficiaryConsentAcquired=" + beneficiaryConsentAcquired
				+ ", signaturePhoto=" + signaturePhoto + ", designation=" + designation + ", status=" + status
				+ ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + "]";
	}

    
}
