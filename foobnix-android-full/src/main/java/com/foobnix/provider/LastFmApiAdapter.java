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

import java.util.List;

import com.foobnix.model.FModel;
import com.foobnix.model.FModelBuilder;
import com.foobnix.model.SearchBy;
import com.foobnix.model.SearchQuery;
import com.foobnix.util.LOG;
import com.foobnix.util.TimeUtil;

import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Playlist;
import de.umass.lastfm.Tag;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;

public class LastFmApiAdapter {

	private final String apiKey;

	public LastFmApiAdapter(String apiKey) {
		this.apiKey = apiKey;
	}

	public List<FModel> findAlbumsByArtist(final String artist) {
		AdapterHelper<Album> helper = new AdapterHelper<Album>(Artist.getTopAlbums(artist, apiKey)) {
			@Override
			public FModel getModel(Album entry) {
				return FModelBuilder.Folder(//
				        entry.getName()).addArtist(artist).addAlbum(entry.getName())
				        .addYear(TimeUtil.getYear(entry.getReleaseDate()))//
				        .addSearchQuery(new SearchQuery(SearchBy.TRACKS_BY_ALBUM, artist, entry.getName()));
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findUserTopTracks(String user) {
		AdapterHelper<Track> helper = new AdapterHelper<Track>(User.getTopTracks(user, apiKey)) {
			@Override
			public FModel getModel(Track entry) {
				return FModelBuilder.Track(entry);
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findUserFriends(String user) {
		AdapterHelper<User> helper = new AdapterHelper<User>(User.getFriends(user, apiKey)) {
			@Override
			public FModel getModel(User entry) {
				return FModelBuilder.SearchFolder(entry.getName(),
				        new SearchQuery(SearchBy.LAST_FM_USER, entry.getName()));
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findUserNeighbours(String user) {
		AdapterHelper<User> helper = new AdapterHelper<User>(User.getNeighbours(user, apiKey)) {
			@Override
			public FModel getModel(User entry) {
				return FModelBuilder.SearchFolder(entry.getName(),
				        new SearchQuery(SearchBy.LAST_FM_USER, entry.getName()));
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findUserRecentTracks(String user) {
		AdapterHelper<Track> helper = new AdapterHelper<Track>(User.getRecentTracks(user, apiKey).getPageResults()) {
			@Override
			public FModel getModel(Track entry) {
				return FModelBuilder.Track(entry);
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findUserLovedTracks(String user) {
		AdapterHelper<Track> helper = new AdapterHelper<Track>(User.getLovedTracks(user, apiKey).getPageResults()) {
			@Override
			public FModel getModel(Track entry) {
				return FModelBuilder.Track(entry);
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findUserTopArtists(String user) {
		AdapterHelper<Artist> helper = new AdapterHelper<Artist>(User.getTopArtists(user, apiKey)) {
			@Override
			public FModel getModel(Artist entry) {
				return FModelBuilder.SearchFolder(entry.getName(),
				        new SearchQuery(SearchBy.LAST_FM_ARTIST, entry.getName()));

			}
		};
		return helper.getFModels();
	}


	public List<FModel> findSimilarArtistByArtist(final String artist) {
		AdapterHelper<Artist> helper = new AdapterHelper<Artist>(Artist.getSimilar(artist, apiKey)) {
			@Override
			public FModel getModel(Artist entry) {
				return FModelBuilder.SearchFolder(entry.getName(),
				        new SearchQuery(SearchBy.LAST_FM_ARTIST, entry.getName())).addArtist(entry.getName());
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findTagsByTag(final String tag) {
		AdapterHelper<Tag> helper = new AdapterHelper<Tag>(Tag.search(tag, apiKey)) {
			@Override
			public FModel getModel(Tag entry) {
				return FModelBuilder.SearchFolder(entry.getName(),
				        new SearchQuery(SearchBy.TRACKS_BY_TAG, entry.getName()));
			}
		};
		return helper.getFModels();

	}

	public List<FModel> findTracksByArtist(final String artist) {
		AdapterHelper<Track> helper = new AdapterHelper<Track>(Artist.getTopTracks(artist, apiKey)) {
			@Override
			public FModel getModel(Track entry) {
				return FModelBuilder.Track(entry);
			}
		};
		return helper.getFModels();
	}

	public List<FModel> findTracksByTag(final String tag) {
		AdapterHelper<Track> helper = new AdapterHelper<Track>(Tag.getTopTracks(tag, apiKey)) {
			@Override
			public FModel getModel(Track entry) {
				return FModelBuilder.Track(entry);
			}
		};
		return helper.getFModels();

	}

	public List<FModel> findTracksByArtistAlbum(final String artist, final String album) {
		LOG.d("Seach tracks by ", artist, album);
		Album info = Album.getInfo(artist, album, apiKey);

		AdapterHelper<Track> helper = new AdapterHelper<Track>(Playlist.fetchAlbumPlaylist(info.getId(), apiKey)
		        .getTracks()) {
			@Override
			public FModel getModel(Track entry) {
				return FModelBuilder.Track(entry);
			}
		};
		return helper.getFModels();
	}

	/**
	 * @param param1
	 * @param param2
	 * @return
	 */
	public List<FModel> findSimilarTracksByTrack(String artist, String track) {
		LOG.d("Seach similar tracks by ", artist, track);

		AdapterHelper<Track> helper = new AdapterHelper<Track>(Track.getSimilar(artist, track, apiKey)) {
			@Override
			public FModel getModel(Track entry) {
				return FModelBuilder.Track(entry.getArtist(), entry.getName()).addAlbum(entry.getAlbum());
			}
		};
		return helper.getFModels();
    }

}
