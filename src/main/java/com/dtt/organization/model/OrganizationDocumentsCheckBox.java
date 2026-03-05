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
@Table(name = "organization_document_check")
@NamedQuery(name="OrganizationDocumentsCheckBox.findAll", query="SELECT o FROM OrganizationDocumentsCheckBox o")
public class OrganizationDocumentsCheckBox implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "organizationDoc_check_id", nullable = false, unique = true)
	private int organizationDocCheckId;
	
	
	@Column(name="organization_uid")
	private String organizationUid;
	
	@Column(name="document_check_box")
	private String documentListCheckbox;

	public int getOrganizationDocCheckId() {
		return organizationDocCheckId;
	}

	public void setOrganizationDocCheckId(int organizationDocCheckId) {
		this.organizationDocCheckId = organizationDocCheckId;
	}

	public String getOrganizationUid() {
		return organizationUid;
	}

	public void setOrganizationUid(String organizationUid) {
		this.organizationUid = organizationUid;
	}

	public String getDocumentListCheckbox() {
		return documentListCheckbox;
	}

	public void setDocumentListCheckbox(String documentListCheckbox) {
		this.documentListCheckbox = documentListCheckbox;
	}

	@Override
	public String toString() {
		return "OrganizationDocumentsCheckBox [organizationDocCheckId=" + organizationDocCheckId + ", organizationUid="
				+ organizationUid + ", documentListCheckbox=" + documentListCheckbox + "]";
	}

}
