package com.gh1234.vertretungsplangag;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class PlanListFragment extends ListFragment {
	Plan plan;

	public PlanListFragment() {

	}

	public void setPlan(Plan plan){
		this.plan = plan;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(plan == null)
			throw new IllegalArgumentException("Use setPlan prior!");
		PlanListAdapter adapter = new PlanListAdapter(plan, getActivity());
		setListAdapter(adapter);
	}
}
