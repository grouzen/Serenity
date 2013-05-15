package com.grouzen.android.serenity;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SessionStorage {
	
	private Context context;
	
	private static final String STORAGE_KEY = "com.grouzen.android.serenity.SessionStorage.STORAGE_KEY";
	
	private Bundle bundle;
	
	public SessionStorage(Context context) {
		this.context = context;
		this.bundle = new Bundle();
	}
	
	public boolean isEmpty() {
		return bundle.isEmpty();
	}
	
	public Bundle load() {
		bundle = new Bundle();
		SharedPreferences preferences = 
				context.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE);
		
		Map<String, ?> entries = preferences.getAll();
		
		for(String key : entries.keySet()) {
			bundle.putString(key, (String) entries.get(key));
		}
		
		return bundle;
	}
	
	public void save(Bundle bundle) {
		SharedPreferences.Editor editor = 
				context.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE).edit();
		
		for(String key : bundle.keySet()) {
			editor.putString(key, bundle.getString(key));
		}
		
		this.bundle = bundle;
		editor.commit();
	}
	
	public void clear() {
		SharedPreferences.Editor editor = 
				context.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE).edit();
		
		this.bundle.clear();
		editor.clear().commit();
	}
	
}


