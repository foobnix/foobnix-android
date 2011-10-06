package com.foobnix.commons;

import java.io.File;

public class RecurciveFiles {

    public static boolean deleteFileOrDir(File rootFile) {
        if (rootFile.isDirectory()) {
            File[] contents = rootFile.listFiles();
            boolean success = true;
            for (File file : contents) {
                success &= deleteFileOrDir(file);
            }
            return success;
        }
        return rootFile.delete();
    }
}
