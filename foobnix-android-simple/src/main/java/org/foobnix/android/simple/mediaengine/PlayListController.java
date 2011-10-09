package org.foobnix.android.simple.mediaengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayListController {
    private final List<Model> list = new ArrayList<Model>();
    private int active;
    private Random random = new Random();

    public PlayListController() {
        active = 0;
    }

    public int size() {
        return list.size();
    }

    public void remove(Model model) {
        list.remove(model);
    }

    public Model getAtPos(int pos) {
        if (pos > list.size()) {
            return null;
        }
        active = pos;
        return getItem();
    }

    public void addToPlaylist(List<Model> items) {
        list.addAll(items);
    }

    public void setPlaylist(List<Model> items) {
        active = 0;
        list.clear();
        list.addAll(items);
    }

    public void add(Model Model) {
        list.add(Model);
    }

    public Model getNextModel() {
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

    public Model getRandomModel() {
        if (!list.isEmpty()) {
            active = random.nextInt(list.size());
        }

        return getItem();

    }

    public Model getPrevModel() {
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

    public List<Model> getPlayList() {
        return list;
    }

    public void setActive(Model model) {
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

    public Model getItem() {
        return getCurrentModel();
    }

    public Model getCurrentModel() {
        if (list.isEmpty()) {
            return null;
        }

        Model model = list.get(active);
        // model.setPosition(active);
        return model;
    }

    public int getActive() {
        return active;
    }

}
