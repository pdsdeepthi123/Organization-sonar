package com.dtt.organization.model;


import jakarta.persistence.*;

@Entity
@Table(name = "organization_pricing_slab_definitions")
public class OrganizationPricingSlabDefinitions
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "discount")
    private double discount;

    @Column(name = "volume_range_from")
    private double volumeRangeFrom;

    @Column(name = "volume_range_to")
    private double volumeRangeTo;



    @Column(name = "organization_id")
    private String organizationId;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getVolumeRangeFrom() {
        return volumeRangeFrom;
    }

    public void setVolumeRangeFrom(double volumeRangeFrom) {
        this.volumeRangeFrom = volumeRangeFrom;
    }

    public double getVolumeRangeTo() {
        return volumeRangeTo;
    }

    public void setVolumeRangeTo(double volumeRangeTo) {
        this.volumeRangeTo = volumeRangeTo;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }


}
