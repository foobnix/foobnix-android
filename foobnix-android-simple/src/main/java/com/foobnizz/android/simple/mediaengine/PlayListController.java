package com.foobnizz.android.simple.mediaengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayListController {
    private final List<MediaModel> list = new ArrayList<MediaModel>();
    private int active;
    private Random random = new Random();

    public PlayListController() {
        active = 0;
    }

    public int size() {
        return list.size();
    }

    public void remove(MediaModel model) {
        list.remove(model);
    }

    public MediaModel getAtPos(int pos) {
        if (pos > list.size()) {
            return null;
        }
        active = pos;
        return getItem();
    }

    public void addToPlaylist(List<MediaModel> items) {
        list.addAll(items);
    }

    public void setPlaylist(List<MediaModel> items) {
        active = 0;
        list.clear();
        list.addAll(items);
    }

    public void add(MediaModel Model) {
        list.add(Model);
    }

    public MediaModel getNextModel() {
        if (list.isEmpty()) {
            return null;
        }

        if (active < list.size() - 1) {
            active++;
        } else {
            active = 0;
        }

        return getItem();
    }

    public MediaModel getRandomModel() {
        if (!list.isEmpty()) {
            active = random.nextInt(list.size());
        }

        return getItem();

    }

    public MediaModel getPrevModel() {
        if (list.isEmpty()) {
            return null;
        }

        if (active > 0) {
            active--;
        } else {
            active = list.size() - 1;
        }
        return getItem();
    }

    public void clear() {
        list.clear();
        active = 0;
    }

    public List<MediaModel> getPlayList() {
        return list;
    }

    public void setActive(MediaModel model) {
        for (int i = 0; i < list.size(); i++) {
            if (model.equals(list.get(i))) {
                active = i;
                return;
            }
        }
    }

    public void setActive(int active) {
        this.active = active;
    }

    public MediaModel getItem() {
        return getCurrentModel();
    }

    public MediaModel getCurrentModel() {
        if (list.isEmpty()) {
            return null;
        }

        MediaModel model = list.get(active);
        // model.setPosition(active);
        return model;
    }

    public int getActive() {
        return active;
    }

}
