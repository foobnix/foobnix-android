package com.foobnix.commons.io;

import java.io.File;
import java.io.FileFilter;

public class FileExtFilter implements FileFilter {

    private final String[] exts;

    public FileExtFilter(String[] exts) {
        this.exts = exts;
    }

    @Override
    public boolean accept(File file) {
        if (exts == null) {
            return true;
        }
        for (String ext : exts) {
            if (file.getName().toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;

    }

}