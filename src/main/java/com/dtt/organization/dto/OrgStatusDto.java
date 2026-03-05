package com.dtt.organization.dto;

import java.util.Date;

public class OrgStatusDto {

    private String certificateStatus;
    private Date certificateStartDate;

    private Date certificateEndDate;

    public String getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(String certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public Date getCertificateStartDate() {
        return certificateStartDate;
    }

    public void setCertificateStartDate(Date certificateStartDate) {
        this.certificateStartDate = certificateStartDate;
    }

    public Date getCertificateEndDate() {
        return certificateEndDate;
    }

    public void setCertificateEndDate(Date certificateEndDate) {
        this.certificateEndDate = certificateEndDate;
    }

    @Override
    public String toString() {
        return "OrgStatusDto{" +
                "certificateStatus='" + certificateStatus + '\'' +
                ", certificateStartDate=" + certificateStartDate +
                ", certificateEndDate=" + certificateEndDate +
                '}';
    }
}
