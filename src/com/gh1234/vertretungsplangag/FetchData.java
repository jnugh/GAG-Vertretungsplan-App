package com.gh1234.vertretungsplangag;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.os.AsyncTask;
import android.util.Log;

public class FetchData extends AsyncTask<Integer, Integer, String> {

	private OnTaskCompleted listener;
	private boolean error = false;
	private Plan plan = null;

	public Plan getPlan() {
		return plan;
	}

	public FetchData(OnTaskCompleted listener) {
		this.listener = listener;
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
							JSONArray arr = new JSONArray(new JSONTokener(
									response));
							for (int i = 0; i < arr.length(); i++) {
								JSONObject obj = arr.getJSONObject(i);
								plan.add(new Entry(obj.getString("hour"), obj
										.getString("teacher"), obj
										.getString("subject"), obj
										.getString("classes"), obj
										.getString("room"), obj
										.getString("comment")));
							}
							FetchData.this.listener.onTaskCompleted();
						} catch (JSONException throwable) {
							FetchData.this.error = true;
							Log.e("FetchData", "Error in JSON read", throwable);
							FetchData.this.listener.onTaskCompleted();
						}
					}

					@Override
					public void onFailure(Throwable throwable) {
						FetchData.this.error = true;
						Log.e("FetchData", "Error in HTTP request", throwable);
						FetchData.this.listener.onTaskCompleted();
					}
				});

		return null;
	}

	public boolean hasError() {
		return this.error;
	}
}
