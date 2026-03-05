package com.dtt.organization.dto;

public class UpdateOrganizationPrivilegeDto {

    private int id;

    private String orgId;

    private String privilege;

    private String status;

    private String adminName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Override
    public String toString() {
        return "UpdateOrganizationPrivilegeDto{" +
                "id=" + id +
                ", orgId='" + orgId + '\'' +
                ", privilege='" + privilege + '\'' +
                ", status='" + status + '\'' +
                ", adminName='" + adminName + '\'' +
                '}';
    }
}
