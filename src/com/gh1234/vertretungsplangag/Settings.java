package com.gh1234.vertretungsplangag;

import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Settings extends FragmentActivity {
	private EditText classes;
	private LinearLayout subjectLinearLayout;
	private ImageButton addSubject;
	private CheckBox notificationCheckBox;
	private Button saveButton;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_settings);

		if (android.os.Build.VERSION.SDK_INT >= 11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		classes = (EditText) findViewById(R.id.settings_classes);
		subjectLinearLayout = (LinearLayout) findViewById(R.id.settings_subject_list);
		addSubject = (ImageButton) findViewById(R.id.settings_add_subject);
		notificationCheckBox = (CheckBox) findViewById(R.id.settings_notifications);
		saveButton = (Button) findViewById(R.id.settings_save);

		insertData();

		saveButton.setOnClickListener(new SaveListener());
	}

	@SuppressLint("NewApi")
	private void insertData() {
		SharedPreferences prefs = getSharedPreferences(Main.PREFERENCE_FILE, 0);
		classes.setText(prefs.getString(Main.PREFERENCE_CLASSES, "13"));
		notificationCheckBox.setChecked(prefs.getBoolean(
				Main.PREFERENCE_NOTIFICATIONS, true));

		Set<String> subjectSet = null;
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			Set<String> defaultSet = new HashSet<String>();
			defaultSet.add("Ph1");
			subjectSet = prefs.getStringSet(Main.PREFERENCE_SUBJECTS,
					defaultSet);
		} else {
			subjectSet = new HashSet<String>();
			String subjectString = prefs.getString(Main.PREFERENCE_SUBJECTS,
					"Ph1");
			String[] subjectStringArr = subjectString.split(";");
			for (String string : subjectStringArr) {
				subjectSet.add(string);
			}
		}
		insertSubjects(subjectSet);
	}

	private void insertSubjects(Set<String> subjects) {
		int i = 0;
		for (String string : subjects) {
			EditText input = (EditText) subjectLinearLayout.getChildAt(i);
			if (input == null) {
				input = new EditText(this);
				input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
				subjectLinearLayout.addView(input);
			}
			input.setText(string);
			i++;
		}
	}

	private class SaveListener implements OnClickListener {
		@Override
		public void onClick(View v) {

		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, Main.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
