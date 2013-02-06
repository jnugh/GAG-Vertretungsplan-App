package com.gh1234.vertretungsplangag;

public class Entry {

	private String hour;
	private String teacher;
	private String subject;
	private String classes;
	private String room;
	private String comment;

	public Entry(String hour, String teacher, String subject, String classes,
			String room, String comment) {
		this.setHour(hour);
		this.setTeacher(teacher);
		this.setSubject(subject);
		this.setClasses(classes);
		this.setRoom(room);
		this.setComment(comment);
	}

	public String getHour() {
		return hour;
	}

	private void setHour(String hour) {
		this.hour = hour;
	}

	public String getTeacher() {
		return teacher;
	}

	private void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getClasses() {
		return classes;
	}

	private void setClasses(String classes) {
		this.classes = classes;
	}

	public String getSubject() {
		return subject;
	}

	private void setSubject(String subject) {
		this.subject = subject;
	}

	public String getRoom() {
		return room;
	}

	private void setRoom(String room) {
		this.room = room;
	}

	public String getComment() {
		return comment;
	}

	private void setComment(String comment) {
		this.comment = comment;
	}

}
