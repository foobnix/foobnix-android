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
package com.foobnix.engine.store;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.foobnix.model.FModel;
import com.foobnix.util.LOG;

public class PrefPlayListStoreImpl implements PlayListStore {
	private final String FILENAME = "foobnix.obj";
	private Context context;
	private FileHelper helper;
	private List<FModel> cache;

	public PrefPlayListStoreImpl(Context context) {
		this.context = context;
		helper = new FileHelper();
		cache = helper.read();
	}

	@Override
	public long insert(FModel model) {
		cache.add(model);
		helper.write(cache);
		return 0;
	}

	@Override
	public void insertAll(List<FModel> list) {
		cache.addAll(list);
		helper.write(cache);
	}

	@Override
	public void delete(FModel model) {
		cache.remove(model);
		helper.write(cache);
	}

	@Override
	public void deleteAll() {
		cache.clear();
		helper.write(cache);
	}

	@Override
	public List<FModel> selectAll() {
		return cache;
	}

	class FileHelper {
		private void write(List<FModel> models) {
			try {
			FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(models);
			out.close();
			} catch (IOException e) {
				LOG.e("Write foobnix file error", e);
			}
		}

		private List<FModel> read() {

			List<FModel> result = new ArrayList<FModel>();
			try{
			FileInputStream fos = context.openFileInput(FILENAME);

			ObjectInputStream input = new ObjectInputStream(fos);
				result = (List<FModel>) input.readObject();
			} catch (IOException e) {
			} catch (ClassNotFoundException e) {
			}
			return result;
		}
	}


}
