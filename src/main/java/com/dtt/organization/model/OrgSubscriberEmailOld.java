package com.dtt.organization.model;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "org_subscriber_email_old")
public class OrgSubscriberEmailOld implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_uid", nullable = false, length = 36)
    private String organizationUid;

    @Column(name = "email", length = 100)
    private String email;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationUid() {
        return organizationUid;
    }

    public void setOrganizationUid(String organizationUid) {
        this.organizationUid = organizationUid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
