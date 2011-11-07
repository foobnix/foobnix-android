package com.foobnix.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.foobnix.commons.io.FileExtFilter;
import com.foobnix.commons.io.FileExtOrDirFilter;
import com.foobnix.commons.io.FileDirUtils;
import com.foobnix.commons.log.LOG;

public class FileItemProvider {
    private final static File ROOT_PATH = Environment.getExternalStorageDirectory();
    private final static FileExtFilter MP3_FILTER = new FileExtFilter(new String[] { ".mp3" });
    private final static FileExtOrDirFilter MP3_WITH_DIR_FILTER = new FileExtOrDirFilter(new String[] { ".mp3" });
    
    public static List<FileItem> getRootList(File rootPath, String[] exts) {
        List<FileItem> items = new ArrayList<FileItem>();
       
        if (!rootPath.equals(ROOT_PATH)) {
            items.add(new TopFileItem(rootPath.getParentFile()));
        }
        items.addAll(getList(rootPath, exts));
        return items;
        
    }

    public static List<FileItem> getList(File rootPath, String[] exts) {
        List<File> list = FileDirUtils.getList(rootPath, MP3_WITH_DIR_FILTER);
        LOG.d("getList size", rootPath, list.size());

        final List<FileItem> items = new ArrayList<FileItem>();
        for (File file : list) {
            items.add(new FileItem(file));
        }
        return items;
    }

    public static List<FileItem> getFilesByPath(File rootPath) {
        LOG.d("get getFilesByPath", rootPath.getPath());

        List<File> listFiles = FileDirUtils.getList(rootPath, MP3_FILTER);

        List<FileItem> items = new ArrayList<FileItem>();
        for (File file : listFiles) {
            items.add(new FileItem(file));
        }

        return items;

    }

}
