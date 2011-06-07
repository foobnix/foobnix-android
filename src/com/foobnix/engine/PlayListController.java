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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;

import com.foobnix.model.FModel;

public class PlayListController {
	private final List<FModel> list = new ArrayList<FModel>();
	private int active;
	private Random random = new Random();

	public PlayListController(Context context) {
		List<String> res = Collections.emptyList();

		active = 0;
	}

	public void remove(FModel model) {
		list.remove(model);
	}

	public FModel getAtPos(int pos){
		if (pos > list.size()) {
			return null;
		}
		active = pos;
		return getItem();
	}

	public void addAll(List<FModel> FModels) {
		list.addAll(FModels);
	}

	public void setAll(List<FModel> FModels) {
		active = 0;
		list.clear();
		list.addAll(FModels);
	}

	public void add(FModel FModel) {
		list.add(FModel);
	}

	public FModel getNextFModel() {
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


	public FModel getRandomFModel() {
		active = random.nextInt(list.size());
		return getItem();
	}

	public FModel getPrevFModel() {
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

	public List<FModel> getPlayList() {
		return list;
	}

	public void setActive(FModel model) {
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

	public FModel getItem() {
		return getCurrentFModel();
	}

	public FModel getCurrentFModel() {
		if (list.isEmpty()) {
			return null;
		}
		FModel model = list.get(active);
		model.setPosition(active);
		return model;
	}

	public int getActive() {
	    return active;
    }

}
