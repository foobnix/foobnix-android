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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.foobnix.commons.string.StringUtils;
import com.foobnix.model.FModel;
import com.foobnix.model.FModelBuilder;
import com.foobnix.util.LOG;

public class DBPlayListStoreImpl implements PlayListStore {

	private static final int DATABASE_VERSION = 8;

	private static final String DATABASE_NAME = "foobnix.db";
	private static final String TABLE_NAME = "FModels";

	private Context context;
	private SQLiteDatabase db;

	private SQLiteStatement insertStmt;
	private static final String INSERT = "insert into " + TABLE_NAME
	        + "(uuid, name, path, time, type) values (?,?,?,?,?)";

	public DBPlayListStoreImpl(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(context);
		this.db = openHelper.getWritableDatabase();
		this.insertStmt = this.db.compileStatement(INSERT);
	}

	public long insert(FModel model) {
		LOG.d("insert " + model);
		this.insertStmt.bindLong(1, model.hashCode());
		this.insertStmt.bindString(2, valueOrEmpty(model.getText()));
		this.insertStmt.bindString(3, valueOrEmpty(model.getPath()));
		this.insertStmt.bindString(4, valueOrEmpty(model.getTime()));
		this.insertStmt.bindString(5, valueOrEmpty(model.getType().name()));
		return this.insertStmt.executeInsert();
	}

	public static String valueOrEmpty(String value) {
		if (StringUtils.isEmpty(value)) {
			return "";
		} else {
			return value;
		}
	}

	public void deleteAll() {
		LOG.d("deleteAll " + TABLE_NAME);
		this.db.delete(TABLE_NAME, null, null);
	}

	public List<FModel> selectAll() {
		LOG.d("selectAll " + TABLE_NAME);
		List<FModel> list = new ArrayList<FModel>();
		Cursor cursor = this.db.query(TABLE_NAME, new String[] { "uuid", "name", "path", "time", "type" }, null, null,
		        null, null, null);
		if (cursor.moveToFirst()) {
			do {

				int uuid = cursor.getInt(0);
				String name = cursor.getString(1);
				String path = cursor.getString(2);
				String time = cursor.getString(3);
				list.add(FModelBuilder.PatternText(name).addPath(path).addTime(time));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME
			        + "(id INTEGER PRIMARY KEY, name TEXT, path TEXT, uuid INTEGER, time TEXT, type TEXT)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Example", "Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}

	@Override
	public void insertAll(List<FModel> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(FModel model) {
		// TODO Auto-generated method stub
	}
}