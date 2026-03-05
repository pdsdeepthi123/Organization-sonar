package com.dtt.organization.repository;



import com.dtt.organization.model.OrgBucketConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrgBucketConfigRepo extends JpaRepository<OrgBucketConfig,Integer> {



    @Query("SELECT o FROM OrgBucketConfig o WHERE o.orgId = :ouid")
    List<OrgBucketConfig> getAllBucketConfigListByOuid(@Param("ouid") String ouid);

    @Query("SELECT o FROM OrgBucketConfig o WHERE o.appId = :appId")
    OrgBucketConfig getBucketsConfigByAppid(@Param("appId") String appId);

    OrgBucketConfig findByid(int id); // optional: rename to findById(int id)

    @Query("SELECT o FROM OrgBucketConfig o WHERE o.orgId = :ouid")
    List<OrgBucketConfig> getBucketConfigListByOuid(@Param("ouid") String ouid); // duplicate, can be removed

    @Query("SELECT o FROM OrgBucketConfig o")
    List<OrgBucketConfig> getOrgBucketConfigList();



}
