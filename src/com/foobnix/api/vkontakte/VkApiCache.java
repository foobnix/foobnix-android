/**
 * 
 */
package com.foobnix.api.vkontakte;

import java.util.List;

import com.foobnix.model.VKUser;
import com.foobnix.model.VkAudio;
import com.foobnix.util.memcache.CacheTask;
import com.foobnix.util.memcache.MemCache;

/**
 * @author iivanenko
 * 
 */
public class VkApiCache implements VkApiCalls {

    private VkApi vkApi;
    private MemCache memCache = new MemCache();

    public VkApiCache(String token) {
        vkApi = new VkApi(token);
    }

    @Override
    public void setToken(String token) {
        vkApi.setToken(token);

    }

    @Override
    public List<VkAudio> audioSearch(final String text) {
        CacheTask<List<VkAudio>> task = new CacheTask<List<VkAudio>>() {
            @Override
            public List<VkAudio> run() {
                return vkApi.audioSearch(text);
            }
        };
        return memCache.getOrUpdate("audioSearch_" + text, MemCache.DAY, task);
    }

    @Override
    public List<VkAudio> getUserAudio(final String uid) {
        CacheTask<List<VkAudio>> task = new CacheTask<List<VkAudio>>() {
            @Override
            public List<VkAudio> run() {
                return vkApi.getUserAudio(uid);
            }
        };
        return memCache.getOrUpdate("getUserAudio_" + uid, MemCache.MIN * 10, task);
    }

    @Override
    public List<VkAlbum> getUserAlbums(final String uid) {
        CacheTask<List<VkAlbum>> task = new CacheTask<List<VkAlbum>>() {
            @Override
            public List<VkAlbum> run() {
                return vkApi.getUserAlbums(uid);
            }
        };
        return memCache.getOrUpdate("getUserAlbums_" + uid, MemCache.DAY, task);
    }

    @Override
    public List<VkAlbum> getGroupAlbums(final String gid) {
        CacheTask<List<VkAlbum>> task = new CacheTask<List<VkAlbum>>() {
            @Override
            public List<VkAlbum> run() {
                return vkApi.getGroupAlbums(gid);
            }
        };
        return memCache.getOrUpdate("getGroupAlbums_" + gid, MemCache.DAY, task);
    }

    @Override
    public List<VkAudio> getGroupAudio(final String gid) {
        CacheTask<List<VkAudio>> task = new CacheTask<List<VkAudio>>() {
            @Override
            public List<VkAudio> run() {
                return vkApi.getGroupAudio(gid);
            }
        };
        return memCache.getOrUpdate("getGroupAudio_" + gid, MemCache.DAY, task);
    }

    @Override
    public List<VkAudio> getUserAlbumAudio(final String uid, final String albumId) {
        CacheTask<List<VkAudio>> task = new CacheTask<List<VkAudio>>() {
            @Override
            public List<VkAudio> run() {
                return vkApi.getUserAlbumAudio(uid, albumId);
            }
        };
        return memCache.getOrUpdate("getUserAlbumAudio_" + uid + albumId, MemCache.DAY, task);
    }

    @Override
    public List<VkAudio> getGroupAlbumAudio(final String gid, final String albumId) {
        CacheTask<List<VkAudio>> task = new CacheTask<List<VkAudio>>() {
            @Override
            public List<VkAudio> run() {
                return vkApi.getGroupAlbumAudio(gid, albumId);
            }
        };
        return memCache.getOrUpdate("getGroupAlbumAudio_" + gid + albumId, MemCache.DAY, task);
    }

    @Override
    public List<VkGroup> getUserGroups() {
        CacheTask<List<VkGroup>> task = new CacheTask<List<VkGroup>>() {
            @Override
            public List<VkGroup> run() {
                return vkApi.getUserGroups();
            }
        };
        return memCache.getOrUpdate("getUserGroups", MemCache.DAY, task);
    }

    @Override
    public List<VKUser> getUserFriends(final String uid) {
        CacheTask<List<VKUser>> task = new CacheTask<List<VKUser>>() {
            @Override
            public List<VKUser> run() {
                return vkApi.getUserFriends(uid);
            }
        };
        return memCache.getOrUpdate("getUserFriends", MemCache.DAY, task);
    }

    @Override
    public VKUser getUserProfile(final String uid) {
        CacheTask<VKUser> task = new CacheTask<VKUser>() {
            @Override
            public VKUser run() {
                return vkApi.getUserProfile(uid);
            }
        };
        return memCache.getOrUpdate("getUserProfile", MemCache.DAY, task);
    }

}
