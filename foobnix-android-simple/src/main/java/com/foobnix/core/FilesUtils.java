package com.foobnix.core;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilesUtils {
    private final static FileOrderComparator orderComparator = new FileOrderComparator();

    public static List<File> getRecurcive(File rootPath, String[] formats) {
        List<File> items = new ArrayList<File>();
        if (rootPath.isDirectory()) {
            items.addAll(getRecurcive(rootPath, formats));
        } else {
            items.add(rootPath);
        }
        return items;
    };

    public static List<File> getList(File rootPath, String[] exts) {
        return getList(rootPath, new FileExtFilter(exts));
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
