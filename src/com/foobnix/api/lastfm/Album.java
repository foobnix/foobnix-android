/**
 * 
 */
package com.foobnix.api.lastfm;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * @author iivanenko
 * 
 */
@Root(name = "album")
public class Album {

    @Attribute
    private int rank;

    @Element
    private String name;

    @Element
    private int playcount;

    @Element(required = false)
    private String mbid;

    @Element(required = false)
    private String url;

    @Element(name = "artist")
    private ArtistShort artist;

    @ElementList(inline = true)
    private List<Image> images;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArtistShort getArtist() {
        return artist;
    }

    public void setArtist(ArtistShort artist) {
        this.artist = artist;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

}
