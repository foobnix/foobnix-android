package com.foobnix.android.simple.core;

import java.io.File;
import java.io.FileFilter;

public class Mp3FileNameFilter implements FileFilter {
	@Override
	public boolean accept(File file) {
		if (file.isFile() && !isSuppertExt(file)) {
			return false;
		}
		/*
		 * //check directories if(file.isDirectory()){ File[] listFiles =
		 * file.listFiles(); if (listFiles == null) { return true; }
		 * 
		 * for (File child : listFiles) { if (child.isDirectory()) { return
		 * true; } if (isSuppertExt(child)) { return true; } }
		 * 
		 * }
		 */
		return true;
	}

	public boolean isSuppertExt(File file) {
		if (file.getName().toLowerCase().endsWith(".mp3")) {
			return true;
		}
		return false;

	}

}
