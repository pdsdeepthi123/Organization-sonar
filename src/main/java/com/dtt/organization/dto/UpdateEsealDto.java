package com.dtt.organization.dto;

public class UpdateEsealDto {
    private String orgUid;

    private String eSealImage;
    
    private String authorizedLetterForSignatories;

    public String getOrgUid() {
        return orgUid;
    }

    public void setOrgUid(String orgUid) {
        this.orgUid = orgUid;
    }

    public String geteSealImage() {
        return eSealImage;
    }

    public void seteSealImage(String eSealImage) {
        this.eSealImage = eSealImage;
    }


    public String getAuthorizedLetterForSignatories() {
		return authorizedLetterForSignatories;
	}

	public void setAuthorizedLetterForSignatories(String authorizedLetterForSignatories) {
		this.authorizedLetterForSignatories = authorizedLetterForSignatories;
	}

	@Override
	public String toString() {
		return "UpdateEsealDto [orgUid=" + orgUid + ", eSealImage=" + eSealImage + ", authorizedLetterForSignatories="
				+ authorizedLetterForSignatories + "]";
	}
}
