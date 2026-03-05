package com.dtt.organization.dto;

public class ToggleManageByAdmin {

    private String orgId;

    private boolean manageByAdmin;


    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public boolean isManageByAdmin() {
        return manageByAdmin;
    }

    public void setManageByAdmin(boolean manageByAdmin) {
        this.manageByAdmin = manageByAdmin;
    }
}
