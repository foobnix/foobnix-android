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
import java.util.Stack;

import android.content.Context;

import com.foobnix.exception.VKAuthorizationException;
import com.foobnix.model.FModel;
import com.foobnix.model.FModelBuilder;
import com.foobnix.model.SearchBy;
import com.foobnix.model.SearchQuery;
import com.foobnix.util.LOG;

public class SearchQueryManager {

	private final OnlineProvider provider;
	private Stack<SearchQuery> stack;

	public SearchQueryManager(Context context) {
		provider = new OnlineProvider(context);
		stack = new Stack<SearchQuery>();
	}

	public List<FModel> previousPage() {
		if (stack.size() <= 1) {
			return Collections.EMPTY_LIST;
		}

		stack.pop();
		SearchQuery back = stack.pop();
		LOG.d("previous result page:", back.getSearchBy(), back.getParam1(), back.getParam2());
		return getSearchResult(back);
	}

	public List<FModel> getSearchResult(SearchQuery searchQuery) {

		LOG.d("Search By L1", searchQuery.getSearchBy(), searchQuery.getParam1(), searchQuery.getParam2());

		if (searchQuery.getSearchBy() == SearchBy.BACK_BUTTON) {
			stack.pop();
			SearchQuery back = stack.pop();
			LOG.d("back", back.getSearchBy(), back.getParam1(), back.getParam2());
			return getSearchResult(back);
		} else {
			List<FModel> result = new ArrayList<FModel>();

			if (!stack.isEmpty()) {
				FModel back = FModelBuilder.SearchFolder("..", new SearchQuery(SearchBy.BACK_BUTTON, ".."));
				result.add(back);
			}

			stack.push(searchQuery);
			result.addAll(getSearchResultProccess(searchQuery));
			return result;
		}

	}

	public List<FModel> getSearchResultProccess(SearchQuery searchQuery) {
		LOG.d("Search By", searchQuery.getSearchBy(), searchQuery.getParam1(), searchQuery.getParam2());

		switch (searchQuery.getSearchBy()) {

		case LAST_FM_USER:
			List<FModel> items = new ArrayList<FModel>();
			items.add(FModelBuilder.SearchFolder(//
			        String.format("%s - %s", searchQuery.getParam1(), "Top Tracks"),//
			        new SearchQuery(SearchBy.TOP_TRACKS_BY_USER, searchQuery.getParam1())));

			items.add(FModelBuilder.SearchFolder(//
			        String.format("%s - %s", searchQuery.getParam1(), "Recent Tracks"),//
			        new SearchQuery(SearchBy.RECENT_TRACKS_BY_USER, searchQuery.getParam1())));

			items.add(FModelBuilder.SearchFolder(//
			        String.format("%s - %s", searchQuery.getParam1(), "Loved Tracks"),//
			        new SearchQuery(SearchBy.LOVED_TRACKS_BY_USER, searchQuery.getParam1())));

			items.add(FModelBuilder.SearchFolder(//
			        String.format("%s - %s", searchQuery.getParam1(), "Top Artists"),//
			        new SearchQuery(SearchBy.TOP_ARTISTS_BY_USER, searchQuery.getParam1())));

			items.add(FModelBuilder.SearchFolder(//
			        String.format("%s - %s", searchQuery.getParam1(), "Friends"),//
			        new SearchQuery(SearchBy.LAST_FM_FRIENDS_BY_USER, searchQuery.getParam1())));

			return items;

		case LAST_FM_ARTIST:
			List<FModel> options = new ArrayList<FModel>();
			options.add(FModelBuilder.Search(//
			        String.format("%s - %s", searchQuery.getParam1(), "Top Tracks"),//
			        new SearchQuery(SearchBy.TOP_TRACKS_BY_ARTIST, searchQuery.getParam1())));

			options.add(FModelBuilder.Search(//
			        String.format("%s - %s", searchQuery.getParam1(), "Top Albums"),//
			        new SearchQuery(SearchBy.ALBUMS_BY_ARTIST, searchQuery.getParam1())));

			options.add(FModelBuilder.Search(//
			        String.format("%s - %s", searchQuery.getParam1(), "Similar Artists"),//
			        new SearchQuery(SearchBy.SIMILAR_ARTISTS_BY_ARTIST, searchQuery.getParam1())));

			return options;

		case TOP_TRACKS_BY_ARTIST:
			return provider.findTracksByArtist(searchQuery.getParam1());

		case ALBUMS_BY_ARTIST:
			return provider.findAlbumsByArtist(searchQuery.getParam1());

		case SIMILAR_ARTISTS_BY_ARTIST:
			return provider.findSimilarArtistByArtist(searchQuery.getParam1());

		case TAGS_BY_TAG:
			return provider.findTagsByTag(searchQuery.getParam1());

		case ALL_AUDIO:
			try {
				return provider.findTracksByVK(searchQuery.getParam1());
			} catch (VKAuthorizationException e) {
				new RuntimeException("to fix");
			}

		case TRACKS_BY_ALBUM:
			return provider.findTracksByArtistAlbum(searchQuery.getParam1(), searchQuery.getParam2());

		case TRACKS_BY_TAG:
			return provider.findTracksByTag(searchQuery.getParam1());

		case RECENT_TRACKS_BY_USER:
			return provider.findUserRecentTracks(searchQuery.getParam1());

		case TOP_TRACKS_BY_USER:
			return provider.findUserTopTracks(searchQuery.getParam1());

		case LOVED_TRACKS_BY_USER:
			return provider.findUserLovedTracks(searchQuery.getParam1());

		case TOP_ARTISTS_BY_USER:
			return provider.findUserTopArtists(searchQuery.getParam1());

		case LAST_FM_FRIENDS_BY_USER:
			return provider.findUserFriends(searchQuery.getParam1());

		}

		LOG.d("Search method not found");

		return Collections.EMPTY_LIST;
	}

}
