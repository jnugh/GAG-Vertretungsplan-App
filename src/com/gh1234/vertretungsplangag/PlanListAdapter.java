package com.gh1234.vertretungsplangag;

import android.content.Context;
import android.graphics.Color;
import android.provider.CalendarContract.Colors;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class PlanListAdapter extends BaseAdapter implements ListAdapter {

	private User user;
	private Plan plan;
	private Context context;
	private LayoutInflater layoutInflater = null;

	public PlanListAdapter(Plan pPlan, Context pContext) {
		plan = pPlan;
		context = pContext;
		user = new User(getContext());
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
				layoutInflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.entry_row, parent, false);

			holder = new ViewHolder();
			holder.hour = (TextView) view.findViewById(R.id.hour);
			holder.classes = (TextView) view.findViewById(R.id.classes);
			holder.subject = (TextView) view.findViewById(R.id.subject);
			holder.comment = (TextView) view.findViewById(R.id.comment);
			holder.holder = (LinearLayout) view.findViewById(R.id.holder);
			holder.bg = ViewHolder.BG_NONE;

			view.setTag(holder);
		}
		holder.hour.setText(entry.getHour());
		holder.classes.setText(entry.getClasses());
		holder.subject.setText(entry.getSubject());
		holder.comment.setText(entry.getComment());
		if (entry.getClasses().contains(user.getClasses())) {
			if (!user.getSubjects().contains(entry.getSubject())) {
				holder.holder.setBackgroundColor(ViewHolder.BG_CLASSES);
			} else {
				holder.holder.setBackgroundColor(ViewHolder.BG_SUBJECT);
			}
		} else {
			holder.holder.setBackgroundColor(ViewHolder.BG_NONE);
		}
		return view;
	}

	static class ViewHolder {
		LinearLayout holder;
		TextView hour;
		TextView classes;
		TextView subject;
		TextView comment;
		int bg;
		final static int BG_NONE = Color.WHITE;
		final static int BG_CLASSES = Color.YELLOW;
		final static int BG_SUBJECT = Color.RED;
	}

}
