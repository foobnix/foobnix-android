package com.foobnix.model;


public class Song extends NavItem {
	private int position;


	public Song(String text, String path) {
		super(text, path);
	}

	public Song(String text, String path, int uuid) {
		super(text, path);
		setUuid(uuid);
	}

	public Song(NavItem item) {
		super(item.getText(), item.getPath());
		setUuid(item.getUuid());
	}

	@Override
	public int hashCode() {
		return getUuid();
	}

	@Override
	public boolean equals(Object o) {
		return o.hashCode() == hashCode();
	}

	@Override
	public String toString() {
		return new StringBuilder().append(getText()).append(getPath()).toString();
	}

	public void setPosition(int position) {
	    this.position = position;
    }

	public int getPosition() {
	    return position;
    }

}
