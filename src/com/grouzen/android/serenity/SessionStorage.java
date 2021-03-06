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

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SessionStorage {
	
	private Context mContext;
	
	private static final String STORAGE_KEY = 
			"com.grouzen.android.serenity.SessionStorage.STORAGE_KEY";
	
	private Bundle mBundle;
	
	public SessionStorage(Context context) {
		mContext = context;
		mBundle = new Bundle();
	}
	
	public boolean isEmpty() {
		synchronized(this) {
			return mBundle.isEmpty();
		}
	}
	
	public Bundle getBundle() {
		return mBundle;
	}
	
	public void setBundle(Bundle bundle) {
		this.mBundle = bundle;
	}
	
	public void load() {
		synchronized(this) {
			SharedPreferences preferences = 
					mContext.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE);
			
			Map<String, ?> entries = preferences.getAll();
			
			for(String key : entries.keySet()) {
				mBundle.putString(key, (String) entries.get(key));
			}
		}
	}
	
	public void save(Bundle bundle) {
		synchronized(this) {
			SharedPreferences.Editor editor = 
					mContext.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE).edit();
			
			for(String key : bundle.keySet()) {
				editor.putString(key, bundle.getString(key));
			}
			
			mBundle = bundle;
			editor.commit();
		}
	}
	
	public void clear() {
		synchronized(this) {
			SharedPreferences.Editor editor = 
					mContext.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE).edit();
			
			mBundle.clear();
			editor.clear().commit();
		}
	}
	
}
