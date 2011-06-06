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
package com.foobnix.model;

import java.io.Serializable;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.foobnix.ui.activity.OnlineActivity.SEARCH_BY;
import com.foobnix.util.SongUtil;
import com.foobnix.util.TimeUtil;

public class FModel implements Serializable {
	private static final long serialVersionUID = 277665175970179591L;

	public enum TYPE {
		LOCAL, ONLINE, RADIO;
	};

	public enum DOWNLOAD_STATUS {
		DONE, EXIST, NEW, FAIL, ACTIVE
	}

	private TYPE type = TYPE.LOCAL;
	private DOWNLOAD_STATUS status = DOWNLOAD_STATUS.NEW;
	private SEARCH_BY searchBy = SEARCH_BY.ALL_AUDIO;

	private String parent;
	private String tag;
	private String album;
	private String text;
	private String artist;
	private String title;

	private boolean isFile;
	private String path;
	private String downloadTo;

	private String ext;
	private String time;
	private String size;
	private String image;
	private int UUID;

	private int position;
	private int percent;
	private boolean isScrobbled;

	public static FModel Empty() {
		return FModel.File("").addArtist("").addTitle("").addUUID(Integer.MIN_VALUE);
	}

	public static FModel Parent(SEARCH_BY searchBy) {
		return FModel.Folder("..").addSearchBy(searchBy);
	}

	public static FModel File(String text) {
		String artist = SongUtil.getArtist(text);
		String title = SongUtil.getTitle(text);
		return new FModel(text).addIsFile(true).addArtist(artist).addTitle(title);
	}

	public static FModel Search(String text, SEARCH_BY by) {
		return new FModel(text).addIsFile(true).addArtist(text).addTitle(text).addTag(text).addAlbum(text)
		        .addSearchBy(by);
	}

	public static FModel Folder(String text) {
		return new FModel(text).addIsFile(false);
	}

	public FModel addParent(String parent) {
		this.parent = parent;
		return this;
	}

	public FModel addType(TYPE type) {
		this.type = type;
		return this;
	}

	public FModel addUUID(int uuid) {
		this.UUID = uuid;
		return this;
	}

	public FModel addArtist(String artist) {
		this.artist = artist;
		return this;
	}

	public FModel addTag(String tag) {
		this.tag = tag;
		return this;
	}

	public FModel addSearchBy(SEARCH_BY by) {
		this.searchBy = by;
		return this;
	}

	public FModel addAlbum(String album) {
		this.album = album;
		return this;
	}

	public FModel addTitle(String title) {
		this.title = title;
		return this;
	}

	public FModel addIsFile(boolean isFile) {
		this.isFile = isFile;
		return this;
	}

	public FModel addPath(String path) {
		this.path = path;
		return this;
	}

	public FModel addExt(String ext) {
		this.ext = ext;
		return this;
	}

	public FModel addSize(String size) {
		this.size = size;
		return this;
	}

	public FModel addTime(String time) {
		this.time = time;
		return this;
	}

	public FModel() {

	}

	private FModel(String text) {
		this.text = text;
		this.UUID = new Random().nextInt();
	}

	public FModel(VKSong song) {
		this.isFile = true;
		this.artist = song.getArtist();
		this.title = song.getTitle();
		this.time = TimeUtil.durationSecToString(song.getDuration());
		this.path = song.getUrl();
		this.UUID = new Random().nextInt();
	}

	public DOWNLOAD_STATUS getStatus() {
		return status;
	}

	public void setStatus(DOWNLOAD_STATUS status) {
		this.status = status;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isScrobbled() {
		return isScrobbled;
	}

	public void setScrobbled(boolean scrobbled) {
		this.isScrobbled = scrobbled;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public String getText() {
		if (StringUtils.isNotEmpty(artist) && StringUtils.isNotEmpty(title)) {
			return String.format("%s - %s", artist, title);
		}
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isFile() {
		return isFile;
	}

	public boolean isFolder() {
		return !isFile;
	}

	public void setFile(boolean file) {
		this.isFile = file;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime() {
		return time;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getExt() {
		return ext;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSize() {
		return size;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public TYPE getType() {
		return type;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getArtist() {
		return artist;
	}

	public void setDownloadTo(String downloadTo) {
		this.downloadTo = downloadTo;
	}

	public String getDownloadTo() {
		return downloadTo;
	}

	@Override
	public int hashCode() {
		return UUID;
	}

	public void setUUID(int uuid) {
		this.UUID = uuid;
	}

	public int getUUID() {
		return UUID;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getAlbum() {
		return album;
	}

	public void setSearchBy(SEARCH_BY searchBy) {
		this.searchBy = searchBy;
	}

	public SEARCH_BY getSearchBy() {
		return searchBy;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getParent() {
		return parent;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof FModel)) {
			return false;
		}
		return o.hashCode() == hashCode();
	}

	@Override
	public String toString() {
		return new StringBuilder().append("FModel Text:").append(text).append(" Artist: ").append(artist)
		        .append(" Title: ").append(title).append(" Path: ").append(path).toString();
	}

}
