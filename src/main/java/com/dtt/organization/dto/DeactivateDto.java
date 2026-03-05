package com.dtt.organization.dto;

import java.io.Serializable;

public class DeactivateDto  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String organizationUID;

    private String employeeEmail;

    private String endDate;

    public String getOrganizationUID() {
        return organizationUID;
    }

    public void setOrganizationUID(String organizationUID) {
        this.organizationUID = organizationUID;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "DeactivateDto{" +
                "organizationUID='" + organizationUID + '\'' +
                ", employeeEmail='" + employeeEmail + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
