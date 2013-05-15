package com.grouzen.android.serenity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONTokener;

import android.os.Bundle;

public class Response {

	private Request request;
	
	private String data;
	
	private Exception exception;
	
	public Response(Request request) {
		this.request = request;
	}
	
	private String dataFromResponse(HttpResponse response) throws IllegalStateException, IOException {
		if(response != null) {
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader bf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuilder dataBuilder = new StringBuilder();
				String bfLine = "";
					
				while((bfLine = bf.readLine()) != null) {
					dataBuilder.append(bfLine).append('\n');					
				}
					
				return dataBuilder.toString();
			}
		}
		
		return null;
	}
	
	public Exception getException() {
		return exception;
	}
	
	public Response fromHttpConnection() {
		Bundle parameters = request.getParameters();
		HttpResponse response;
		
		try {
			response = request.getConnection().send(parameters);
			data = dataFromResponse(response);
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
