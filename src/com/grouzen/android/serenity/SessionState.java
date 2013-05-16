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
