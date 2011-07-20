/**
 * 
 */
package com.foobnix.api.lastfm;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

/**
 * @author iivanenko
 * 
 */
public class TopAlbums extends Statistics {
    @Attribute
    private String artist;

    @ElementList(inline = true)
    private List<Album> albums;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

}