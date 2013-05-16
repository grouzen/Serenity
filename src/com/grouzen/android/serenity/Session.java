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
	
	private Context context;
	
	private SessionState state;
	
	private Date expirationDate;
	
	private OpenHandler openHandler;
	
	private SessionState.Callbacks stateCallbacks;
	
	public Session(Context context, Session.OpenHandler openHandler, 
			SessionState.Callbacks stateCallbacks) {
		this.context = context;
		this.state = SessionState.CREATED;
		this.openHandler = openHandler;
		this.stateCallbacks = stateCallbacks;
		this.storage = new SessionStorage(context);
		
		callStateCallback(this.state);
	}
	
	public Session(Context context, Session.OpenHandler openHandler) {
		this(context, openHandler, null);
	}
	
	public Session(Context context) {
		this(context, null, null);
	}
	
	public boolean isOpened() {
		return this.state == SessionState.OPENED;
	}
	
	public boolean isClosed() {
		return this.state == SessionState.CLOSED;
	}
	
	public boolean isCreated() {
		return this.state == SessionState.CREATED;
	}
	
	public final Date getExpirationDate() {
		return expirationDate;
	}
	
	public void setExpirationDate(Bundle bundle) {
		//expirationDate = bundle.getString(STORAGE_EXPIRATION_DATE_KEY); 
	}
	
	public final SessionStorage getStorage() {
		return storage;
	}
	
	public final SessionState getState() {
		return state;
	}
	
	public void open() {
		Bundle bundle = storage.load();
		
		if(bundle.isEmpty()) {
			if(openHandler != null)
				openHandler.onOpen(this);
		} else {
			//expirationDate = bundle.getString(STORAGE_EXPIRATION_DATE_KEY));
			state = SessionState.OPENED;
			
			callStateCallback(state);
		}
	}
	
	public void close() {
		state = SessionState.CLOSED;
		
		callStateCallback(state);
	}
	
	public static interface OpenHandler {
		
		public boolean onOpen(Session session);
		
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
