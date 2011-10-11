package org.foobnix.android.simple.mediaengine;

import java.io.Serializable;

public class Model implements Serializable {
    private String title;
    private String artist;
    private String path;
    private int position;
    private String duration = "00:00";

    public Model(String title, String path) {
        this.title = title;
        this.path = path;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Model) {
            return path.equals(((Model) obj).getPath());
        }
        return false;
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

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

}
