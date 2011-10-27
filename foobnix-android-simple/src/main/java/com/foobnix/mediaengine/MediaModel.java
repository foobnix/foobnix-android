package com.foobnix.mediaengine;

import java.io.Serializable;

import com.foobnix.commons.string.StringUtils;
import com.foobnix.integrations.SearchQuery;

public class MediaModel implements Serializable {

    private SearchQuery searchQuery;
    private String title = "";
    private String artist;
    private String path = "";
    private int position;
    private String duration = "00:00";
    private boolean isFile;

    public MediaModel(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public MediaModel(String title) {
        this.title = title;
    }

    private String artistTitle;

    public String getArtistTitle() {
        if (artistTitle != null) {
            return artistTitle;
        }
        if (StringUtils.isNotEmpty(artist)) {
            artistTitle = String.format("%s - %s", artist, title);
        } else {
            artistTitle = title;
        }
        return artistTitle;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MediaModel) {
            return hashCode() == obj.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return title.toLowerCase().replace(" ", "").hashCode();
    }

    public String getText() {
        return title;
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

    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    public void setFile(boolean isFile) {
        this.isFile = isFile;
    }

    public boolean isFile() {
        return isFile;
    }

}
