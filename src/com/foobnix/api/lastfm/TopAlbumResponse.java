/**
 * 
 */
package com.foobnix.api.lastfm;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author iivanenko
 * 
 */
@Root(name = "lfm")
public class TopAlbumResponse extends LastFmResponse {

    @Element(name = "topalbums")
    private TopAlbums topAlbums;

    public void setTopAlbums(TopAlbums topAlbums) {
        this.topAlbums = topAlbums;
    }

    public TopAlbums getTopAlbums() {
        return topAlbums;
    }

}
