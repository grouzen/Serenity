package com.grouzen.android.serenity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;

public class HttpConnection {
	
	private HttpConnectionMethod method;
	
	private HttpClient client;
	
	private String url;
	
	public HttpConnection(String url, HttpConnectionMethod method) {
		this.method = method;
		this.url = url;
		this.client = new DefaultHttpClient();
	}
	
	public HttpConnection(String url) {
		this(url, HttpConnectionMethod.POST);
	}
	
	public HttpConnection() {
		this(null, HttpConnectionMethod.POST);
	}
	
	public void setMethod(HttpConnectionMethod method) {
		this.method = method;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public HttpResponse send(Bundle params) 
			throws ClientProtocolException, IOException {
		switch(method) {
		case POST:
			return post(url, params);
		case GET:
			return get(url, params);
		default:
			return null;
		}
	}
	
	public HttpResponse send() 
			throws ClientProtocolException, IOException {
		return send(null);
	}
	
	private HttpResponse get(String url, Bundle params) 
			throws ClientProtocolException, IOException {
		return client.execute(new HttpGet(url));
	}
	
	private HttpResponse post(String url, Bundle params) 
			throws ClientProtocolException, IOException {
		HttpPost method = new HttpPost(url);
		
		if(params != null) {
			try {
				ArrayList<NameValuePair> entity = new ArrayList<NameValuePair>();
				
				for(String k : params.keySet()) {
					entity.add(new BasicNameValuePair(k, params.getString(k)));
				}
				
				method.setEntity(new UrlEncodedFormEntity(entity));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		return client.execute(method);
	}

}
