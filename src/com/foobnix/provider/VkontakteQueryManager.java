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

import com.foobnix.model.FModel;
import com.foobnix.model.FModelBuilder;
import com.foobnix.model.SearchBy;
import com.foobnix.model.SearchQuery;
import com.foobnix.model.VKUser;
import com.foobnix.util.LOG;

public class VkontakteQueryManager extends StackQueryManager {

	private final VkontakteApiAdapter provider;

	public VkontakteQueryManager(VkontakteApiAdapter provider) {
		this.provider = provider;
	}

	@Override
	public List<FModel> getSearchResultProccess(SearchQuery searchQuery) {
		LOG.d("Search By", searchQuery.getSearchBy(), searchQuery.getParam1(), searchQuery.getParam2());

		switch (searchQuery.getSearchBy()) {

		case VK_USER_ID:
			VKUser user = provider.getVkApi().getUserProfile(searchQuery.getParam1());
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
			return provider.getUserAlbumAudio(searchQuery.getParam1(), searchQuery.getParam2());

		case VK_GROUP_ALBUM_AUDIO:
			return provider.getGroupAlbumAudio(searchQuery.getParam1(), searchQuery.getParam2());

		case VK_USER_AUDIO:
			return provider.getUserAudio(searchQuery.getParam1());

		case VK_GROUP_AUDIO:
			return provider.getGroupAudio(searchQuery.getParam1());

		case VK_USER_ALBUMS:
			return provider.getUserAlbums(searchQuery.getParam1());

		case VK_GROUP_ALBUMS:
			return provider.getGroupAlbums(searchQuery.getParam1());

		case VK_USER_FRINDS:
			return provider.getUserFriends(searchQuery.getParam1());

		case VK_USER_GROUPS:
			return provider.getUserGroups();

		}
		return Collections.EMPTY_LIST;
	}
}