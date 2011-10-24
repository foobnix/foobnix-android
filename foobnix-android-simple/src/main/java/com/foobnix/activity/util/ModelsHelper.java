package com.foobnix.activity.util;

import java.util.ArrayList;
import java.util.List;

import com.foobnix.core.FileItem;
import com.foobnix.mediaengine.MediaModel;
import com.foobnix.mediaengine.MediaModels;

public class ModelsHelper {

    public static MediaModels getModelsByFileItems(List<FileItem> fileitems) {
        List<MediaModel> result = new ArrayList<MediaModel>();
        int i = 0;
        for (FileItem item : fileitems) {
            MediaModel model = new MediaModel(item.getFile().getName(), item.getFile().getPath());
            model.setPosition(i++);
            result.add(model);
        }
        return new MediaModels(result);
    }

}
