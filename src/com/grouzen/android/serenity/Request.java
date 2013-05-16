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
