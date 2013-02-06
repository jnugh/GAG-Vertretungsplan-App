package com.gh1234.vertretungsplangag;

import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Main extends FragmentActivity implements OnTaskCompleted, FragmentAlertDialog {

	public static final String SERVER = "http://192.168.0.2:3000/";
	ProgressBar progress = null;
	FetchData fetch = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		progress = (ProgressBar) findViewById(R.id.progress);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		fetch = new FetchData(Main.this);
		fetch.execute(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onTaskCompleted() {
		if(fetch.hasError()){
			connectionError();
		}
		Plan plan = fetch.getPlan();
		
		progress.setVisibility(View.GONE);
	}

	private void connectionError(){
		DialogFragment newFragment = NoConnectionDialog.newInstance(
	            R.string.connection_error);
	    newFragment.show(getSupportFragmentManager(), "dialog");
	}

	public void doPositiveClick() {
		Intent intent = new Intent(this, Main.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void doNegativeClick() {
		finish();
	}
}
