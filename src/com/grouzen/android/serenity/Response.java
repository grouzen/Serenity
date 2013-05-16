package com.grouzen.android.serenity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Bundle;
import android.util.Log;

public class Response {

	private final static String TAG = Response.class.getCanonicalName();
	
	private Request request;
	
	private byte[] data;
	
	private Exception exception;
	
	public Response(Request request) {
		this.request = request;
	}
	
	private byte[] readData(HttpResponse response) throws IOException {
		byte[] data = null;
		
		if(response != null) {
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				data = EntityUtils.toByteArray(response.getEntity());
			}
		}
		
		return data;
	}
	
	/*
	private String dataFromResponse(HttpResponse response) throws IllegalStateException, IOException {
		if(response != null) {
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader bf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuilder dataBuilder = new StringBuilder();
				String bfLine = "";
					
				while((bfLine = bf.readLine()) != null) {
					dataBuilder.append(bfLine).append('\n');					
				}
				Log.i(TAG, dataBuilder.toString());
				return dataBuilder.toString();
			}
		}
		
		Log.i(TAG, "dataFromResponse returns null");
		return null;
	}
	*/
	
	public JSONObject toJSONObject() throws JSONException {
		return new JSONObject(new String(data));
	}
	
	public JSONArray toJSONArray() throws JSONException {
		return new JSONArray(new String(data));
	}
	
	public Exception getException() {
		return exception;
	}
	
	public Response fromHttpConnection() {
		Bundle parameters = request.getParameters();
		HttpResponse response;
		
		try {
			response = request.getConnection().send(parameters);
			data = readData(response);
		} catch (ClientProtocolException e) {
			exception = e;
		} catch (IOException e) {
			exception = e;
		} catch(IllegalStateException e) {
			exception = e;
		}
		
		
		return this;
	}
	
	public Request getRequest() {
		return request;
	}
	
}
