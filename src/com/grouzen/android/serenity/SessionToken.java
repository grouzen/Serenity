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

import java.util.Date;

import android.os.Bundle;

public abstract class SessionToken {

	protected Bundle bundle;

	public static final String STORAGE_EXPIRATION_DATE_KEY = 
			"com.grounze.android.serenity.SessionToken.STORAGE_EXPIRATION_DATE_KEY";
	
	protected Date expirationDate;
	
	public abstract void fill(Bundle bundle);
	
	public SessionToken() {
		bundle = new Bundle();
		expirationDate = new Date(0);
	}
	
	public void clear() {
		bundle.clear();
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}
	
	public void setExpirationDate(Date date) {
		expirationDate = date;
	}
	
	/*
	 * Default behavior of this method - add token's 
	 * bundle to the request parameters. You can override
	 * it in your own SessionToken class.  
	 */
	public void send(Request request) {
		Bundle parameters = request.getParameters();
		
		if(bundle != null) {
			for(String key: bundle.keySet()) {
				parameters.putString(key, bundle.getString(key));
			}
		}
	}
	
}
