package com.dtt.organization.util;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.dtt.organization.dto.LogModelDTO;
import com.dtt.organization.request.entity.LogModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ug.daes.DAESService;
import ug.daes.PKICoreServiceException;
import ug.daes.Result;


public class NativeUtils {

	private NativeUtils() {
		throw new UnsupportedOperationException("Utility class cannot be instantiated");
	}

	static UUID uuid;


	public static String getUUId() {
		uuid = UUID.randomUUID();
		return uuid.toString();
	}


	public static Date getTimeStamp() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return  format.parse(new Timestamp(System.currentTimeMillis()).toString());

	}



	public static String getTimeStampString(){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return f.format(new Date());
	}




	public static LogModel getLogModel(LogModelDTO logModelDTO) throws JsonProcessingException, PKICoreServiceException {
		LogModel logModel = new LogModel();
		logModel.setIdentifier(logModelDTO.getIdentifier());
		logModel.setCorrelationID(logModelDTO.getCorrelationID());
		logModel.setTransactionID(logModelDTO.getTransactionID());
		logModel.setSubTransactionID(logModelDTO.getSubTransactionID());
		logModel.setTimestamp(logModelDTO.getTimestamp());
		logModel.setStartTime(logModelDTO.getStartTime());
		logModel.setEndTime(logModelDTO.getEndTime());
		logModel.setGeoLocation(logModelDTO.getGeoLocation());
		logModel.setCallStack(logModelDTO.getCallStack());
		logModel.setServiceName(logModelDTO.getServiceName());
		logModel.setTransactionType(logModelDTO.getTransactionType());
		logModel.setTransactionSubType(logModelDTO.getTransactionSubType());
		logModel.setLogMessageType(logModelDTO.getLogMessageType());
		logModel.setLogMessage(logModelDTO.getLogMessage());
		logModel.setServiceProviderName(logModelDTO.getServiceProviderName());
		logModel.setServiceProviderAppName(logModelDTO.getServiceProviderAppName());
		logModel.setSignatureType(logModelDTO.getSignatureType());
		logModel.seteSealUsed(logModelDTO.iseSealUsed());
		logModel.setChecksum(logModelDTO.getChecksum());

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(logModel);

		Result checksumResult = DAESService.addChecksumToTransaction(json);

		String push = new String(checksumResult.getResponse());
		return objectMapper.readValue(push, LogModel.class);
	}

	public static String getDate(String date) {
		String[] dates = date.split(" ");
		return dates[0];
	}
}
