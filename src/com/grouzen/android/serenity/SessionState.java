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

public enum SessionState {

	CLOSED(State.CLOSED),
	
	OPENED(State.OPENED),
	
	CREATED(State.CREATED);
	
	private final State mState;
	
	SessionState() {
		this.mState = State.CREATED;
	}
	
	SessionState(State state) {
		this.mState = state;
	}
	
	private enum State {
		
		CLOSED,
		
		OPENED,
		
		CREATED,
		
	}
	
	public static class CallbackNotImplementedException extends Exception {

		private static final long serialVersionUID = 1L;
		
		public CallbackNotImplementedException(String message) {
			super(message);
		}
		
	}
	
	public static class Callbacks {
		
		public void onCreated(Context context) throws CallbackNotImplementedException {
			throw new CallbackNotImplementedException("onCreated() callback is not implemented");
		}
		
		public void onOpened(Context context) throws CallbackNotImplementedException {
			throw new CallbackNotImplementedException("onOpened() callback is not implemented");
		}
		
		public void onClosed(Context context) throws CallbackNotImplementedException {
			throw new CallbackNotImplementedException("onClosed() callback is not implemented");
		}
		
	}
	
}
