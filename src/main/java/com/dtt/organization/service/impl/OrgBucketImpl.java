package com.dtt.organization.service.impl;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.OrgBucketConfigDTO;

import com.dtt.organization.dto.OrgClientAppConfigDto;
import com.dtt.organization.exception.ExceptionHandlerUtil;
import com.dtt.organization.model.OrgBucketConfig;
import com.dtt.organization.model.OrgBuckets;

import com.dtt.organization.model.OrgClientAppConfig;
import com.dtt.organization.repository.OrgBucketConfigRepo;
import com.dtt.organization.repository.OrgBucketsRepo;

import com.dtt.organization.repository.OrgClientAppConfigRepo;
import com.dtt.organization.service.iface.OrgBucketsIface;
import com.dtt.organization.util.AppUtil;

import org.hibernate.PessimisticLockException;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.dtt.organization.constant.Constant.*;

@Service
public class OrgBucketImpl implements OrgBucketsIface {

    private final OrgBucketsRepo orgBucketsRepo;
    private final OrgBucketConfigRepo orgBucketConfigRepo;
    private final OrgClientAppConfigRepo orgClientAppConfigRepo;
    private final ExceptionHandlerUtil exceptionHandlerUtil;

    public OrgBucketImpl(
            OrgBucketsRepo orgBucketsRepo,
            OrgBucketConfigRepo orgBucketConfigRepo,
            OrgClientAppConfigRepo orgClientAppConfigRepo,
            ExceptionHandlerUtil exceptionHandlerUtil) {

        this.orgBucketsRepo = orgBucketsRepo;
        this.orgBucketConfigRepo = orgBucketConfigRepo;
        this.orgClientAppConfigRepo = orgClientAppConfigRepo;
        this.exceptionHandlerUtil = exceptionHandlerUtil;
    }


   Logger logger= LoggerFactory.getLogger(OrgBucketImpl.class);

    public ApiResponses getBucketDetailsById(int id) {

        try {
            if (id == 0) {
                return exceptionHandlerUtil.createErrorResponse(
                        "api.error.id.cant.null");
            }

            OrgBuckets bucket = orgBucketsRepo.getBucketDetailsById(id);

            if (bucket == null) {
                return exceptionHandlerUtil.createErrorResponse(
                        "api.error.fetched.bucket.id.cant.null");
            }

            return exceptionHandlerUtil.createSuccessResponse(
                    "api.response.bucket.fetch.success",
                    bucket);

        } catch (Exception e) {
            logger.error("{} - No details found with that id: {}", this.getClass().getSimpleName(), e.getMessage(), e);
            return exceptionHandlerUtil.createErrorResponse(
                    SOMETHING_WENT_WRONG);
        }
    }


    @Override
    public ApiResponses getAllBucketConfigListByOuid(String ouid) {
        try{
            if(ouid!=null){

                return exceptionHandlerUtil.createSuccessResponse(
                        API_RESPONSE_FETCHES_SUCCESSFULLY,
                        orgBucketConfigRepo.getAllBucketConfigListByOuid(ouid));

            }
        }catch (Exception e){
            logger.error("{} - Exception : {}", this.getClass().getSimpleName(), e.getMessage(), e);
            return exceptionHandlerUtil.createErrorResponse(
                    SOMETHING_WENT_WRONG);
        }

        return null;
    }

    @Override
    public ApiResponses getBucketConfigByAppid(String appId) {
        try{
            if(appId!=null){
                return exceptionHandlerUtil.createSuccessResponse(API_RESPONSE_FETCHES_SUCCESSFULLY,orgBucketConfigRepo.getBucketsConfigByAppid(appId));

            }
        }catch (Exception e){
            logger.error("{} - No Bucket config with the app id: {}", this.getClass().getSimpleName(), e.getMessage(), e);
            return exceptionHandlerUtil.createErrorResponse(
                    SOMETHING_WENT_WRONG);
        }

        return null;
    }

