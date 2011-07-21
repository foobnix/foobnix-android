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

import com.foobnix.api.vkontakte.VkAlbum;
import com.foobnix.api.vkontakte.VkApiCache;
import com.foobnix.api.vkontakte.VkApiCalls;
import com.foobnix.api.vkontakte.VkGroup;
import com.foobnix.api.vkontakte.VkHelper;
import com.foobnix.model.FModel;
import com.foobnix.model.FModelBuilder;
import com.foobnix.model.SearchBy;
import com.foobnix.model.SearchQuery;
import com.foobnix.model.VKUser;
import com.foobnix.model.VkAudio;

public class VkontakteApiAdapter {
    private VkApiCalls vkApi;

    public VkontakteApiAdapter(String token) {
        vkApi = new VkApiCache(token);
    }

    public void setToken(String token) {
        vkApi.setToken(token);
    }

    public VkAudio getMostRelevantSong(String query) {
        return VkHelper.getMostRelevantSong(vkApi.audioSearch(query));
    }

    public List<FModel> search(String query) {
        AdapterHelper<VkAudio> helper = new AdapterHelper<VkAudio>(vkApi.audioSearch(query)) {
            @Override
            public FModel getModel(VkAudio entry) {
                return FModelBuilder.VkAudio(entry);
            }
        };
        return helper.getFModels();
    }

    public List<FModel> getUserFriends(final String uid) {
        AdapterHelper<VKUser> helper = new AdapterHelper<VKUser>(vkApi.getUserFriends(uid)) {
            @Override
            public FModel getModel(VKUser entry) {
                return FModelBuilder.Folder(entry.getFirstName() + " " + entry.getLastName())//
                        .addSearchQuery(new SearchQuery(SearchBy.VK_USER_ID, entry.getUid()));
            }
        };
        return helper.getFModels();
    }

    public List<FModel> getUserGroups() {
        AdapterHelper<VkGroup> helper = new AdapterHelper<VkGroup>(vkApi.getUserGroups()) {
            @Override
            public FModel getModel(VkGroup entry) {
                return FModelBuilder.Folder(entry.getName())//
                        .addSearchQuery(new SearchQuery(SearchBy.VK_GROUP_ID, entry.getGid(), entry.getName()));
            }
        };
        return helper.getFModels();
    }

    public List<FModel> getUserAudio(final String uid) {
        AdapterHelper<VkAudio> helper = new AdapterHelper<VkAudio>(vkApi.getUserAudio(uid)) {
            @Override
            public FModel getModel(VkAudio entry) {
                return FModelBuilder.VkAudio(entry);
            }
        };
        return helper.getFModels();
    }

    public List<FModel> getGroupAudio(final String gid) {
        AdapterHelper<VkAudio> helper = new AdapterHelper<VkAudio>(vkApi.getGroupAudio(gid)) {
            @Override
            public FModel getModel(VkAudio entry) {
                return FModelBuilder.VkAudio(entry);
            }
        };
        return helper.getFModels();
    }

    public List<FModel> getUserAlbums(final String uid) {
        AdapterHelper<VkAlbum> helper = new AdapterHelper<VkAlbum>(vkApi.getUserAlbums(uid)) {
            @Override
            public FModel getModel(VkAlbum entry) {
                return FModelBuilder.Folder(entry.getTitle()).addSearchQuery(
                        new SearchQuery(SearchBy.VK_USER_ALBUM_AUIO, entry.getOwnerId(), entry.getAlbumId()));
            }
        };
        return helper.getFModels();
    }

    public List<FModel> getGroupAlbums(final String gid) {
        AdapterHelper<VkAlbum> helper = new AdapterHelper<VkAlbum>(vkApi.getGroupAlbums(gid)) {
            @Override
            public FModel getModel(VkAlbum entry) {
                return FModelBuilder.Folder(entry.getTitle()).addSearchQuery(
                        new SearchQuery(SearchBy.VK_USER_ALBUM_AUIO, entry.getOwnerId(), entry.getAlbumId()));
            }
        };
        return helper.getFModels();
    }

    public List<FModel> getUserAlbumAudio(final String uid, final String albumId) {
        AdapterHelper<VkAudio> helper = new AdapterHelper<VkAudio>(vkApi.getUserAlbumAudio(uid, albumId)) {
            @Override
            public FModel getModel(VkAudio entry) {
                return FModelBuilder.Track(entry.getArtist(), entry.getTitle());
            }
        };
        return helper.getFModels();
    }

    public List<FModel> getGroupAlbumAudio(final String gid, final String albumId) {
        AdapterHelper<VkAudio> helper = new AdapterHelper<VkAudio>(vkApi.getGroupAlbumAudio(gid, albumId)) {
            @Override
            public FModel getModel(VkAudio entry) {
                return FModelBuilder.Track(entry.getArtist(), entry.getTitle());
            }
        };
        return helper.getFModels();
    }

    public VkApiCalls getVkApi() {
        return vkApi;
    }

}
