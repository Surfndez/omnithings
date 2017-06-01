package com.asuscloud.iot.utils;

import org.apache.log4j.Logger;

import com.asuscloud.iot.constant.ConfigConstant;
import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TSDBaseEntryUtil {
	private static Logger log = Logger.getLogger(TSDBaseEntryUtil.class);
	// TSDB Query Key
	public final static String KEY_SCHEMA_NAME = "schemaname";
	public final static String KEY_KEY_CONDITIONS = "keyconditions";
	public final static String KEY_QUERY_ATTRIBUTES = "query_attributes";
	public final static String KEY_ENTRIES = "entries";
	public final static String KEY_FUNCS = "funcs";
	
	public final static String CLIENT_VERSION = "1.0.0";
	public final static String OS_VERSION = "tomcat 8.0";
	public final static String CLIENT_TYPE = "web";
			
	public final static String ACTION_QUERY = "Query";
	public final static String ACTION_PUT_ENTRIES = "PutEntries";
	
	public static int putEntries(String sid, String progKey, String serviceName, String body){
		String authorization = AuthorizationUtil.composeAuthorizationHeader(progKey);
		
		int status = -1;
		try {
			/**
			 * Authorization: Authorization Header
			 * X-Omni-Sid：SID
			 * X-Omni-Service：ServiceName 
			 * X-Omni-Action：PutEntries
			 * X-Omni-CV: client version
			 * X-Omni-OV: os version
			 * X-Omni-CT :client type
			 */
			log.trace("TX: " + body);
			HttpResponse<JsonNode> jsonResponse = Unirest.post(ConfigConstant.TS_DBASE+"/tsdbase/entry")
														 .header("Authorization", authorization)
														 .header("X-Omni-Action", ACTION_PUT_ENTRIES)
														 .header("X-Omni-Sid", sid)
														 .header("X-Omni-Service", serviceName)
														 .header("X-Omni-OV", OS_VERSION)
														 .header("X-Omni-CT", CLIENT_TYPE)
														 .body(body).asJson();
			
			Headers headers = jsonResponse.getHeaders();
			
			if(jsonResponse.getStatus() == 200){
				
				if(headers.get("X-Omni-Status") != null && headers.get("X-Omni-Status").size() > 0){
					status = Integer.parseInt(headers.get("X-Omni-Status").get(0));
					log.info("PUT_ENTRIES|"+jsonResponse.getStatus() + "|" + jsonResponse.getStatusText()+"|"+headers.get("X-Omni-Status").get(0)+"|"+headers.get("X-Omni-RequestId").get(0)+"|"+headers.get("X-Omni-Status-Message").get(0));
				}
			}else{
				log.warn("PUT_ENTRIES|"+jsonResponse.getStatus() + "|" + jsonResponse.getStatusText());
			}
		} catch(java.lang.NullPointerException e){
			log.warn("PUT_ENTRIES|"+e.getMessage() + "|" + e.getStackTrace()[0]);
		} catch (UnirestException e) {
			log.warn("PUT_ENTRIES|"+e.getMessage() + "|" + e.getStackTrace()[0]);
		}
		
		return status;
	}
	
	public static String query(String sid , String progKey, String serviceName, String body){
		
		String result = null;
		int status = -1;
		try {
			/**
			 * Authorization: Authorization Header
			 * X-Omni-Sid：SID
			 * X-Omni-Service：ServiceName 
			 * X-Omni-Action：Query
			 * X-Omni-CV: client version
			 * X-Omni-OV: os version
			 * X-Omni-CT :client type
			 */
			log.trace("TX: " + body);
			String authorization = AuthorizationUtil.composeAuthorizationHeader(progKey);		
			HttpResponse<JsonNode> jsonResponse = Unirest.post(ConfigConstant.TS_DBASE+"/tsdbase/entry")
														 .header("Authorization", authorization)
														 .header("X-Omni-Action", ACTION_QUERY)
														 .header("X-Omni-Sid", sid)
														 .header("X-Omni-Service", serviceName)
														 .header("X-Omni-OV", OS_VERSION)
														 .header("X-Omni-CT", CLIENT_TYPE)
														 .body(body).asJson();
			result = jsonResponse.getBody().toString();
			log.trace(jsonResponse.getStatus() + "|" + jsonResponse.getStatusText());
			Headers headers = jsonResponse.getHeaders();
			if(jsonResponse.getStatus() == 200){
				
				if(headers.get("X-Omni-Status") != null && headers.get("X-Omni-Status").size() > 0){
					status = Integer.parseInt(headers.get("X-Omni-Status").get(0));
					log.info("QUERY||"+jsonResponse.getStatus() + "|" + jsonResponse.getStatusText()+"|"+status+"|"+headers.get("X-Omni-RequestId").get(0)+"|"+headers.get("X-Omni-Status-Message").get(0));
				}
			}else{
				log.warn("QUERY|"+jsonResponse.getStatus() + "|" + jsonResponse.getStatusText());
			}
			log.trace("RX: " + result);
		} catch (UnirestException e) {
			log.warn(e.getMessage() + "|" + e.getStackTrace()[0]);
		}

		return result;
	}
}
