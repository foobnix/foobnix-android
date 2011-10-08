package org.foobnix.android.simple.mediaengine;

import java.io.Serializable;
import java.util.List;

public class Models implements Serializable {
    private final List<Model> items;

    public Models(List<Model> items) {
        this.items = items;
    }

    public List<Model> getItems() {
        return items;
    }

}
