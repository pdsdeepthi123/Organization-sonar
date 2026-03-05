package com.dtt.organization.model;


import jakarta.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "beneficiary_validity")
public class BeneficiaryValidity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "beneficiary_id")
    private int beneficiaryId;

    @Column(name = "privilege_service_id")
    private int privilegeServiceId;

    @Column(name = "validity_applicable")
    private boolean validityApplicable;

    @Column(name = "valid_from")
    private String validFrom;

    @Column(name = "valid_upto")
    private String validUpTo;

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

    public int getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(int beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public int getPrivilegeServiceId() {
        return privilegeServiceId;
    }

    public void setPrivilegeServiceId(int privilegeServiceId) {
        this.privilegeServiceId = privilegeServiceId;
    }

    public boolean isValidityApplicable() {
        return validityApplicable;
    }

    public void setValidityApplicable(boolean validityApplicable) {
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
		return "BeneficiaryValidity [id=" + id + ", beneficiaryId=" + beneficiaryId + ", privilegeServiceId="
				+ privilegeServiceId + ", validityApplicable=" + validityApplicable + ", validFrom=" + validFrom
				+ ", validUpTo=" + validUpTo + ", status=" + status + ", createdOn=" + createdOn + ", updatedOn="
				+ updatedOn + "]";
	}

    
}
