package com.foobnix.model;

public class PrefItem {
    private int resId;
    private String title;
    private String summary;
    private Runnable action;

    public PrefItem(int resId, String title, String summary, Runnable action) {
        this.resId = resId;
        this.title = title;
        this.summary = summary;
        this.action = action;
    }

    public PrefItem(int resId, String title, String summary) {
        this.resId = resId;
        this.title = title;
        this.summary = summary;
    }

    public PrefItem(int resId, String title) {
        this.resId = resId;
        this.title = title;
    }

    public PrefItem(String title, String summary) {
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

    public void setAction(Runnable action) {
        this.action = action;
    }

    public Runnable getAction() {
        return action;
    }

}
