package com.grouzen.android.serenity;

import java.util.Date;

import android.content.Context;
import android.util.Log;

public class Session {
	
	public final static String TAG = Session.class.getCanonicalName();
	
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
		
		try {
			stateCallbacks.onCreated();
		} catch(SessionState.CallbackNotImplementedException e) {
			Log.i(TAG, "onCreated callback not implemented");
		}
	}
	
	public Session(Context context, Session.OpenHandler openHandler) {
		this(context, openHandler, null);
	}
	
	public Session(Context context) {
		this(context, null, null);
	}
	
	public final Date getExpirationDate() {
		return expirationDate;
	}
	
	public final SessionState getState() {
		return state;
	}
	
	public void open() {
		if(openHandler.onOpen(this)) {
			state = SessionState.OPENED;
			
			try {
				stateCallbacks.onOpened();
			} catch(SessionState.CallbackNotImplementedException e) {
				Log.i(TAG, "onOpened callback not implemented");
			}
		}
	}
	
	public void close() {
		state = SessionState.CLOSED;
		
		try {
			stateCallbacks.onClosed();
		} catch(SessionState.CallbackNotImplementedException e) {
			Log.i(TAG, "onClosed callback not implemented");
		}
	}
	
	public static interface OpenHandler {
		
		public boolean onOpen(Session session);
		
	}
	
	
}
