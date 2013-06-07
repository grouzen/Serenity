/*
Android java library for asynchronous http requests with sessions support 
Copyright (c) 2013 Michael Nedokushev <grouzen.hexy@gmail.com>
http://grouzen.com/

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.grouzen.android.serenity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.Bundle;

public class HttpConnection {
	
	private static final String TAG = HttpConnection.class.getCanonicalName();

    private static final String CHARSET = "UTF-8";
	
	private HttpConnectionMethod mMethod;
	
	private HttpClient mClient;
	
	private CookieStore mCookies;
	
	private String mUrl;

    private String mMultipartKey;

    private String mMultipartFileName;
	
	public HttpConnection(String url, HttpConnectionMethod method) {
		this.mMethod = method;
		this.mUrl = url;
		this.mClient = new DefaultHttpClient();
		this.mCookies = new BasicCookieStore();
	}
	
	public HttpConnection(String url) {
		this(url, HttpConnectionMethod.POST);
	}
	
	public HttpConnection() {
		this(null, HttpConnectionMethod.POST);
	}

    public void setMultipartKey(String key) {
        mMultipartKey = key;
    }

    public void setMultipartFileName(String fileName) {
        mMultipartFileName = fileName;
    }

	public CookieStore getCookies() {
		return mCookies;
	}
	
	public void setMethod(HttpConnectionMethod method) {
		this.mMethod = method;
	}
	
	public void setUrl(String url) {
		this.mUrl = url;
	}
	
	public HttpResponse send(Bundle params) 
			throws ClientProtocolException, IOException {
		switch(mMethod) {
		case POST:
			return post(mUrl, params);
		case GET:
			return get(mUrl, params);
		default:
			return null;
		}
	}
	
	public HttpResponse send() 
			throws ClientProtocolException, IOException {
		return send(null);
	}
	
	private HttpResponse execute(HttpUriRequest request) 
			throws ClientProtocolException, IOException {
		HttpResponse response;
		HttpContext localContext = new BasicHttpContext();
		
		localContext.setAttribute(ClientContext.COOKIE_STORE, mCookies);
		response = mClient.execute(request, localContext);
		
		return response;
	}

    private ArrayList<NameValuePair> convertParams(Bundle params) {
        ArrayList<NameValuePair> entity = new ArrayList<NameValuePair>();

        for(String k : params.keySet()) {
            entity.add(new BasicNameValuePair(k, params.getString(k)));
        }

        return entity;
    }

	private HttpResponse get(String url, Bundle params) 
			throws ClientProtocolException, IOException {
        String urlParametrized = url;

        if(params != null) {
            urlParametrized += "?" + URLEncodedUtils.format(convertParams(params), CHARSET);
        }

		return execute(new HttpGet(urlParametrized));
	}
	
	private HttpResponse post(String url, Bundle params) 
			throws ClientProtocolException, IOException {
		HttpPost method = new HttpPost(url);
		
		if(params != null) {
            try {
                if(mMultipartKey != null && mMultipartFileName != null) {
                    ByteArrayBody byteArrayBody = new ByteArrayBody(params.getByteArray(mMultipartKey), mMultipartFileName);
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    Charset charset = Charset.forName(CHARSET);

                    entity.addPart(mMultipartKey, byteArrayBody);
                    params.remove(mMultipartKey);

                    for(String k : params.keySet()) {
                        entity.addPart(new FormBodyPart(k, new StringBody(params.getString(k), charset)));
                    }

                    method.setEntity(entity);
                } else {
                        ArrayList<NameValuePair> entity = convertParams(params);

                        method.setEntity(new UrlEncodedFormEntity(entity, CHARSET));

                }
            }  catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
		
		return execute(method);
	}

}
