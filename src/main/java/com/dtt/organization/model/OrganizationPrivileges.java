package com.dtt.organization.model;


import jakarta.persistence.*;

@Entity
@Table(name = "organization_privileges")
public class OrganizationPrivileges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "organization_id")
    private String organizationId;


    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "privilege")
    private String privilege;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_on")
    private String createdOn;

    @Column(name = "status")
    private String status;


    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_on")
    private String modifiedOn;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }


    @Override
    public String toString() {
        return "OrganizationPrivileges{" +
                "id=" + id +
                ", organizationId='" + organizationId + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", privilege='" + privilege + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", status='" + status + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", modifiedOn='" + modifiedOn + '\'' +
                '}';
    }
}
