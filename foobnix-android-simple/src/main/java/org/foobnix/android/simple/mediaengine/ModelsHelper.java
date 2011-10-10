package org.foobnix.android.simple.mediaengine;


import java.util.ArrayList;
import java.util.List;

import org.foobnix.android.simple.core.FileItem;

public class ModelsHelper {
    
    public static Models getModelsByFileItems(List<FileItem> fileitems) {
        List<Model> result = new ArrayList<Model>();
        int i = 0;
        for (FileItem item : fileitems) {
            Model model = new Model(item.getFile().getName(), item.getFile().getPath());
            model.setPosition(i++);
            result.add(model);
        }
        return new Models(result);
    }
    

}
