package com.foobnix.mediaengine;

import java.io.Serializable;
import java.util.List;

public class MediaModels implements Serializable {
    private final List<MediaModel> items;

    public MediaModels(List<MediaModel> items) {
        this.items = items;
    }

    public List<MediaModel> getItems() {
        return items;
    }

}
