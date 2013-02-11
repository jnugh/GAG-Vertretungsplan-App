package com.gh1234.vertretungsplangag;

import java.util.ArrayList;

public class Plan {
	private Integer type;
	private ArrayList<Entry> entries = new ArrayList<Entry>();
	private String date;
	public Plan(Integer type) {
		this.type = type;
	}
	public void add(Entry entry) {
		entries.add(entry);
	}
	public ArrayList<Entry> getEntries() {
		return entries;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate(){
		return this.date;
	}
	public int getType(){
		return type;
	}
}
