package com.grouzen.android.serenity;

import android.os.AsyncTask;

public class RequestAsyncTask 
	extends AsyncTask<Void, Void, Response> {

	private Request request;
	
	private Response response;
	
	public RequestAsyncTask(Request request) {
		this.request = request;
		this.response = new Response(request);
	}
	
	@Override
	protected Response doInBackground(Void... params) {
		return response.fromHttpConnection();
	}
	
	@Override 
	protected void onPostExecute(Response response) {
		response.getRequest().getCallback().onComplete(response);
	}

}
