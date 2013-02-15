package com.gh1234.vertretungsplangag;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GCMIntentService extends GCMBaseIntentService {

	private static final int GCM_NOTIFYID = 11;

	@Override
	protected void onError(Context arg0, String arg1) {

	}

	@SuppressLint({ "NewApi", "Wakelock" })
	@Override
	protected void onMessage(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		final PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,
				"register");
		wl.acquire();

		// Notification Setup
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Vertretungsplanänderung")
				.setContentText(
						new String(Base64.decode(
								intent.getStringExtra("message"),
								Base64.DEFAULT)));
		Intent resultIntent = new Intent(context, Main.class);
		if (android.os.Build.VERSION.SDK_INT >= 16) {
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(Main.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					0, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);
		} else {
			PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
			mBuilder.setContentIntent(resultPendingIntent);
		}
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(GCM_NOTIFYID, mBuilder.build());

		wl.release();
	}

	@Override
	protected void onRegistered(final Context context, String regId) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		final PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,
				"register");
		wl.acquire();

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("gtoken", regId);
		client.get(context, Main.SERVER + "users/add", params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable arg0, String arg1) {
						super.onFailure(arg0, arg1);
						Log.e("GCM", "Unable to register with server", arg0);
						GCMRegistrar.unregister(context);
						wl.release();
					}

					@Override
					public void onSuccess(String arg0) {
						super.onSuccess(arg0);
						try {
							JSONObject jsonObject = new JSONObject(
									new JSONTokener(arg0));
							if (jsonObject.getBoolean("status")) {
								SharedPreferences preferences = context
										.getSharedPreferences(
												Main.PREFERENCE_FILE, 0);
								Editor editor = preferences.edit();
								editor.putString(Main.PREFERENCE_USERID,
										jsonObject.getString("id"));
								editor.commit();
							} else {
								Log.e("GCM", "Unable to register with server: "
										+ jsonObject.getString("err"));
								GCMRegistrar.unregister(context);
							}
						} catch (JSONException e) {
							Log.e("GCM", "Unable to read server response", e);
							GCMRegistrar.unregister(context);
						} finally {
							wl.release();
						}
					}
				});
	}

	@SuppressLint("Wakelock")
	@Override
	protected void onUnregistered(Context context, String arg1) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		final PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,
				"register");
		wl.acquire();

		SharedPreferences preferences = getSharedPreferences(
				Main.PREFERENCE_FILE, 0);
		if (preferences.contains(Main.PREFERENCE_USERID)) {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(
					context,
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
	
	@Override
	protected String[] getSenderIds(Context context) {
		return new String[]{context.getString(R.string.SENDER_ID)};
	}
}
