package com.gh1234.vertretungsplangag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PlanView extends FragmentPagerAdapter {

	public PlanView(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public Fragment getItem(int position) {
		return PlanFragment.newInstance(position);
	}

}
