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

import android.os.Bundle;

public class Request {

	private Session session;
	
	private Callback callback;
	
	private Bundle parameters;
	
	private HttpConnection connection;
	
	private String url;
	
	private HttpConnectionMethod method;
	
	public Request(String url, Callback callback, Bundle parameters, 
			Session session, HttpConnectionMethod method) {
		this.session = session;
		this.parameters = parameters;
		this.callback = callback;
		this.url = url;
		this.method = method;
		this.connection = new HttpConnection(url, method);
	}
	
	public Request(String url, Callback callback, Bundle parameters, Session session) {
		this(url, callback, parameters, session, HttpConnectionMethod.POST);
	}
	
	public RequestAsyncTask execute() {
		RequestAsyncTask task = new RequestAsyncTask(this);
		
		task.execute();
		
		return task;
	}
	
	public Callback getCallback() {
		return callback;
	}
	
	public Bundle getParameters() {
		return parameters;
	}
	
	public Session getSession() {
		return session;
	}
	
	public HttpConnection getConnection() {
		return connection;
	}
	
	public String getUrl() {
		return url;
	}
	
	public static interface Callback {
	
		public void onComplete(Response response);
	
	}
	
}
