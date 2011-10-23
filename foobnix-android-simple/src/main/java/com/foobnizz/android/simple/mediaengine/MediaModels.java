package com.foobnizz.android.simple.mediaengine;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class MediaModels implements Serializable {
    private final List<MediaModel> items;

    public static MediaModels empty() {
        return new MediaModels(Collections.EMPTY_LIST);
    }

    public MediaModels(List<MediaModel> items) {
        this.items = items;
    }

    public List<MediaModel> getItems() {
        return items;
    }

}
