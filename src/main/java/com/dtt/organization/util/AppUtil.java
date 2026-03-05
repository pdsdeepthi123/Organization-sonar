package com.dtt.organization.util;
import com.dtt.organization.constant.ApiResponses;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class AppUtil {


    private AppUtil() {
        throw new UnsupportedOperationException("Utility class — cannot be instantiated");
    }
	static String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    static String dateFormat = "yyyy-MM-dd HH:mm:ss";


	public static String generatePKIKeyId() {
		String alphaNumeric = upperAlphabet;
		return genRandomNumber(alphaNumeric);
	}

    private static final Random RANDOM = new Random();

    private static String genRandomNumber(String alphaNumeric) {
        StringBuilder sb = new StringBuilder();

        int length = 10;

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(alphaNumeric.length());
            sb.append(alphaNumeric.charAt(index));
        }

        return sb.toString();
    }



    public static Date getCurrentDate() {
        return new Date();
    }





    public static ApiResponses createApiResponse(boolean success, String msg, Object object) {
        ApiResponses apiResponse = new ApiResponses();
        apiResponse.setMessage(msg);
        apiResponse.setResult(object);
        apiResponse.setSuccess(success);
        return apiResponse;

    }

    public static Date getTimeStamp() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.parse(new Timestamp(System.currentTimeMillis()).toString());
	}
    
    public static Date getTimeStamp(String date) throws ParseException {
		SimpleDateFormat f = new SimpleDateFormat(dateFormat);

		return f.parse(date);
	}
    

	public static String getDate(){
    	SimpleDateFormat smpdate = new SimpleDateFormat(dateFormat);
		Date date = new Date();
		return smpdate.format(date);
    }

	
	public static String getUUId() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
