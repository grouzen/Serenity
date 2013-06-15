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

import android.content.Context;
import android.util.Log;

public class Session {
	
	public final static String TAG = Session.class.getCanonicalName();

	private SessionStorage mStorage;
	
	private SessionToken mToken;
	
	private Context mContext;
	
	private SessionState mState;
	
	private ValidateHandler mValidateHandler;
	
	private SessionState.Callbacks mStateCallbacks;
	
	public Session(Context context, SessionToken token, 
			SessionState.Callbacks stateCallbacks, ValidateHandler validateHandler) {
		mContext = context;
		mValidateHandler = validateHandler;
		mStateCallbacks = stateCallbacks;
		mStorage = new SessionStorage(context);
		mToken = token;
		
		mStorage.load();
		
		mState = SessionState.CREATED;
		callStateCallback(mState, context);
	}
	
	public Session(Context context, SessionToken token, SessionState.Callbacks stateCallbacks) {
		this(context, token, stateCallbacks, null);
	}

	public Session(Context context, SessionToken token) {
		this(context, token, null, null);
	}
	
	public boolean isOpened() {
		synchronized(this) {
			return mState == SessionState.OPENED;
		}
	}
	
	public boolean isClosed() {
		synchronized(this) {
			return mState == SessionState.CLOSED;
		}
	}
	
	public boolean isCreated() {
		synchronized(this) {
			return mState == SessionState.CREATED;
		}
	}
	
	public ValidateHandler getValidateHandler() {
		synchronized(this) {
			return mValidateHandler;
		}
	}
	
	public void setValidateHandler(ValidateHandler handler) {
		synchronized(this) {
			mValidateHandler = handler;
		}
	}
	
	public SessionStorage getStorage() {
		synchronized(this) {
			return mStorage;
		}
	}
	
	public SessionState getState() {
		synchronized(this) {
			return mState;
		}
	}
	
	public SessionToken getToken() {
		synchronized(this) {
			return mToken;
		}
	}
	
	public void open() {
        if(!mStorage.isEmpty()) {
            synchronized(this) {
                mState = SessionState.OPENED;

                mToken.fill(mStorage.getBundle());
                callStateCallback(mState, mContext);
            }
        }
	}
	
	public void close() {
        synchronized(this) {
            mState = SessionState.CLOSED;

            mStorage.clear();
            mToken.clear();
            callStateCallback(mState, mContext);
        }
	}
	
	public static interface ValidateHandler {
		
		public boolean onValidate(Response response);
		
	}
	
	private void callStateCallback(SessionState state, Context context) {
		if(mStateCallbacks != null) {
			try {
				switch(state) {
				case CREATED:
					mStateCallbacks.onCreated(context);
					
					break;
				case OPENED:
					mStateCallbacks.onOpened(context);
					
					break;
				case CLOSED:
					mStateCallbacks.onClosed(context);
					
					break;
				}
			} catch(SessionState.CallbackNotImplementedException e) {
				Log.i(TAG, e.getMessage());
			}
		}
	}

}
