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
import java.util.Collections;
import java.util.List;

import android.content.Context;

import com.foobnix.R;
import com.foobnix.model.FModel;
import com.foobnix.model.FModelBuilder;
import com.foobnix.model.SearchBy;
import com.foobnix.model.SearchQuery;
import com.foobnix.model.VKUser;
import com.foobnix.util.LOG;
import com.foobnix.util.Pref;
import com.foobnix.util.PrefKeys;
import com.foobnix.util.Res;

public class IntegrationsQueryManager extends StackQueryManager {

    private final VkontakteApiAdapter vkAdapter;
    private final LastFmApiAdapter lfmAdapter;

    public IntegrationsQueryManager(Context context) {
        vkAdapter = new VkontakteApiAdapter(Pref.getStr(context, PrefKeys.VKONTAKTE_TOKEN));
        lfmAdapter = new LastFmApiAdapter(Res.getStr(context, R.string.LAST_FM_API_KEY));
    }

    @Override
    protected List<FModel> getSearchResultProccess(SearchQuery searchQuery) {
        LOG.d("Search By", searchQuery.getSearchBy(), searchQuery.getParam1(), searchQuery.getParam2());

        switch (searchQuery.getSearchBy()) {

        case SEARCH:
            String artist = searchQuery.getParam1();
            String track = searchQuery.getParam1();
            List<FModel> sitems = new ArrayList<FModel>();
            sitems.add(FModelBuilder.SearchFolder(//
                    String.format("%s: %s", "Vkontakte Search", artist),//
                    new SearchQuery(SearchBy.ALL_AUDIO, artist)));

            sitems.add(FModelBuilder.SearchFolder(//
                    String.format("%s: %s", "Tracks By Artist", artist),//
                    new SearchQuery(SearchBy.TRACKS_BY_ARTIST, artist)));

            sitems.add(FModelBuilder.SearchFolder(//
                    String.format("%s: %s", "Albums By Artist", artist),//
                    new SearchQuery(SearchBy.ALBUMS_BY_ARTIST, artist)));

            sitems.add(FModelBuilder.SearchFolder(//
                    String.format("%s: %s", "Similar Artists By Artist", artist),//
                    new SearchQuery(SearchBy.SIMILAR_ARTISTS_BY_ARTIST, artist)));

            sitems.add(FModelBuilder.SearchFolder(//
                    String.format("%s: %s", "Similar Tracks By Track", track),//
                    new SearchQuery(SearchBy.SIMILAR_TRACKS_BY_TRACK, artist, track)));

            return sitems;

        case VK_USER_ID:
            VKUser user = vkAdapter.getVkApi().getUserProfile(searchQuery.getParam1());
            String name = user.getFirstName() + " " + user.getLastName();

            List<FModel> items = new ArrayList<FModel>();
            items.add(FModelBuilder.SearchFolder(//
                    String.format("%s - %s", name, "Music"),//
                    new SearchQuery(SearchBy.VK_USER_AUDIO, searchQuery.getParam1())));

            items.add(FModelBuilder.SearchFolder(//
                    String.format("%s - %s", name, "Albums"),//
                    new SearchQuery(SearchBy.VK_USER_ALBUMS, searchQuery.getParam1())));

            if (stack.size() < 2) {

                items.add(FModelBuilder.SearchFolder(//
                        String.format("%s - %s", name, "Friends"),//
                        new SearchQuery(SearchBy.VK_USER_FRINDS, searchQuery.getParam1())));

                items.add(FModelBuilder.SearchFolder(//
                        String.format("%s - %s", name, "Groups"),//
                        new SearchQuery(SearchBy.VK_USER_GROUPS, searchQuery.getParam1())));
            }
            return items;

        case VK_GROUP_ID:

            List<FModel> groups = new ArrayList<FModel>();
            groups.add(FModelBuilder.SearchFolder(//
                    String.format("%s - %s", searchQuery.getParam2(), "Music"),//
                    new SearchQuery(SearchBy.VK_GROUP_AUDIO, searchQuery.getParam1(), searchQuery.getParam2())));

            groups.add(FModelBuilder.SearchFolder(//
                    String.format("%s - %s", searchQuery.getParam2(), "Albums"),//
                    new SearchQuery(SearchBy.VK_GROUP_ALBUMS, searchQuery.getParam1())));
            return groups;

        case VK_USER_ALBUM_AUIO:
            return vkAdapter.getUserAlbumAudio(searchQuery.getParam1(), searchQuery.getParam2());

        case VK_GROUP_ALBUM_AUDIO:
            return vkAdapter.getGroupAlbumAudio(searchQuery.getParam1(), searchQuery.getParam2());

        case VK_USER_AUDIO:
            return vkAdapter.getUserAudio(searchQuery.getParam1());

        case VK_GROUP_AUDIO:
            return vkAdapter.getGroupAudio(searchQuery.getParam1());

        case VK_USER_ALBUMS:
            return vkAdapter.getUserAlbums(searchQuery.getParam1());

        case VK_GROUP_ALBUMS:
            return vkAdapter.getGroupAlbums(searchQuery.getParam1());

        case VK_USER_FRINDS:
            return vkAdapter.getUserFriends(searchQuery.getParam1());

        case VK_USER_GROUPS:
            return vkAdapter.getUserGroups();

            // //////////

        case LAST_FM_USER:
            List<FModel> uitems = new ArrayList<FModel>();
            uitems.add(FModelBuilder.SearchFolder(//
                    String.format("%s - %s", searchQuery.getParam1(), "Top Tracks"),//
                    new SearchQuery(SearchBy.TOP_TRACKS_BY_USER, searchQuery.getParam1())));

            uitems.add(FModelBuilder.SearchFolder(//
                    String.format("%s - %s", searchQuery.getParam1(), "Recent Tracks"),//
                    new SearchQuery(SearchBy.RECENT_TRACKS_BY_USER, searchQuery.getParam1())));

            uitems.add(FModelBuilder.SearchFolder(//
                    String.format("%s - %s", searchQuery.getParam1(), "Loved Tracks"),//
                    new SearchQuery(SearchBy.LOVED_TRACKS_BY_USER, searchQuery.getParam1())));

            uitems.add(FModelBuilder.SearchFolder(//
                    String.format("%s - %s", searchQuery.getParam1(), "Top Artists"),//
                    new SearchQuery(SearchBy.TOP_ARTISTS_BY_USER, searchQuery.getParam1())));

            uitems.add(FModelBuilder.SearchFolder(//
                    String.format("%s - %s", searchQuery.getParam1(), "Friends"),//
                    new SearchQuery(SearchBy.LAST_FM_FRIENDS_BY_USER, searchQuery.getParam1())));

            return uitems;

        case LAST_FM_ARTIST:
            List<FModel> options = new ArrayList<FModel>();
            options.add(FModelBuilder.SearchFolder(//
                    String.format("%s - %s", searchQuery.getParam1(), "Top Tracks"),//
                    new SearchQuery(SearchBy.TRACKS_BY_ARTIST, searchQuery.getParam1())));

            options.add(FModelBuilder.SearchFolder(//
                    String.format("%s - %s", searchQuery.getParam1(), "Top Albums"),//
                    new SearchQuery(SearchBy.ALBUMS_BY_ARTIST, searchQuery.getParam1())));

            options.add(FModelBuilder.SearchFolder(//
                    String.format("%s - %s", searchQuery.getParam1(), "Similar Artists"),//
                    new SearchQuery(SearchBy.SIMILAR_ARTISTS_BY_ARTIST, searchQuery.getParam1())));

            return options;

        case TRACKS_BY_ARTIST:
            return lfmAdapter.findTracksByArtist(searchQuery.getParam1());

        case ALBUMS_BY_ARTIST:
            return lfmAdapter.findAlbumsByArtist(searchQuery.getParam1());

        case SIMILAR_ARTISTS_BY_ARTIST:
            return lfmAdapter.findSimilarArtistByArtist(searchQuery.getParam1());

        case TAGS_BY_TAG:
            return lfmAdapter.findTagsByTag(searchQuery.getParam1());

        case ALL_AUDIO:
            return vkAdapter.search(searchQuery.getParam1());

        case TRACKS_BY_ALBUM:
            return lfmAdapter.findTracksByArtistAlbum(searchQuery.getParam1(), searchQuery.getParam2());

        case TRACKS_BY_TAG:
            return lfmAdapter.findTracksByTag(searchQuery.getParam1());

        case RECENT_TRACKS_BY_USER:
            return lfmAdapter.findUserRecentTracks(searchQuery.getParam1());

        case TOP_TRACKS_BY_USER:
            return lfmAdapter.findUserTopTracks(searchQuery.getParam1());

        case LOVED_TRACKS_BY_USER:
            return lfmAdapter.findUserLovedTracks(searchQuery.getParam1());

        case TOP_ARTISTS_BY_USER:
            return lfmAdapter.findUserTopArtists(searchQuery.getParam1());

        case LAST_FM_FRIENDS_BY_USER:
            return lfmAdapter.findUserFriends(searchQuery.getParam1());

        }

        return Collections.EMPTY_LIST;
    }

    public VkontakteApiAdapter getVkAdapter() {
        return vkAdapter;
    }

    public LastFmApiAdapter getLfmAdapter() {
        return lfmAdapter;
    }
}