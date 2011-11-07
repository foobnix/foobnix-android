package com.foobnix.android.widgets.navlist;

public class NavigationItem {
    private int resId;
    private String title;
    private String summary;

    public NavigationItem(int resId, String title, String summary) {
        this.resId = resId;
        this.title = title;
        this.summary = summary;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
