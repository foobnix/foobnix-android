package com.foobnix.core;

import java.io.File;


public class FileExceptDirFilter extends FileExtFilter {

    public FileExceptDirFilter(String exts[]) {
        super(exts);
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return false;
        }
        return super.accept(file);
    }

}
