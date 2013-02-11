package com.gh1234.vertretungsplangag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

public class Main extends FragmentActivity implements OnTaskCompleted, FragmentAlertDialog {

	public static final String SERVER = "http://192.168.0.2:3000/";
	ProgressBar progress = null;
	FetchData fetch = null;
	PlanListAdapter adapter;
	ListView listView;
	PlanListFragment planListFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		progress = (ProgressBar) findViewById(R.id.progress);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		fetch = new FetchData(Main.this, Main.this);
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
				
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		planListFragment = new PlanListFragment();
		planListFragment.setPlan(plan);
		ft.add(R.id.content, planListFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
		
		progress.setVisibility(View.GONE);
	}

	private void connectionError(){
		DialogFragment newFragment = NoConnectionDialog.newInstance(
	            R.string.connection_error);
	    newFragment.show(getSupportFragmentManager(), "dialog");
	}

	@Override
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
