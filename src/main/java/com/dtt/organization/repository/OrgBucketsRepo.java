package com.dtt.organization.repository;


import com.dtt.organization.model.OrgBuckets;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrgBucketsRepo extends JpaRepository<OrgBuckets,Integer> {


    @Query("SELECT o FROM OrgBuckets o WHERE o.id = :id")
    OrgBuckets getBucketDetailsById(@Param("id") int id);

    @Query("SELECT o FROM OrgBuckets o WHERE o.orgBucketConfig.orgId = :ouid")
    List<OrgBuckets> getBucketsListByOuid(@Param("ouid") String ouid);


    @Query("SELECT o FROM OrgBuckets o WHERE o.orgBucketConfig.id = :bucketConfigId")
    List<OrgBuckets> findBucketHistoryListByBucketConfigId(@Param("bucketConfigId") Long bucketConfigId);


    @Query("SELECT o FROM OrgBuckets o WHERE o.bucketId LIKE CONCAT(:bucketId, '-%')")
    List<OrgBuckets> findBucketHistoryListByBucketId(@Param("bucketId") String bucketId);







}
