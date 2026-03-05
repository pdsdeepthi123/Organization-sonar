package com.dtt.organization.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "org_buckets")
public class OrgBuckets {

    @Column(name="id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "bucket_id")
    private String bucketId;

    @ManyToOne
    @JoinColumn(name = "bucket_configuration_id")
    private OrgBucketConfig orgBucketConfig;


    @Column(name = "total_digital_signatures")
    private int totalDS;

    @Column(name="total_eseal")
    private int totalEDS;

    @Column(name="created_on")
    private LocalDateTime createdOn;

    @Column(name="updated_on")
    private LocalDateTime updatedOn;

    @Column(name="status")
    private String status;

    @Column(name="last_signatory_id")
    private String closedBy;

    @Column(name= "sponsor_id")
    private String sponsorId;

    @Column(name="payment_recieved_on")
    private LocalDateTime closedOn;

    @Column(name = "payment_recieved")
    private boolean paymentRecieved;


    @Column(name="additionalDSRemaining")
    private int remainingDSAfterPayment;

    @Column(name = "additionalEDSRemaining")
    private int remainingEDSAfterPayment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public OrgBucketConfig getOrgBucketConfig() {
        return orgBucketConfig;
    }

    public void setOrgBucketConfig(OrgBucketConfig orgBucketConfig) {
        this.orgBucketConfig = orgBucketConfig;
    }

    public int getTotalDS() {
        return totalDS;
    }

    public void setTotalDS(int totalDS) {
        this.totalDS = totalDS;
    }

    public int getTotalEDS() {
        return totalEDS;
    }

    public void setTotalEDS(int totalEDS) {
        this.totalEDS = totalEDS;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public void setClosedOn(LocalDateTime closedOn) {
        this.closedOn = closedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    public String getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(String sponsorId) {
        this.sponsorId = sponsorId;
    }


    public LocalDateTime getClosedOn() {
        return closedOn;
    }

    public boolean isPaymentRecieved() {
        return paymentRecieved;
    }

    public void setPaymentRecieved(boolean paymentRecieved) {
        this.paymentRecieved = paymentRecieved;
    }

    public int getRemainingDSAfterPayment() {
        return remainingDSAfterPayment;
    }

    public void setRemainingDSAfterPayment(int remainingDSAfterPayment) {
        this.remainingDSAfterPayment = remainingDSAfterPayment;
    }

    public int getRemainingEDSAfterPayment() {
        return remainingEDSAfterPayment;
    }

    public void setRemainingEDSAfterPayment(int remainingEDSAfterPayment) {
        this.remainingEDSAfterPayment = remainingEDSAfterPayment;
    }

    @Override
    public String toString() {
        return "OrgBuckets{" +
                "id=" + id +
                ", bucketId='" + bucketId + '\'' +
                ", orgBucketConfig=" + orgBucketConfig +
                ", totalDS=" + totalDS +
                ", totalEDS=" + totalEDS +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", status='" + status + '\'' +
                ", closedBy='" + closedBy + '\'' +
                ", sponsorId='" + sponsorId + '\'' +
                ", closedOn=" + closedOn +
                ", paymentRecieved=" + paymentRecieved +
                ", remainingDSAfterPayment=" + remainingDSAfterPayment +
                ", remainingEDSAfterPayment=" + remainingEDSAfterPayment +
                '}';
    }
}
