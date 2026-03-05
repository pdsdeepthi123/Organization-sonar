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
@Table(name = "signature_templates")
@NamedQuery(name="SignatureTemplates.findAll", query="SELECT s FROM SignatureTemplates s")
public class SignatureTemplates implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name="template_id")
	private String templateId;
	
	@Column(name="display_name")
	private String displayName;
	
	@Column(name="type")
	private String type;
	
	@Column(name="sample_preview")
	private String samplePreview;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSamplePreview() {
		return samplePreview;
	}

	public void setSamplePreview(String samplePreview) {
		this.samplePreview = samplePreview;
	}

	@Override
	public String toString() {
		return "SignatureTemplates [id=" + id + ", templateId=" + templateId + ", displayName=" + displayName
				+ ", type=" + type + ", samplePreview=" + samplePreview + "]";
	}
	
}
