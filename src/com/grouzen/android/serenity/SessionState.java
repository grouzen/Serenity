package com.grouzen.android.serenity;

public enum SessionState {

	CLOSED(State.CLOSED),
	
	OPENED(State.OPENED),
	
	CREATED(State.CREATED);
	
	private final State state;
	
	SessionState() {
		this.state = State.CREATED;
	}
	
	SessionState(State state) {
		this.state = state;
	}
	
	private enum State {
		
		CLOSED,
		
		OPENED,
		
		CREATED,
		
	}
	
	public static class CallbackNotImplementedException extends Exception {

		private static final long serialVersionUID = 1L;
		
	}
	
	public static class Callbacks {
		
		public void onCreated() throws CallbackNotImplementedException {
			throw new CallbackNotImplementedException();
		}
		
		public void onOpened() throws CallbackNotImplementedException {
			throw new CallbackNotImplementedException();
		}
		
		public void onClosed() throws CallbackNotImplementedException {
			throw new CallbackNotImplementedException();
		}
		
	}
	
}