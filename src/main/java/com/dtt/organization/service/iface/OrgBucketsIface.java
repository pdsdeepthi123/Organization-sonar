package com.dtt.organization.service.iface;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.OrgBucketConfigDTO;
import com.dtt.organization.dto.OrgClientAppConfigDto;

public interface OrgBucketsIface {



    ApiResponses getBucketDetailsById(int id);

    ApiResponses getAllBucketConfigListByOuid(String ouid);


    ApiResponses getBucketConfigByAppid(String appId);

    ApiResponses getBucketsListByOuid(String ouid);

    ApiResponses getBucketHistoryByBucketId(String bucketId);

    ApiResponses addOrgBucketConfig(OrgBucketConfigDTO orgBucketConfigDTO);

    ApiResponses updateBucketConfigById(OrgBucketConfigDTO orgBucketConfigDTO);

    ApiResponses getBucketHistoryByBucketConfigId(int bucketConfigId);

    ApiResponses getBucketConfigByid(int id);


    ApiResponses addOrgClientAppConfig(OrgClientAppConfigDto orgClientAppConfigDto);

    ApiResponses enableDisableSponsorship(int id);
}
