package com.dtt.organization.model;


import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="privilege")
public class BeneficiariedPrivilegeService implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privilege_id")
    private int privilegeId;

    @Column(name = "privilege_service_name")
    private String privilegeServiceName;

    @Column(name = "privilege_service_display_name")
    private String privilegeServiceDisplayName;

    @Column(name = "status")
    private String status;


    @Column(name = "is_chargeable")
    private int isChargeable;

    public int getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(int privilegeId) {
        this.privilegeId = privilegeId;
    }

    public String getPrivilegeServiceName() {
        return privilegeServiceName;
    }

    public void setPrivilegeServiceName(String privilegeServiceName) {
        this.privilegeServiceName = privilegeServiceName;
    }

    public String getPrivilegeServiceDisplayName() {
        return privilegeServiceDisplayName;
    }

    public void setPrivilegeServiceDisplayName(String privilegeServiceDisplayName) {
        this.privilegeServiceDisplayName = privilegeServiceDisplayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public int getIsChargeable() {
		return isChargeable;
	}

	public void setIsChargeable(int isChargeable) {
		this.isChargeable = isChargeable;
	}

	@Override
	public String toString() {
		return "BeneficiariedPrivilegeService [privilegeId=" + privilegeId + ", privilegeServiceName="
				+ privilegeServiceName + ", privilegeServiceDisplayName=" + privilegeServiceDisplayName + ", status="
				+ status + ", isChargeable=" + isChargeable + "]";
	}


}
