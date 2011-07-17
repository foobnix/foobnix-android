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
package com.foobnix.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;

import com.foobnix.exception.VKAuthorizationException;
import com.foobnix.model.FModel;
import com.foobnix.model.FModelBuilder;
import com.foobnix.model.SearchBy;
import com.foobnix.model.SearchQuery;
import com.foobnix.model.VKSong;
import com.foobnix.service.VKService;
import com.foobnix.util.Conf;
import com.foobnix.util.LOG;

import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Playlist;
import de.umass.lastfm.Tag;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;

public class OnlineProvider {

	private Context context;
	private final String apiKey = Conf.LAST_FM_API_KEY;

	public OnlineProvider(Context context) {
		this.context = context;
	}

	public List<FModel> findAlbumsByArtist(final String artist) {
		MusicHelper<Album> helper = new MusicHelper<Album>(Artist.getTopAlbums(artist, apiKey)) {
			@Override
			public FModel getModel(Album entry) {
				return FModelBuilder.Folder(entry.getName()).addArtist(artist).addAlbum(entry.getName());
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findUserTopTracks(String user) {
		MusicHelper<Track> helper = new MusicHelper<Track>(User.getTopTracks(user, apiKey)) {
			@Override
			public FModel getModel(Track entry) {
				return FModelBuilder.Track(entry.getArtist(), entry.getName(), entry.getAlbum());
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findUserFriends(String user) {
		MusicHelper<User> helper = new MusicHelper<User>(User.getFriends(user, apiKey)) {
			@Override
			public FModel getModel(User entry) {
				return FModelBuilder.SearchFolder(entry.getName(),
				        new SearchQuery(SearchBy.LAST_FM_USER, entry.getName()));
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findUserRecentTracks(String user) {
		MusicHelper<Track> helper = new MusicHelper<Track>(User.getRecentTracks(user, apiKey).getPageResults()) {
			@Override
			public FModel getModel(Track entry) {
				return FModelBuilder.Track(entry.getArtist(), entry.getName(), entry.getAlbum());
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findUserLovedTracks(String user) {
		MusicHelper<Track> helper = new MusicHelper<Track>(User.getLovedTracks(user, apiKey).getPageResults()) {
			@Override
			public FModel getModel(Track entry) {
				return FModelBuilder.Folder(entry.getName()).addTitle(entry.getName()).addArtist(entry.getArtist());
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findUserTopArtists(String user) {
		MusicHelper<Artist> helper = new MusicHelper<Artist>(User.getTopArtists(user, apiKey)) {
			@Override
			public FModel getModel(Artist entry) {
				return FModelBuilder.SearchFolder(entry.getName(),
				        new SearchQuery(SearchBy.LAST_FM_ARTIST, entry.getName()));

			}
		};
		return helper.getFModels();
	}

	public List<FModel> findSimilarArtistByArtist(final String artist) {
		MusicHelper<Artist> helper = new MusicHelper<Artist>(Artist.getSimilar(artist, apiKey)) {
			@Override
			public FModel getModel(Artist entry) {
				return FModelBuilder.Folder(entry.getName()).addArtist(entry.getName()).addParent(artist);
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findTagsByTag(final String tag) {
		MusicHelper<Tag> helper = new MusicHelper<Tag>(Tag.search(tag, apiKey)) {
			@Override
			public FModel getModel(Tag entry) {
				return FModelBuilder.Folder(entry.getName()).addTag(entry.getName()).addParent(tag);
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
		MusicHelper<Track> helper = new MusicHelper<Track>(Artist.getTopTracks(artist, apiKey)) {
			@Override
			public FModel getModel(Track entry) {
				return FModelBuilder.Track(entry.getArtist(), entry.getName(), entry.getAlbum());
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findTracksByTag(final String tag) {
		MusicHelper<Track> helper = new MusicHelper<Track>(Tag.getTopTracks(tag, apiKey)) {
			@Override
			public FModel getModel(Track entry) {
				return FModelBuilder.Track(entry.getName(), entry.getArtist(), entry.getAlbum());
			}
		};
		return helper.getFModels();

	}

	public List<FModel> findTracksByArtistAlbum(final String artist, final String album) {
		LOG.d("Seach tracks by ", artist, album);
		Album info = Album.getInfo(artist, album, apiKey);

		MusicHelper<Track> helper = new MusicHelper<Track>(Playlist.fetchAlbumPlaylist(info.getId(), apiKey)
		        .getTracks()) {
			@Override
			public FModel getModel(Track entry) {
				return FModelBuilder.Track(artist, entry.getName()).addAlbum(album).addTitle(entry.getName());
			}
		};
		return helper.getFModels();
	}

	abstract class MusicHelper<T> {
		Collection<T> musicEntris;

		public MusicHelper(Collection<T> musicEntris) {
			this.musicEntris = musicEntris;
		}

		public List<FModel> getFModels() {
			List<FModel> results = new ArrayList<FModel>();
			int num = 1;
			for (T model : musicEntris) {
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

		public abstract FModel getModel(T entry);

		public FModel getParent() {
			return null;
		}
	}
}
