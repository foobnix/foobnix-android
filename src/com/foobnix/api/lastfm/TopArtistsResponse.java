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
public class TopArtistsResponse extends LastFmResponse {

    @Element(name = "topartists")
    private TopArtists topArtists;

    public void setTopArtists(TopArtists topArtists) {
        this.topArtists = topArtists;
    }

    public TopArtists getTopArtists() {
        return topArtists;
    }

}
