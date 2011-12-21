/* Copyright (c) 2011 Ivan Ivanenko <ivan.ivanenko@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. */
package com.foobnix.engine;

import java.util.List;

import android.content.Context;

import com.foobnix.engine.store.PlayListStore;
import com.foobnix.engine.store.PrefPlayListStoreImpl;
import com.foobnix.model.FModel;

public class PlaylistManager {
	
	private PlayListController playListController;
	private PlayListStore playlistStore;

	public PlaylistManager(Context context) {
		playlistStore = new PrefPlayListStoreImpl(context);
		playListController = new PlayListController(context);
		playListController.setAll(playlistStore.selectAll());
	}

	public List<FModel> getAll() {
		return playListController.getPlayList();
	}

	public void clear() {
		playlistStore.deleteAll();
		playListController.clear();
	}

	public void remove(FModel model){
		playlistStore.delete(model);
		playListController.remove(model);
	}

	public void add(FModel model) {
		playlistStore.insert(model);
		playListController.add(model);
	}


	public void setList(List<FModel> FModels) {
		clear();
		addAll(FModels);
	}

	public void addAll(List<FModel> list) {
		playlistStore.insertAll(list);
		playListController.addAll(list);

	}

	public PlayListController getPlayListController() {
	    return playListController;
    }

}
