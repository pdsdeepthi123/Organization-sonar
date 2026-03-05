package com.dtt.organization.dto;

public class CertReqDto {
    private boolean walletIssuer;
    private String identifier ;
    private String certSubject ;
    private String country;
    private String certProcedure;

    public boolean isWalletIssuer() {
        return walletIssuer;
    }

    public void setWalletIssuer(boolean walletIssuer) {
        this.walletIssuer = walletIssuer;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCertSubject() {
        return certSubject;
    }

    public void setCertSubject(String certSubject) {
        this.certSubject = certSubject;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCertProcedure() {
        return certProcedure;
    }

    public void setCertProcedure(String certProcedure) {
        this.certProcedure = certProcedure;
    }
}
