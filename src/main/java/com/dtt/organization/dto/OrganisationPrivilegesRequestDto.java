package com.dtt.organization.dto;

import java.util.List;

public class OrganisationPrivilegesRequestDto {

    private int id;


    private String organizationId;


    private List<String> privileges;

    private String suid;


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

    public List<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<String> privileges) {
        this.privileges = privileges;
    }

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    @Override
    public String toString() {
        return "OrganisationPrivilegesRequestDto{" +
                "id=" + id +
                ", organizationId='" + organizationId + '\'' +
                ", privileges=" + privileges +
                ", suid='" + suid + '\'' +
                '}';
    }
}
