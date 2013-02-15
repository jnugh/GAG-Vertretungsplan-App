package com.gh1234.vertretungsplangag;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.PowerManager;
import android.util.Log;

public class User {
	SharedPreferences preferences;
	private Context mContext;

	public User(Context context) {
		mContext = context;
		preferences = context.getSharedPreferences(Main.PREFERENCE_FILE, 0);
	}

	public boolean isInitialized() {
		return preferences.contains(Main.PREFERENCE_USERID);
	}

	public String getId() {
		return preferences.getString(Main.PREFERENCE_USERID, "");
	}

	public void register(String regId) {
		preferences.edit().remove(Main.PREFERENCE_USERID).commit();
		PowerManager pm = (PowerManager) mContext
				.getSystemService(Context.POWER_SERVICE);
		final PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,
				"register");
		wl.acquire();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		setParams(params);
		params.put("gtoken", regId);
		client.get(mContext, Main.SERVER + "users/add", params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable arg0, String arg1) {
						super.onFailure(arg0, arg1);
						Log.e("GCM", "Unable to register with server", arg0);
						wl.release();
					}

					@Override
					public void onSuccess(String arg0) {
						super.onSuccess(arg0);
						try {
							JSONObject jsonObject = new JSONObject(
									new JSONTokener(arg0));
							if (jsonObject.getBoolean("status")) {
								Editor editor = preferences.edit();
								editor.putString(Main.PREFERENCE_USERID,
										jsonObject.getString("id"));
								editor.commit();
							} else {
								Log.e("GCM", "Unable to register with server: "
										+ jsonObject.getString("err"));
							}
						} catch (JSONException e) {
							Log.e("GCM", "Unable to read server response", e);
						} finally {
							wl.release();
						}
					}
				});
	}

	private void setParams(RequestParams params) {
		params.put("classes",
				preferences.getString(Main.PREFERENCE_CLASSES, "13"));
		params.put("notify", (preferences.getBoolean(
				Main.PREFERENCE_NOTIFICATIONS, true)) ? "true" : "false");

		Set<String> subjectSet = getSubjectSet();

		JSONArray array = new JSONArray();
		for (String string : subjectSet) {
			array.put(string);
		}
		params.put("subjects", array.toString());
	}

	@SuppressLint("NewApi")
	private Set<String> getSubjectSet() {
		Set<String> subjectSet = null;
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			Set<String> defaultSet = new HashSet<String>();
			defaultSet.add("Ph1");
			subjectSet = preferences.getStringSet(Main.PREFERENCE_SUBJECTS,
					defaultSet);
		} else {
			subjectSet = new HashSet<String>();
			String subjectString = preferences.getString(
					Main.PREFERENCE_SUBJECTS, "Ph1");
			String[] subjectStringArr = subjectString.split(";");
			for (String string : subjectStringArr) {
				subjectSet.add(string);
			}
		}
		return subjectSet;
	}

	public void pushUpdates() {
		PowerManager pm = (PowerManager) mContext
				.getSystemService(Context.POWER_SERVICE);
		final PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,
				"register");
		wl.acquire();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		setParams(params);
		client.get(mContext, Main.SERVER + "users/" + getId() + "/set", params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable arg0, String arg1) {
						super.onFailure(arg0, arg1);
						Log.e("SRV", "Unable to register with server", arg0);
						wl.release();
					}

					@Override
					public void onSuccess(String arg0) {
						super.onSuccess(arg0);
						try {
							JSONObject jsonObject = new JSONObject(
									new JSONTokener(arg0));
							if (jsonObject.getBoolean("status")) {
								Editor editor = preferences.edit();
								editor.putBoolean(
										Main.PREFERENCE_DATA_NOT_PUSHED, false);
								editor.commit();
							} else {
								Log.e("SRV", "Unable to register with server: "
										+ jsonObject.getString("err"));
							}
						} catch (JSONException e) {
							Log.e("SRV", "Unable to read server response", e);
						} finally {
							wl.release();
						}
					}
				});
	}
	
	public String getClasses(){
		return preferences.getString(Main.PREFERENCE_CLASSES, "13");
	}
	
	public Set<String> getSubjects(){
		return getSubjectSet();
	}

	public void unregister() {
		PowerManager pm = (PowerManager) mContext
				.getSystemService(Context.POWER_SERVICE);
		final PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,
				"register");
		wl.acquire();

		if (preferences.contains(Main.PREFERENCE_USERID)) {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(
					mContext,
					Main.SERVER + "users/"
							+ preferences.getString(Main.PREFERENCE_USERID, "")
							+ "/remove", new AsyncHttpResponseHandler() {
						@Override
						public void onFailure(Throwable arg0, String arg1) {
							super.onFailure(arg0, arg1);
							wl.release();
						}

						@Override
						public void onSuccess(int arg0, String arg1) {
							super.onSuccess(arg0, arg1);
							wl.release();
						}
					});
			preferences.edit().remove(Main.PREFERENCE_USERID).commit();
		}
	}
}
