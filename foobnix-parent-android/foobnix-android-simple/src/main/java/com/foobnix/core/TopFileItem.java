package com.foobnix.core;

import java.io.File;

public class TopFileItem extends FileItem {
    
    public TopFileItem(File file) {
        super(file);
        setDisplayName("..");
    }
    

}
