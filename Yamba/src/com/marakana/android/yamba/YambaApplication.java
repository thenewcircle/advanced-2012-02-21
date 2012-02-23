package com.marakana.android.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class YambaApplication extends Application
		implements OnSharedPreferenceChangeListener {
	
	public static final String ACTION_NEW_STATUS
		= "com.marakana.android.yamba.ACTION_NEW_STATUS";
	public static final String EXTRA_NEW_STATUS_COUNT
		= "com.marakana.android.yamba.EXTRA_NEW_STATUS_COUNT";
	public static final String PERM_NEW_STATUS
		= "com.marakana.android.yamba.permission.NEW_STATUS";
	public static final int NOTIFICATION_NEW_STATUS = 1;
	
	private static YambaApplication instance;

	private Twitter twitter;
	private SharedPreferences prefs;
	private String prefPasswordKey;
	private String prefSiteUrlKey;
	private String prefUserKey;

	@Override
	public void onCreate() {
		super.onCreate();
		
		instance = this;
		
		prefUserKey = getString(R.string.pref_user_key);
		prefPasswordKey = getString(R.string.pref_password_key);
		prefSiteUrlKey = getString(R.string.pref_site_url_key);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
	}
	
	public static YambaApplication getInstance() {
		return instance;
	}

	public synchronized Twitter getTwitter() {
		if (twitter == null) {
			
			// System.setProperty("http.proxyHost", "my.proxyhost.com");
			// System.setProperty("http.proxyPort", "1234");
			// System.setProperty("http.proxyUser", "someUserName");
			// System.setProperty("http.proxyPassword", "somePassword");
			
			String user = prefs.getString(prefUserKey, null);
			String password = prefs.getString(prefPasswordKey, null);
			String url = prefs.getString(prefSiteUrlKey, null);
			
			twitter = new Twitter(user, password);
			twitter.setAPIRootUrl(url);
			
		}
		return twitter;
	}

	@Override
	public synchronized void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		twitter = null;
	}

}
