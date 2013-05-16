package com.grouzen.android.serenity;

import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
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
		
		if(stateCallbacks != null) {
			try {
				stateCallbacks.onCreated();
			} catch(SessionState.CallbackNotImplementedException e) {
				Log.i(TAG, "onCreated callback not implemented");
			}
		}
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
			
			if(stateCallbacks != null) {
				try {
					stateCallbacks.onOpened();
				} catch(SessionState.CallbackNotImplementedException e) {
					Log.i(TAG, "onOpened callback not implemented");
				}
			}
		}
	}
	
	public void close() {
		state = SessionState.CLOSED;
		
		if(stateCallbacks != null) {
			try {
				stateCallbacks.onClosed();
			} catch(SessionState.CallbackNotImplementedException e) {
				Log.i(TAG, "onClosed callback not implemented");
			}
		}
	}
	
	public static interface OpenHandler {
		
		public boolean onOpen(Session session);
		
	}
	
	
}
