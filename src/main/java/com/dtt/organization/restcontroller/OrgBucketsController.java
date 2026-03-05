package com.dtt.organization.restcontroller;


import com.dtt.organization.constant.ApiResponses;

import com.dtt.organization.dto.OrgBucketConfigDTO;
import com.dtt.organization.dto.OrgClientAppConfigDto;
import com.dtt.organization.service.iface.OrgBucketsIface;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrgBucketsController {


    private final OrgBucketsIface orgBucketsIface;


    public OrgBucketsController(OrgBucketsIface orgBucketsIface) {
        this.orgBucketsIface = orgBucketsIface;
    }

    @GetMapping("/api/get/bucket-details/id")
    public ApiResponses getBucketDetailsById(@RequestParam int id){

        return orgBucketsIface.getBucketDetailsById(id);
    }

    //1
    @GetMapping("/api/get/bucket-config-list/by/ouid")
    public ApiResponses getBucketsListByOuid(@RequestParam String ouid){

        return orgBucketsIface.getBucketsListByOuid(ouid);
    }

    @GetMapping("/api/get/all/bucket-config-list/by/ouid")
    public ApiResponses getAllBucketConfigListByOuid(@RequestParam String ouid){

        return orgBucketsIface.getAllBucketConfigListByOuid(ouid);
    }


    @GetMapping("/api/get/bucket-config/by/app-id")
    public ApiResponses getBucketConfigByAppid(@RequestParam String appId){

        return orgBucketsIface.getBucketConfigByAppid(appId);
    }

    @GetMapping("/api/get/bucket-history/bucket-id")
    public ApiResponses getBucketHistoryByBucketId(@RequestParam String bucketId){

        return orgBucketsIface.getBucketHistoryByBucketId(bucketId);

    }

    @PostMapping("api/post/add/org-bucket-config")
    public ApiResponses addOrgBucketConfig(@RequestBody OrgBucketConfigDTO orgBucketConfigDTO){
        return orgBucketsIface.addOrgBucketConfig(orgBucketConfigDTO);
    }

    @PostMapping("/api/update/bucket-config")
    public ApiResponses updateBucketConfigById(@RequestBody OrgBucketConfigDTO orgBucketConfigDTO){
        return orgBucketsIface.updateBucketConfigById(orgBucketConfigDTO);
    }

    @GetMapping("/api/get/bucket-history/by/bucket-config-id")
    public ApiResponses getBucketHistoryByBucketConfigId(@RequestParam int bucketConfigId){
        return  orgBucketsIface.getBucketHistoryByBucketConfigId(bucketConfigId);
    }

    @GetMapping("/api/get/bucket-config/by/id")
    public ApiResponses getBucketConfigByid(@RequestParam int id){

        return orgBucketsIface.getBucketConfigByid(id);
    }

    @PostMapping("api/post/add/orgClientAppConfig")
    public ApiResponses addOrgClientAppConfig(@RequestBody OrgClientAppConfigDto orgClientAppConfigDto){
        return orgBucketsIface.addOrgClientAppConfig(orgClientAppConfigDto);
    }

    @GetMapping("/api/enable/disable/sponsorship/by/id")
    public ApiResponses enableDisableSponsorShip(@RequestParam int id){

        return orgBucketsIface.enableDisableSponsorship(id);
    }
}
