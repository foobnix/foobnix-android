/**
 * 
 */
package com.foobnix.provider;

import java.util.List;

import com.foobnix.exception.VKAuthorizationException;
import com.foobnix.model.FModel;

/**
 * @author iivanenko
 *
 */
public interface LastFmMethods {

    public abstract List<FModel> findAlbumsByArtist(final String artist);

    public abstract List<FModel> findUserTopTracks(String user);

    public abstract List<FModel> findUserFriends(String user);

    public abstract List<FModel> findUserRecentTracks(String user);

    public abstract List<FModel> findUserLovedTracks(String user);

    public abstract List<FModel> findUserTopArtists(String user);

    public abstract List<FModel> findSimilarArtistByArtist(final String artist);

    public abstract List<FModel> findTagsByTag(final String tag);

    public abstract List<FModel> findTracksByVK(String q) throws VKAuthorizationException;

    public abstract List<FModel> findTracksByArtist(final String artist);

    public abstract List<FModel> findTracksByTag(final String tag);

    public abstract List<FModel> findTracksByArtistAlbum(final String artist, final String album);

}