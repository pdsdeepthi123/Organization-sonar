package com.dtt.organization.service.impl;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.service.iface.EGPVerifyVendorIFace;
import com.dtt.organization.util.AppUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EGPVerifyVendorImpl implements EGPVerifyVendorIFace {
	
    static final String CLASS = "EGPVerifyVendorImpl";
    Logger logger = LoggerFactory.getLogger(EGPVerifyVendorImpl.class);
    
    @Value("${verify.egp.vendorId}")
    private String verifyEGPVendorURL;
    
    @Value("${egp.authorization.username}")
    private String eGPUserName;
    
    @Value("${egp.authorization.password}")
    private String eGPPasswrod;
    
    @Value("${egp.org.id}")
    private String egpOrgId;

    private final RestTemplate restTemplate;
    private final MessageSource messageSource;

    public EGPVerifyVendorImpl(RestTemplate restTemplate, MessageSource messageSource) {
        this.restTemplate = restTemplate;
        this.messageSource = messageSource;
    }

    Locale locale = LocaleContextHolder.getLocale();

    public ApiResponses verifyByEgpForVendor(String vendorId, String orgId) {
        ResponseEntity<Object> res = null;

        try {
            logger.info("EGPVerifyVendorImplverifyByEgpForVendor vendorID , organizationID {} , {}", vendorId, orgId);
            if (egpOrgId.equals(orgId)) {
                ObjectMapper objectMapper = new ObjectMapper();
                String reqBody = getVerifyVendorEmailRequest(vendorId);
                String eGPURL = verifyEGPVendorURL + "api/users/validate";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBasicAuth(eGPUserName, eGPPasswrod);
                HttpEntity<Object> requestEntity = new HttpEntity<>(reqBody, headers);
                res = restTemplate.exchange(eGPURL, HttpMethod.POST, requestEntity, Object.class);
                logger.info("EGPVerifyVendorImpl verifyByEgpForVendor egp api response {} ", res);
                if (res.getStatusCode() != HttpStatus.OK && res.getStatusCode() != HttpStatus.CREATED) {
                    return AppUtil.createApiResponse(
                            false,
                            messageSource.getMessage("api.error.vendor.not.found", null, locale),
                            null
                    );
                }else {
                    JsonNode jsonNode = objectMapper.valueToTree(res.getBody());
                    if(jsonNode.get("account_exists").asBoolean() && jsonNode.get("is_provider").asBoolean()) {
                    	return AppUtil.createApiResponse(true, messageSource.getMessage("api.response.vendor.verified.successfully", null, locale), null);
                    }else {
                    	return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.invalid.vendor", null, locale), res.getBody());
                    }

                }

            } else {
                return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.vendor.not.found", null, locale), null);
            }

        } catch (Exception var15) {
            logger.info("{}", var15.getMessage());
            return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.something.went.wrong.please.contact.admin", null, locale), null);
        }
    }

    public String getVerifyVendorEmailRequest(String email) {
        return "{\"email\":\"" + email + "\"}";
    }


}
