package com.gh1234.vertretungsplangag;

import com.google.android.gcm.GCMRegistrar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class BaseFragmentActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		// First Start?
		SharedPreferences preferences = getSharedPreferences(
				Main.PREFERENCE_FILE, 0);
		Intent startIntent = getIntent();
		startIntent.getBooleanExtra("initialSettings", false);
		if (!preferences.getBoolean(Main.PREFERENCE_NOT_FIRST_START, false) && !startIntent.getBooleanExtra("initialSettings", false)) {
			Intent intent = new Intent(this, Settings.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("initialSettings", true);
			startActivity(intent);
			finish();
			return;
		}
		
		if(preferences.getBoolean(Main.PREFERENCE_DATA_NOT_PUSHED, false)){
			new User(this).pushUpdates();
		}

		// GCM
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			GCMRegistrar.register(this, getString(R.string.SENDER_ID));
		} else {
			if (!preferences.contains(Main.PREFERENCE_USERID)) {
				Log.w("GCM", "Something went wrong...");
				new User(this).register(regId);
			}
			Log.v("GCM", "Already registered");
		}
		// GCM END
	}
}
