package com.foobnix.commons;

import java.io.File;

public class RecurciveFiles {

    public static boolean deleteFileOrDir(File rootFile) {
        if (rootFile.isDirectory()) {
            File[] contents = rootFile.listFiles();
            for (File file : contents) {
                deleteFileOrDir(file);
            }
        }
        return rootFile.delete();
    }
}
