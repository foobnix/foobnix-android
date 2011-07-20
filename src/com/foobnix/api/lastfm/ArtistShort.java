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
@Root(name = "artist")
public class ArtistShort {
    @Element
    private String name;

    @Element(required = false)
    private String mbid;

    @Element(required = false)
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
