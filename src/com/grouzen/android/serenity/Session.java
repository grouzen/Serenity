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

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class Session {
	
	public final static String TAG = Session.class.getCanonicalName();

	private static final String STORAGE_EXPIRATION_DATE_KEY = 
			"com.grounze.android.serenity.SessionStorage.STORAGE_EXPIRATION_DATE_KEY";
	
	private SessionStorage storage;
	
	private SessionToken token;
	
	private Context context;
	
	private SessionState state;
	
	private Date expirationDate;
	
	private ValidateHandler validateHandler;
	
	private SessionState.Callbacks stateCallbacks;
	
	public Session(Context context, SessionToken token, 
			ValidateHandler validateHandler, SessionState.Callbacks stateCallbacks) {
		this.context = context;
		this.state = SessionState.CREATED;
		this.validateHandler = validateHandler;
		this.stateCallbacks = stateCallbacks;
		this.storage = new SessionStorage(context);
		this.token = token;
		
		callStateCallback(this.state);
	}
	
	public Session(Context context, SessionToken token, ValidateHandler validateHandler) {
		this(context, token, validateHandler, null);
	}

	public Session(Context context, SessionToken token) {
		this(context, token, null, null);
	}
	
	public boolean isOpened() {
		synchronized(this) {
			return this.state == SessionState.OPENED;
		}
	}
	
	public boolean isClosed() {
		synchronized(this) {
			return this.state == SessionState.CLOSED;
		}
	}
	
	public boolean isCreated() {
		synchronized(this) {
			return this.state == SessionState.CREATED;
		}
	}
	
	public final Date getExpirationDate() {
		synchronized(this) {
			return expirationDate;
		}
	}
	
	public void setExpirationDate(Bundle bundle) {
		//expirationDate = bundle.getString(STORAGE_EXPIRATION_DATE_KEY); 
	}
	
	public ValidateHandler getValidateHandler() {
		synchronized(this) {
			return validateHandler;
		}
	}
	
	public void setValidateHandler(ValidateHandler handler) {
		synchronized(this) {
			validateHandler = handler;
		}
	}
	
	public SessionStorage getStorage() {
		synchronized(this) {
			return storage;
		}
	}
	
	public SessionState getState() {
		synchronized(this) {
			return state;
		}
	}
	
	public SessionToken getToken() {
		synchronized(this) {
			return token;
		}
	}
	
	public void open() {
		Bundle bundle;
		
		if(!isOpened()) {
			bundle = storage.load();
			
			if(!bundle.isEmpty()) {
				synchronized(this) {
					//expirationDate = bundle.getString(STORAGE_EXPIRATION_DATE_KEY));
					state = SessionState.OPENED;
					
					token.fill(bundle);
					callStateCallback(state);
				}
			} else {
				close();
			}
		}
	}
	
	public void close() {
		if(isOpened()) {
			synchronized(this) {
				state = SessionState.CLOSED;
				
				token.clear();
				callStateCallback(state);
			}
		}
	}
	
	public static interface ValidateHandler {
		
		public boolean onValidate(Response response);
		
	}
	
	private void callStateCallback(SessionState state) {
		if(stateCallbacks != null) {
			try {
				switch(state) {
				case CREATED:
					stateCallbacks.onCreated();
					
					break;
				case OPENED:
					stateCallbacks.onOpened();
					
					break;
				case CLOSED:
					stateCallbacks.onClosed();
					
					break;
				}
			} catch(SessionState.CallbackNotImplementedException e) {
				Log.i(TAG, e.getMessage());
			}
		}
	}

}
