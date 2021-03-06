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
        if(response.getException() != null && request.getExceptionHandler() != null) {
            request.getExceptionHandler().onException(response.getException());
        } else {
		    response.getRequest().getCallback().onComplete(response);
        }
	}

}
