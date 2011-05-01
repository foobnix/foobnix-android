package com.foobnix.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.foobnix.log.LOG;
import com.foobnix.model.Song;

public class DataHelper {

	private static final int DATABASE_VERSION = 5;

	private static final String DATABASE_NAME = "foobnix.db";
	private static final String TABLE_NAME = "songs";

	private Context context;
	private SQLiteDatabase db;

	private SQLiteStatement insertStmt;
	private static final String INSERT = "insert into " + TABLE_NAME + "(name, path, uuid) values (?,?, ?)";

	public DataHelper(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(context);
		this.db = openHelper.getWritableDatabase();
		this.insertStmt = this.db.compileStatement(INSERT);
	}

	public long insert(Song song) {
		LOG.d("insert " + song);
		this.insertStmt.bindString(1, song.getText());
		this.insertStmt.bindString(2, song.getPath());
		this.insertStmt.bindLong(3, song.getUuid());
		return this.insertStmt.executeInsert();
	}

	public void deleteAll() {
		LOG.d("deleteAll " + TABLE_NAME);
		this.db.delete(TABLE_NAME, null, null);
	}

	public List<Song> selectAll() {
		LOG.d("selectAll " + TABLE_NAME);
		List<Song> list = new ArrayList<Song>();
		Cursor cursor = this.db
		        .query(TABLE_NAME, new String[] { "name", "path", "uuid" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {

				String name = cursor.getString(0);
				String path = cursor.getString(1);
				int uuid = cursor.getInt(2);

				list.add(new Song(name, path, uuid));
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
			db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY, name TEXT, path TEXT, uuid INTEGER)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Example", "Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}
}