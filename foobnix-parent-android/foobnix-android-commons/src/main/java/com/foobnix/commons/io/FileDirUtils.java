package com.foobnix.commons.io;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileDirUtils {
    private final static FileOrderComparator orderComparator = new FileOrderComparator();

    public static List<File> getRecurcive(File rootPath, String[] exts) {
        return getRecurciveList(rootPath, new FileExtFilter(exts));
    };

    public static List<File> getRecurciveList(File rootPath, FileFilter filter) {
        List<File> items = new ArrayList<File>();
        items.add(rootPath);

        if (rootPath.isDirectory()) {
            for (File file : getList(rootPath, filter)) {
                if (file.isDirectory()) {
                    items.addAll(getRecurciveList(file, filter));
                } else {
                    items.add(file);
                }
            }
        }
        return items;
    };

    public static List<File> getList(File rootPath, String[] exts) {
        return getList(rootPath, new FileExtOrDirFilter(exts));
    };

    public static List<File> getList(File rootPath, FileFilter filter) {
        if (rootPath == null) {
            throw new IllegalArgumentException("filepath not set");
        }

        if (filter == null) {
            throw new IllegalArgumentException("filter not set");
        }

        final List<File> items = new ArrayList<File>();

        if (rootPath.isFile() && filter.accept(rootPath)) {
            items.add(rootPath);
            return items;
        }

        File[] listFiles = rootPath.listFiles(filter);

        if (listFiles == null) {
            return Collections.emptyList();
        }

        for (File file : listFiles) {
            items.add(file);
        }

        Collections.sort(items, orderComparator);
        return items;
    };

}
