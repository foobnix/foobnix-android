package org.foobnix.android.simple.core;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileItemProvider {
	public final static FileItemComparator comparator = new FileItemComparator();
	public final static Mp3FileNameFilter filter = new Mp3FileNameFilter();
	
	public static List<FileItem> getFilesAndFoldersByPath(File rootPath){
		final List<FileItem> items = new ArrayList<FileItem>();
		File[] listFiles = rootPath.listFiles(filter);
		if (listFiles == null) {
			return Collections.emptyList();
		}
        for (File file : listFiles) {
            items.add(new FileItem(file));
        }
        Collections.sort(items,comparator);
        return items;
		
	}

    public static List<FileItem> getFilesByPath(File rootPath) {
        final List<FileItem> items = new ArrayList<FileItem>();
        File[] listFiles = rootPath.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        if (listFiles == null) {
            return Collections.emptyList();
        }
        for (File file : listFiles) {
            items.add(new FileItem(file));
        }
        Collections.sort(items, comparator);
        return items;

    }

}

