package com.dtt.organization.dto;


import java.util.List;

public class OrgPrepetryStatusDto {

    private List<String> bulkSignerList;

    private List<String> bulkSignerEsealList;

    public List<String> getBulkSignerList() {
        return bulkSignerList;
    }

    public void setBulkSignerList(List<String> bulkSignerList) {
        this.bulkSignerList = bulkSignerList;
    }

    public List<String> getBulkSignerEsealList() {
        return bulkSignerEsealList;
    }

    public void setBulkSignerEsealList(List<String> bulkSignerEsealList) {
        this.bulkSignerEsealList = bulkSignerEsealList;
    }

    @Override
    public String toString() {
        return "OrgPrepetryStatusDto{" +
                "bulkSignerList=" + bulkSignerList +
                ", bulkSignerEsealList=" + bulkSignerEsealList +
                '}';
    }
}
