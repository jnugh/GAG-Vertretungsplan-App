package com.gh1234.vertretungsplangag;

import java.util.ArrayList;

public class Plan {
	private Integer type;
	private ArrayList<Entry> entries = new ArrayList<Entry>();
	public Plan(Integer type) {
		this.type = type;
	}
	public void add(Entry entry) {
		entries.add(entry);
	}
	public ArrayList<Entry> getEntries() {
		return entries;
	}

}
