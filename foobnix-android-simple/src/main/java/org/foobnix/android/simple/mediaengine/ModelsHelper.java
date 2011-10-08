package org.foobnix.android.simple.mediaengine;


import java.util.ArrayList;
import java.util.List;

import org.foobnix.android.simple.core.FileItem;

public class ModelsHelper {
    
    public static Models getModelsByFileItems(List<FileItem> fileitems) {
        List<Model> result = new ArrayList<Model>();
        for (FileItem item : fileitems) {
            result.add(new Model(item.getFile().getName(), item.getFile().getPath()));
        }
        return new Models(result);
    }
    

}
