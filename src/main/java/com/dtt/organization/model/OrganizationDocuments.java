package com.dtt.organization.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "organization_documents")
@NamedQuery(name="OrganizationDocuments.findAll", query="SELECT o FROM OrganizationDocuments o")
public class OrganizationDocuments implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "organization_documents_id", nullable = false, unique = true)
	private int organizationDocumentsId;

	
	@Column(name="organization_uid")
	private String organizationUid;
	
	@Column(name = "e_seal_image")
	private String eSealImage ;
	
	@Column(name = "org_details_pdf")
	private String organizationDetailsPdf ;
	
	@Column(name="incorporation_file")
	private String incorporation;
	
	@Column(name="tax_file")
	private String tax;
	
	@Column(name ="any_other_doc")
	private String anyOther;

	public int getOrganizationDocumentsId() {
		return organizationDocumentsId;
	}

	public void setOrganizationDocumentsId(int organizationDocumentsId) {
		this.organizationDocumentsId = organizationDocumentsId;
	}

	public String getOrganizationUid() {
		return organizationUid;
	}

	public void setOrganizationUid(String organizationUid) {
		this.organizationUid = organizationUid;
	}

	public String geteSealImage() {
		return eSealImage;
	}

	public void seteSealImage(String eSealImage) {
		this.eSealImage = eSealImage;
	}

	public String getOrganizationDetailsPdf() {
		return organizationDetailsPdf;
	}

	public void setOrganizationDetailsPdf(String organizationDetailsPdf) {
		this.organizationDetailsPdf = organizationDetailsPdf;
	}

	public String getIncorporation() {
		return incorporation;
	}

	public void setIncorporation(String incorporation) {
		this.incorporation = incorporation;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getAnyOther() {
		return anyOther;
	}

	public void setAnyOther(String anyOther) {
		this.anyOther = anyOther;
	}

	@Override
	public String toString() {
		return "OrganizationDocuments [organizationDocumentsId=" + organizationDocumentsId + ", organizationUid="
				+ organizationUid + ", eSealImage=" + eSealImage + ", organizationDetailsPdf=" + organizationDetailsPdf
				+ ", incorporation=" + incorporation + ", tax=" + tax + ", anyOther=" + anyOther + "]";
	}

}