    @Override
    public ApiResponses getBucketsListByOuid(String ouid) {
            try{

                if(!ouid.equals("null")){
                    logger.info("Inside if");
                    List<OrgBucketConfig> res = orgBucketConfigRepo.getBucketConfigListByOuid(ouid);
                    return exceptionHandlerUtil.createSuccessResponse(API_RESPONSE_FETCHES_SUCCESSFULLY,res);
                }

                else{
                    logger.info("Inside else");
                    List<OrgBucketConfig> res1 = orgBucketConfigRepo.getOrgBucketConfigList();
                    return exceptionHandlerUtil.createSuccessResponse(API_RESPONSE_FETCHES_SUCCESSFULLY,res1);
                }
            }catch (Exception e){
                logger.error("{} - Bucket list empty: {}", this.getClass().getSimpleName(), e.getMessage(), e);
                return exceptionHandlerUtil.createErrorResponse(
                        SOMETHING_WENT_WRONG);
        }


    }


    @Override
    public ApiResponses getBucketHistoryByBucketId(String bucketId) {
        logger.info("BucketId:{}",bucketId);
        try{
            logger.info("Inside try");

            if(bucketId ==null || bucketId.isEmpty()){
                return exceptionHandlerUtil.createErrorResponse("api.error.bucket.id.must.be.null");
            }
            else{
                logger.info("Inside else");
                List<OrgBuckets> orgBuckets = orgBucketsRepo.findBucketHistoryListByBucketId(bucketId);
                return exceptionHandlerUtil.createSuccessResponse("api.response.fetched.bucket.id",orgBuckets);

            }

        }catch (Exception e){
            logger.error("{} - Bucket History empty: {}", this.getClass().getSimpleName(), e.getMessage(), e);
            return exceptionHandlerUtil.createErrorResponse(
                    SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public ApiResponses addOrgBucketConfig(OrgBucketConfigDTO orgBucketConfigDTO) {
        try {
        	logger.info("orgBucketConfigDTO{} ",orgBucketConfigDTO);
            if(orgBucketConfigDTO.getAppId()==null ||  orgBucketConfigDTO.getAppId().equals("") ){
                return exceptionHandlerUtil.createErrorResponse("api.error.app.id.cant.null");
            }

            Optional<OrgBucketConfig> orgBucketConfig = Optional.ofNullable(orgBucketConfigRepo.getBucketsConfigByAppid(orgBucketConfigDTO.getAppId()));

            //CHECK DUPLICATE IN org_bucket_config IF PRESENT THROW ERROR
            if(orgBucketConfig.isPresent()){
                return exceptionHandlerUtil.createErrorResponse("api.error.app.id.already.exists");

            }

            //CHECK DUPLICATE IN org_client_app_config IF Duplicate just active that row status
            Optional<OrgClientAppConfig> orgClientAppConfig = Optional.ofNullable(orgClientAppConfigRepo.getByAppidAndConfigValue(orgBucketConfigDTO.getAppId()));

            if(orgClientAppConfig.isPresent()){
                orgClientAppConfig.get().setStatus(ACTIVE);
                orgClientAppConfig.get().setCreatedOn(AppUtil.getDate());
                orgClientAppConfig.get().setUpdatedOn(AppUtil.getDate());
                orgClientAppConfigRepo.save(orgClientAppConfig.get());
            }
            else {

                OrgClientAppConfig orgClientAppConfigToStore = new OrgClientAppConfig();

                orgClientAppConfigToStore.setAppId(orgBucketConfigDTO.getAppId());
                orgClientAppConfigToStore.setOrgId(orgBucketConfigDTO.getOrgId());
                orgClientAppConfigToStore.setConfigValue("SPONSOR_ID_AND_BUCKET_ID");
                orgClientAppConfigToStore.setStatus(ACTIVE);
                orgClientAppConfigToStore.setCreatedOn(AppUtil.getDate());
                orgClientAppConfigToStore.setUpdatedOn(AppUtil.getDate());

                orgClientAppConfigRepo.save(orgClientAppConfigToStore);
            }



            OrgBucketConfig orgBucketConfig1 = new OrgBucketConfig();

            orgBucketConfig1.setOrgId(orgBucketConfigDTO.getOrgId());
            orgBucketConfig1.setAppId(orgBucketConfigDTO.getAppId());
            orgBucketConfig1.setLabel(orgBucketConfigDTO.getLabel());
            orgBucketConfig1.setBucketClosingMessage(orgBucketConfigDTO.getBucketClosingMessage());
            orgBucketConfig1.setCreatedOn(AppUtil.getDate());
            orgBucketConfig1.setUpdatedOn(AppUtil.getDate());
            orgBucketConfig1.setStatus("ACTIVE");
            orgBucketConfig1.setAdditionalDs(orgBucketConfigDTO.getAdditionalDs());
            orgBucketConfig1.setAdditionalEds(orgBucketConfigDTO.getAdditionalEds());
            orgBucketConfig1.setOrgName(orgBucketConfigDTO.getOrgName());

            orgBucketConfigRepo.save(orgBucketConfig1);

            return exceptionHandlerUtil.createSuccessResponse("api.response.Organisation.bucket.configuration.saved.successfully",null);

        } catch (JDBCConnectionException | ConstraintViolationException | DataException | LockAcquisitionException
                | PessimisticLockException | QueryTimeoutException | SQLGrammarException | GenericJDBCException ex) {
            logger.error("{} - Unknown Exception occurred : {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return exceptionHandlerUtil.createErrorResponse(
                    DATABASE_EXCEPTION);

        } catch (Exception ex) {
            logger.error("{} - Exception occurred while adding the bucket: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return exceptionHandlerUtil.createErrorResponse(
                    SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public ApiResponses updateBucketConfigById(OrgBucketConfigDTO orgBucketConfigDTO) {
        try{

            if(orgBucketConfigDTO.getId()==0 ){
                return exceptionHandlerUtil.createErrorResponse("api.error.id.cant.be.null");
            }

            OrgBucketConfig orgBucketConfig = orgBucketConfigRepo.findByid(orgBucketConfigDTO.getId());

            if(orgBucketConfig==null){
                return exceptionHandlerUtil.createErrorResponse("api.error.record.not.found");
            }
            else {

                OrgClientAppConfig orgClientAppConfig = new OrgClientAppConfig();

                orgBucketConfig.setOrgId(orgBucketConfigDTO.getOrgId());
                orgBucketConfig.setOrgName(orgBucketConfigDTO.getOrgName());
                orgBucketConfig.setAppId(orgBucketConfigDTO.getAppId());
                orgBucketConfig.setLabel(orgBucketConfigDTO.getLabel());
                orgBucketConfig.setBucketClosingMessage(orgBucketConfigDTO.getBucketClosingMessage());
                orgBucketConfig.setUpdatedOn(AppUtil.getDate());
                orgBucketConfig.setAdditionalDs(orgBucketConfigDTO.getAdditionalDs());
                orgBucketConfig.setAdditionalEds(orgBucketConfigDTO.getAdditionalEds());
                orgBucketConfig.setStatus(orgBucketConfigDTO.getStatus());
                orgClientAppConfig.setStatus(orgBucketConfigDTO.getStatus());
                orgBucketConfigRepo.save(orgBucketConfig);
                orgClientAppConfigRepo.save(orgClientAppConfig);


               return exceptionHandlerUtil.createSuccessResponse("api.response.record.updated",null);
            }


        }catch (Exception e){
            logger.error("{} - cannot update: {}", this.getClass().getSimpleName(), e.getMessage(), e);
            return exceptionHandlerUtil.createErrorResponse(
                    SOMETHING_WENT_WRONG);
        }


    }

    @Override
    public ApiResponses getBucketHistoryByBucketConfigId(int bucketConfigId) {
        try{
            if(bucketConfigId==0){
               return exceptionHandlerUtil.createErrorResponse("api.error.fetched.bucket.id.cant.null");
            }
            else {
                List<OrgBuckets> orgBucketsHistory = orgBucketsRepo.findBucketHistoryListByBucketConfigId((long) bucketConfigId);

                return exceptionHandlerUtil.createSuccessResponse("api.response.fetches.successfully",orgBucketsHistory);
            }
        }catch(Exception e){
            logger.error("{} - Exception occurred with bucketid: {}", this.getClass().getSimpleName(), e.getMessage(), e);
            return exceptionHandlerUtil.createErrorResponse(
                    SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public ApiResponses getBucketConfigByid(int id) {

        try{
            if(id==0){
                return exceptionHandlerUtil.createErrorResponse("api.error.id.cant.null");
            }else {

                OrgBucketConfig res = orgBucketConfigRepo.findByid(id);

                if(res==null){
                    return exceptionHandlerUtil.createErrorResponse("api.error.record.not.found");
                }else {
                    return exceptionHandlerUtil.createSuccessResponse("api.response.fetches.successfully",res);
                }

            }
        }catch (JDBCConnectionException | ConstraintViolationException | DataException | LockAcquisitionException
                | PessimisticLockException | QueryTimeoutException | SQLGrammarException | GenericJDBCException ex) {
            logger.error("{} - Exception occurred id: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return exceptionHandlerUtil.createErrorResponse(
                    "api.error.database");

        } catch (Exception ex) {
            logger.error("{} - Exception occurred invalid: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return exceptionHandlerUtil.createErrorResponse(
                    API_ERROR_SOMETHING_WENT_WRONG_PLEASE_TRY_AFTER_SOMETIME);
        }
    }

    @Override
    public ApiResponses addOrgClientAppConfig(OrgClientAppConfigDto orgClientAppConfigDto) {

        try{

            if(orgClientAppConfigDto.getOrgId()==null || orgClientAppConfigDto.getAppId()==null || orgClientAppConfigDto.getConfigValue()==null){
              return exceptionHandlerUtil.createErrorResponse("api.error.app.id.and.organization.id.cant.be.null");
            }



            if(orgClientAppConfigRepo.checkDuplicateByAppid(orgClientAppConfigDto.getAppId())!=null){
                return exceptionHandlerUtil.createErrorResponse("api.error.duplicate.record.found");
             }

            OrgClientAppConfig orgClientAppConfig = new OrgClientAppConfig();

            orgClientAppConfig.setId(orgClientAppConfigDto.getId());
            orgClientAppConfig.setAppId(orgClientAppConfigDto.getAppId());
            orgClientAppConfig.setOrgId(orgClientAppConfigDto.getOrgId());
            orgClientAppConfig.setConfigValue(orgClientAppConfigDto.getConfigValue());
            orgClientAppConfig.setStatus("ACTIVE");
            orgClientAppConfig.setCreatedOn(AppUtil.getDate());
            orgClientAppConfig.setUpdatedOn(AppUtil.getDate());

            orgClientAppConfigRepo.save(orgClientAppConfig);
return exceptionHandlerUtil.createSuccessResponse("api.response.record.saved",null);
        }catch (JDBCConnectionException | ConstraintViolationException | DataException | LockAcquisitionException
                | PessimisticLockException | QueryTimeoutException | SQLGrammarException | GenericJDBCException ex) {
            logger.error("{} - Exception occurred: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return exceptionHandlerUtil.createErrorResponse(
                    "api.error.database");

        } catch (Exception ex) {
            logger.error("{} - Exception occurred: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return exceptionHandlerUtil.createErrorResponse(
                    "api.error.something.went.wrong.please.try.after.sometime");
        }
    }

    @Override
    public ApiResponses enableDisableSponsorship(int id) {
        try {
            if (id == 0) {
                return AppUtil.createApiResponse(false, "id cannot be zero", null);
            }

            // 1. Fetch the Optional first to safely check if the data exists
            Optional<OrgClientAppConfig> optionalConfig = orgClientAppConfigRepo.findById(id);

            if (optionalConfig.isEmpty()) {
                return AppUtil.createApiResponse(false, "Record not found", null);
            }

            // 2. Safely get the value and rename the variable from 'record' to 'clientAppConfig'
            OrgClientAppConfig clientAppConfig = optionalConfig.get();

            // 3. Flipped the .equals() check to prevent a NullPointerException if getStatus() is ever null
            if ("INACTIVE".equals(clientAppConfig.getStatus())) {
                orgClientAppConfigRepo.changeStatusToActiveById(id);
                return AppUtil.createApiResponse(true, "Activated Successfully", null);
            }

            orgClientAppConfigRepo.changeStatusToInactiveById(id);
            return AppUtil.createApiResponse(true, "Deactivated Successfully", null);

        } catch (Exception ex) {
            // Passed 'ex' as the second argument so you don't lose the actual stack trace in your logs
            logger.error("Error in enableDisableSponsorship: {}", ex.getMessage(), ex);
            return exceptionHandlerUtil.createErrorResponse(
                    "api.error.something.went.wrong.please.try.after.sometime");
        }
    }



}
