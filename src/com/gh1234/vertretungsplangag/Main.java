package com.gh1234.vertretungsplangag;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

public class Main extends FragmentActivity implements OnTaskCompleted,
		FragmentAlertDialog {

	public static final String SERVER = "http://192.168.0.2:3000/";
	ProgressBar progress = null;
	FetchData fetch = null;
	PlanListAdapter adapter;
	ListView listView;
	PlanListFragment planListFragment;
	Plan plan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		progress = (ProgressBar) findViewById(R.id.progress);

		Button button = (Button) findViewById(R.id.switch_plan);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, Main.class);
				if (getIntent().getIntExtra("startPlan", 1) == 1)
					intent.putExtra("startPlan", 2);
				else
					intent.putExtra("startPlan", 1);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		fetch = new FetchData(Main.this, Main.this);
		fetch.execute(getIntent().getIntExtra("startPlan", 1));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			return true;
		case R.id.switch_plan:
			Intent intent = new Intent(this, Main.class);
			intent.putExtra("startPlan", 2);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTaskCompleted() {
		if (fetch.hasError()) {
			connectionError();
			return;
		}
		plan = fetch.getPlan();

		displayPlanTitle();

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		planListFragment = new PlanListFragment();
		planListFragment.setPlan(plan);
		ft.add(R.id.content, planListFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();

		progress.setVisibility(View.GONE);
	}

	@SuppressLint("NewApi")
	private void displayPlanTitle() {
		String title = "Vertretungsplan " + plan.getDate();
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			getActionBar().setTitle(title);
		} else {
			setTitle(title);
		}
	}

	private void connectionError() {
		DialogFragment newFragment = NoConnectionDialog
				.newInstance(R.string.connection_error);
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
