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
package com.foobnizz.android.simple.integrations;

import java.util.List;

import com.foobnix.commons.LOG;
import com.foobnix.commons.TimeUtil;
import com.foobnizz.android.simple.mediaengine.MediaModel;

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

    public List<MediaModel> findAlbumsByArtist(final String artist) {
		AdapterHelper<Album> helper = new AdapterHelper<Album>(Artist.getTopAlbums(artist, apiKey)) {
			@Override
            public MediaModel getModel(Album entry) {
                return MediaModelBuilder.Folder(//
				        entry.getName()).addArtist(artist).addAlbum(entry.getName())
				        .addYear(TimeUtil.getYear(entry.getReleaseDate()))//
				        .addSearchQuery(new SearchQuery(SearchBy.TRACKS_BY_ALBUM, artist, entry.getName()));
			}
		};
        return helper.getMediaModels();
	}

    public List<MediaModel> findUserTopTracks(String user) {
		AdapterHelper<Track> helper = new AdapterHelper<Track>(User.getTopTracks(user, apiKey)) {
			@Override
            public MediaModel getModel(Track entry) {
                return MediaModelBuilder.Track(entry);
			}
		};
        return helper.getMediaModels();
	}

    public List<MediaModel> findUserFriends(String user) {
		AdapterHelper<User> helper = new AdapterHelper<User>(User.getFriends(user, apiKey)) {
			@Override
            public MediaModel getModel(User entry) {
                return MediaModelBuilder.SearchFolder(entry.getName(),
				        new SearchQuery(SearchBy.LAST_FM_USER, entry.getName()));
			}
		};
        return helper.getMediaModels();
	}

    public List<MediaModel> findUserNeighbours(String user) {
		AdapterHelper<User> helper = new AdapterHelper<User>(User.getNeighbours(user, apiKey)) {
			@Override
            public MediaModel getModel(User entry) {
                return MediaModelBuilder.SearchFolder(entry.getName(),
				        new SearchQuery(SearchBy.LAST_FM_USER, entry.getName()));
			}
		};
        return helper.getMediaModels();
	}

    public List<MediaModel> findUserRecentTracks(String user) {
		AdapterHelper<Track> helper = new AdapterHelper<Track>(User.getRecentTracks(user, apiKey).getPageResults()) {
			@Override
            public MediaModel getModel(Track entry) {
                return MediaModelBuilder.Track(entry);
			}
		};
        return helper.getMediaModels();
	}

    public List<MediaModel> findUserLovedTracks(String user) {
		AdapterHelper<Track> helper = new AdapterHelper<Track>(User.getLovedTracks(user, apiKey).getPageResults()) {
			@Override
            public MediaModel getModel(Track entry) {
                return MediaModelBuilder.Track(entry);
			}
		};
        return helper.getMediaModels();
	}

    public List<MediaModel> findUserTopArtists(String user) {
		AdapterHelper<Artist> helper = new AdapterHelper<Artist>(User.getTopArtists(user, apiKey)) {
			@Override
            public MediaModel getModel(Artist entry) {
                return MediaModelBuilder.SearchFolder(entry.getName(),
				        new SearchQuery(SearchBy.LAST_FM_ARTIST, entry.getName()));

			}
		};
        return helper.getMediaModels();
	}


    public List<MediaModel> findSimilarArtistByArtist(final String artist) {
		AdapterHelper<Artist> helper = new AdapterHelper<Artist>(Artist.getSimilar(artist, apiKey)) {
			@Override
            public MediaModel getModel(Artist entry) {
                return MediaModelBuilder.SearchFolder(entry.getName(),
				        new SearchQuery(SearchBy.LAST_FM_ARTIST, entry.getName())).addArtist(entry.getName());
			}
		};
        return helper.getMediaModels();
	}

    public List<MediaModel> findTagsByTag(final String tag) {
		AdapterHelper<Tag> helper = new AdapterHelper<Tag>(Tag.search(tag, apiKey)) {
			@Override
            public MediaModel getModel(Tag entry) {
                return MediaModelBuilder.SearchFolder(entry.getName(),
				        new SearchQuery(SearchBy.TRACKS_BY_TAG, entry.getName()));
			}
		};
        return helper.getMediaModels();

	}

    public List<MediaModel> findTracksByArtist(final String artist) {
		AdapterHelper<Track> helper = new AdapterHelper<Track>(Artist.getTopTracks(artist, apiKey)) {
			@Override
            public MediaModel getModel(Track entry) {
                return MediaModelBuilder.Track(entry);
			}
		};
        return helper.getMediaModels();
	}

    public List<MediaModel> findTracksByTag(final String tag) {
		AdapterHelper<Track> helper = new AdapterHelper<Track>(Tag.getTopTracks(tag, apiKey)) {
			@Override
            public MediaModel getModel(Track entry) {
                return MediaModelBuilder.Track(entry);
			}
		};
        return helper.getMediaModels();

	}

    public List<MediaModel> findTracksByArtistAlbum(final String artist, final String album) {
		LOG.d("Seach tracks by ", artist, album);
		Album info = Album.getInfo(artist, album, apiKey);

		AdapterHelper<Track> helper = new AdapterHelper<Track>(Playlist.fetchAlbumPlaylist(info.getId(), apiKey)
		        .getTracks()) {
			@Override
            public MediaModel getModel(Track entry) {
                return MediaModelBuilder.Track(entry);
			}
		};
        return helper.getMediaModels();
	}

	/**
	 * @param param1
	 * @param param2
	 * @return
	 */
    public List<MediaModel> findSimilarTracksByTrack(String artist, String track) {
		LOG.d("Seach similar tracks by ", artist, track);

		AdapterHelper<Track> helper = new AdapterHelper<Track>(Track.getSimilar(artist, track, apiKey)) {
			@Override
            public MediaModel getModel(Track entry) {
                return MediaModelBuilder.Track(entry.getArtist(), entry.getName()).addAlbum(entry.getAlbum());
			}
		};
        return helper.getMediaModels();
    }

}
