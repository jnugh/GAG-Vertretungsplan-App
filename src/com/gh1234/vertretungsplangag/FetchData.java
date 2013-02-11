package com.gh1234.vertretungsplangag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class FetchData extends AsyncTask<Integer, Integer, String> {

	private OnTaskCompleted listener;
	private Activity context;
	private boolean error = false;
	private Plan plan = null;

	public Plan getPlan() {
		return plan;
	}

	public FetchData(OnTaskCompleted listener, Activity context) {
		this.listener = listener;
		this.context = context;
	}

	@Override
	protected String doInBackground(final Integer... params) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Main.SERVER + "plan/" + params[0],
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						try {
							plan = new Plan(params[0]);
							JSONObject baseObj = new JSONObject(new JSONTokener(
									response));
							plan.setDate(baseObj.getString("planDate"));
							JSONArray arr = baseObj.getJSONArray("data");
							for (int i = 0; i < arr.length(); i++) {
								JSONObject obj = arr.getJSONObject(i);
								plan.add(new Entry(obj.getString("hour"), obj
										.getString("teacher"), obj
										.getString("subject"), obj
										.getString("classes"), obj
										.getString("room"), obj
										.getString("comment")));
							}
							FetchData.this.context
									.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											FetchData.this.listener
													.onTaskCompleted();
										}
									});
						} catch (JSONException throwable) {
							FetchData.this.error = true;
							Log.e("FetchData", "Error in JSON read", throwable);
							FetchData.this.context
									.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											FetchData.this.listener
													.onTaskCompleted();
										}
									});
						}
					}

					@Override
					public void onFailure(Throwable throwable) {
						FetchData.this.error = true;
						Log.e("FetchData", "Error in HTTP request", throwable);
						FetchData.this.context.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								FetchData.this.listener.onTaskCompleted();
							}
						});
					}
				});

		return null;
	}

	public boolean hasError() {
		return this.error;
	}
}
