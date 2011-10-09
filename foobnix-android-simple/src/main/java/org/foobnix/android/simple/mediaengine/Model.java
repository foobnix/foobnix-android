package org.foobnix.android.simple.mediaengine;

import java.io.Serializable;

public class Model implements Serializable {
    private String name;
    private String path;
    private int position;

    public Model(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Model) {
            return path.equals(((Model) obj).getPath()) && name.equals(((Model) obj).getName());
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

}
