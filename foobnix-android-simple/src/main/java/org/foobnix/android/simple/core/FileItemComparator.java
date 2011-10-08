package org.foobnix.android.simple.core;

import java.util.Comparator;

public class FileItemComparator implements Comparator<FileItem> {

	@Override
	public int compare(FileItem arg0, FileItem arg1) {
		if(arg0.getFile().isDirectory() && arg1.getFile().isFile()){
			return -1;
		} else if (arg0.getFile().isFile() && arg1.getFile().isDirectory()) {
			return 1;
		};
		
		return arg0.getFile().getPath().compareTo(arg1.getFile().getPath());
	}

}
