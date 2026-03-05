package com.dtt.organization.repository;


import com.dtt.organization.model.OrgClientAppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface OrgClientAppConfigRepo  extends JpaRepository<OrgClientAppConfig,Integer> {


    @Query(value = "select * from org_client_app_config where app_id=?1 AND config_value='SPONSOR_ID_AND_BUCKET_ID'",nativeQuery = true )
    OrgClientAppConfig getByAppidAndConfigValue(String appId);


    @Query(value = "select * from org_client_app_config where app_id=?1",nativeQuery = true )
    OrgClientAppConfig checkDuplicateByAppid(String appId);

    @Modifying
    @Transactional
    @Query(value = "update org_client_app_config set status='ACTIVE' where id=?1",nativeQuery = true)
    int changeStatusToActiveById(int id);

    @Modifying
    @Transactional
    @Query(value = "update org_client_app_config set status='INACTIVE' where id=?1",nativeQuery = true)
    int changeStatusToInactiveById(int id);


}
