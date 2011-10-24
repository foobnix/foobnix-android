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
package com.foobnix.integrations;

import java.util.List;

import com.foobnix.exception.VkErrorException;
import com.foobnix.mediaengine.MediaModel;
import com.foobnix.vkontakte.VKUser;
import com.foobnix.vkontakte.VkAlbum;
import com.foobnix.vkontakte.VkApiCache;
import com.foobnix.vkontakte.VkApiCalls;
import com.foobnix.vkontakte.VkAudio;
import com.foobnix.vkontakte.VkGroup;
import com.foobnix.vkontakte.VkHelper;


public class VkontakteApiAdapter {
    private VkApiCalls vkApi;

    public VkontakteApiAdapter(String token) {
        vkApi = new VkApiCache(token);
    }

    public void setToken(String token) {
        vkApi.setToken(token);
    }

	public VkAudio getMostRelevantSong(String query) throws VkErrorException {
        return VkHelper.getMostRelevantSong(vkApi.audioSearch(query));
    }

    public List<MediaModel> search(String query) throws VkErrorException {
        AdapterHelper<VkAudio> helper = new AdapterHelper<VkAudio>(vkApi.audioSearch(query)) {
            @Override
            public MediaModel getModel(VkAudio entry) {
                return MediaModelBuilder.VkAudio(entry);
            }
        };
        return helper.getMediaModels();
    }

    public List<MediaModel> getUserFriends(final String uid) throws VkErrorException {
        AdapterHelper<VKUser> helper = new AdapterHelper<VKUser>(vkApi.getUserFriends(uid)) {
            @Override
            public MediaModel getModel(VKUser entry) {
                return MediaModelBuilder.Folder(entry.getFirstName() + " " + entry.getLastName())//
                        .addSearchQuery(new SearchQuery(SearchBy.VK_USER_ID, entry.getUid()));
            }
        };
        return helper.getMediaModels();
    }

    public List<MediaModel> getUserGroups() throws VkErrorException {
        AdapterHelper<VkGroup> helper = new AdapterHelper<VkGroup>(vkApi.getUserGroups()) {
            @Override
            public MediaModel getModel(VkGroup entry) {
                return MediaModelBuilder.Folder(entry.getName())//
                        .addSearchQuery(new SearchQuery(SearchBy.VK_GROUP_ID, entry.getGid(), entry.getName()));
            }
        };
        return helper.getMediaModels();
    }

    public List<MediaModel> getUserAudio(final String uid) throws VkErrorException {
        AdapterHelper<VkAudio> helper = new AdapterHelper<VkAudio>(vkApi.getUserAudio(uid)) {
            @Override
            public MediaModel getModel(VkAudio entry) {
                return MediaModelBuilder.VkAudio(entry);
            }
        };
        return helper.getMediaModels();
    }

    public List<MediaModel> getGroupAudio(final String gid) throws VkErrorException {
        AdapterHelper<VkAudio> helper = new AdapterHelper<VkAudio>(vkApi.getGroupAudio(gid)) {
            @Override
            public MediaModel getModel(VkAudio entry) {
                return MediaModelBuilder.VkAudio(entry);
            }
        };
        return helper.getMediaModels();
    }

    public List<MediaModel> getUserAlbums(final String uid) throws VkErrorException {
        AdapterHelper<VkAlbum> helper = new AdapterHelper<VkAlbum>(vkApi.getUserAlbums(uid)) {
            @Override
            public MediaModel getModel(VkAlbum entry) {
                return MediaModelBuilder.Folder(entry.getTitle()).addSearchQuery(
                        new SearchQuery(SearchBy.VK_USER_ALBUM_AUIO, entry.getOwnerId(), entry.getAlbumId()));
            }
        };
        return helper.getMediaModels();
    }

    public List<MediaModel> getGroupAlbums(final String gid) throws VkErrorException {
        AdapterHelper<VkAlbum> helper = new AdapterHelper<VkAlbum>(vkApi.getGroupAlbums(gid)) {
            @Override
            public MediaModel getModel(VkAlbum entry) {
                return MediaModelBuilder.Folder(entry.getTitle()).addSearchQuery(
                        new SearchQuery(SearchBy.VK_USER_ALBUM_AUIO, entry.getOwnerId(), entry.getAlbumId()));
            }
        };
        return helper.getMediaModels();
    }

    public List<MediaModel> getUserAlbumAudio(final String uid, final String albumId) throws VkErrorException {
        AdapterHelper<VkAudio> helper = new AdapterHelper<VkAudio>(vkApi.getUserAlbumAudio(uid, albumId)) {
            @Override
            public MediaModel getModel(VkAudio entry) {
				return MediaModelBuilder.VkAudio(entry);
            }
        };
        return helper.getMediaModels();
    }

    public List<MediaModel> getGroupAlbumAudio(final String gid, final String albumId)
	        throws VkErrorException {
        AdapterHelper<VkAudio> helper = new AdapterHelper<VkAudio>(vkApi.getGroupAlbumAudio(gid, albumId)) {
            @Override
            public MediaModel getModel(VkAudio entry) {
				return MediaModelBuilder.VkAudio(entry);
            }
        };
        return helper.getMediaModels();
    }

    public VkApiCalls getVkApi() {
        return vkApi;
    }

}
