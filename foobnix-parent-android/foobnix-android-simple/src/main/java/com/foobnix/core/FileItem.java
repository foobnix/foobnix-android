package com.foobnix.core;

import java.io.File;

import android.graphics.Bitmap;

@SuppressWarnings("serial")
public class FileItem {
	
	private File file;
	private transient Bitmap bitmap;
	private String displayName;
    private boolean isChecked;
	
	public FileItem(File file) {
		this.file = file;
		this.displayName = file.getName();
	}
	

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }


    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }


    public boolean isChecked() {
        return isChecked;
    }

}
