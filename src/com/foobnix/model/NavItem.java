package com.foobnix.model;

import java.util.Random;

public class NavItem {
	private String text;
	private String path;
	private boolean file;
	private boolean checked;
	private int uuid;

	public NavItem(String text, String path, boolean file) {
		this.text = text;
		this.path = path;
		this.file = file;
		uuid = new Random().nextInt();
	}

	public NavItem(String text, String path) {
		this(text, path, true);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isFile() {
		return file;
	}

	public void setFile(boolean file) {
		this.file = file;
	}

	public void setChecked(boolean checked) {
	    this.checked = checked;
    }

	public boolean isChecked() {
	    return checked;
    }

	public void setUuid(int uuid) {
	    this.uuid = uuid;
    }

	public int getUuid() {
	    return uuid;
    }

}
