package com.gh1234.vertretungsplangag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class PlanListAdapter extends BaseAdapter implements
		ListAdapter {

	private Plan plan;
	private Context context;
	private LayoutInflater layoutInflater = null;

	public PlanListAdapter(Plan pPlan, Context pContext) {
		plan = pPlan;
		context = pContext;
	}

	private Context getContext() {
		return context;
	}

	@Override
	public int getCount() {
		return plan.getEntries().size();
	}

	@Override
	public Entry getItem(int position) {
		return plan.getEntries().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		Entry entry = getItem(position);
		if (convertView != null) {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		} else {
			if (layoutInflater == null)
				layoutInflater = (LayoutInflater) getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.entry_row, parent, false);
			
			holder = new ViewHolder();
			holder.hour = (TextView) view.findViewById(R.id.hour);
			holder.classes = (TextView) view.findViewById(R.id.classes);
			holder.subject = (TextView) view.findViewById(R.id.subject);
			holder.comment = (TextView) view.findViewById(R.id.comment);
			
			view.setTag(holder);
		}
		holder.hour.setText(entry.getHour());
		holder.classes.setText(entry.getClasses());
		holder.subject.setText(entry.getSubject());
		holder.comment.setText(entry.getComment());
		return view;
	}

	static class ViewHolder {
		TextView hour;
		TextView classes;
		TextView subject;
		TextView comment;
	}

}
