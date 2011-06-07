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
package com.foobnix.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;

import com.foobnix.exception.VKAuthorizationException;
import com.foobnix.model.FModel;
import com.foobnix.model.FModelBuilder;
import com.foobnix.model.VKSong;
import com.foobnix.ui.activity.OnlineActivity.SEARCH_BY;
import com.foobnix.util.Conf;
import com.foobnix.util.LOG;

import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.MusicEntry;
import de.umass.lastfm.Playlist;
import de.umass.lastfm.Tag;
import de.umass.lastfm.Track;

public class OnlineManager {
	private Context context;
	private final String apiKey = Conf.LAST_FM_API_KEY;
	
	public OnlineManager(Context context){
		this.context = context;
	}
	public List<FModel> findAlbumsByArtist(final String artist) {
		MusicHelper helper = new MusicHelper(Artist.getTopAlbums(artist, apiKey)) {
			@Override
			public FModel getModel(MusicEntry entry) {
				return FModelBuilder.Folder(entry.getName()).addSearchBy(SEARCH_BY.TRACKS_BY_ALBUM).addArtist(artist)
				        .addAlbum(entry.getName());
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findSimilarArtistByArtist(final String artist) {
		MusicHelper helper = new MusicHelper(Artist.getSimilar(artist, apiKey)) {
			@Override
			public FModel getModel(MusicEntry entry) {
				return FModelBuilder.Folder(entry.getName()).addSearchBy(SEARCH_BY.TRACKS_BY_ARTIST)
				        .addArtist(entry.getName()).addParent(artist);
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findTagsByTag(final String tag) {
		MusicHelper helper = new MusicHelper(Tag.search(tag, apiKey)) {
			@Override
			public FModel getModel(MusicEntry entry) {
				return FModelBuilder.Folder(entry.getName()).addTag(entry.getName())
				        .addSearchBy(SEARCH_BY.TRACKS_BY_TAG)
				        .addParent(tag);
			}
		};
		return helper.getFModels();

	}

	public List<FModel> findTracksByVK(String q) throws VKAuthorizationException {
		List<FModel> results = new ArrayList<FModel>();
		List<VKSong> searchAll = VKService.searchAll(q, context);
		for (VKSong model : searchAll) {
			results.add(FModelBuilder.CreateFromVK(model));
		}
		return results;
	}

	public List<FModel> findTracksByArtist(final String artist) {
		MusicHelper helper = new MusicHelper(Artist.getTopTracks(artist, apiKey)) {
			@Override
			public FModel getModel(MusicEntry entry) {
				return FModelBuilder.CreateFromText(artist, entry.getName());
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findTracksByTag(final String tag) {
		MusicHelper helper = new MusicHelper(Tag.getTopTracks(tag, apiKey)) {
			@Override
			public FModel getModel(MusicEntry entry) {
				return FModelBuilder.CreateFromText(((Track) entry).getArtist(), entry.getName())
				        .addSearchBy(SEARCH_BY.TRACKS_BY_TAG).addTag(entry.getName()).addParent(tag);
			}
			@Override
			public FModel getParent() {
				return FModelBuilder.Parent(SEARCH_BY.TAGS_BY_TAG).addTag(tag);
			}
		};
		return helper.getFModels();
		
	}

	public List<FModel> findTracksByArtistAlbum(final String artist, final String album) {
		LOG.d("Seach tracks by ", artist, album);
		Album info = Album.getInfo(artist, album, apiKey);

		MusicHelper helper = new MusicHelper(Playlist.fetchAlbumPlaylist(info.getId(), apiKey).getTracks()) {
			@Override
			public FModel getModel(MusicEntry entry) {
				return FModelBuilder.CreateFromText(artist, entry.getName()).addAlbum(album).addTitle(entry.getName());
			}

			@Override
			public FModel getParent() {
				return FModelBuilder.Parent(SEARCH_BY.ALBUMS_BY_ARTIST).addArtist(artist).addAlbum(album);
			}
		};
		return helper.getFModels();
	}

	abstract class MusicHelper {
		Collection<? extends MusicEntry> musicEntris;

		public MusicHelper(Collection<? extends MusicEntry> musicEntris) {
			this.musicEntris = musicEntris;
		}

		public List<FModel> getFModels() {
			List<FModel> results = new ArrayList<FModel>();
			int num = 1;
			for (MusicEntry model : musicEntris) {
				FModel fmodel = getModel(model);
				fmodel.setTrackNum(num);
				results.add(fmodel);
				num++;
			}
			if (getParent() != null) {
				results.add(0, getParent());
			}
			return results;
		}

		public abstract FModel getModel(MusicEntry entry);

		public FModel getParent() {
			return null;
		}
	}
}
