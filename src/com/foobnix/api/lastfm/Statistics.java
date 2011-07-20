/**
 * 
 */
package com.foobnix.api.lastfm;

import org.simpleframework.xml.Attribute;

/**
 * @author iivanenko
 * 
 */
public class Statistics {

    @Attribute
    private int page;

    @Attribute
    private int perPage;

    @Attribute
    private int totalPages;

    @Attribute
    private int total;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
