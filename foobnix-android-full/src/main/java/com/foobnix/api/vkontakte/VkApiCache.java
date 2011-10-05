/**
 * 
 */
package com.foobnix.api.vkontakte;

import java.util.List;

import com.foobnix.exception.VkErrorException;
import com.foobnix.model.VKUser;
import com.foobnix.model.VkAudio;
import com.foobnix.util.memcache.CacheETask;
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
	public List<VkAudio> audioSearch(final String text) throws VkErrorException {
		CacheETask<List<VkAudio>, VkErrorException> task = new CacheETask<List<VkAudio>, VkErrorException>() {
            @Override
			public List<VkAudio> run() throws VkErrorException {
                return vkApi.audioSearch(text);
            }
        };
        return memCache.getOrUpdate("audioSearch_" + text, MemCache.DAY, task);
    }

    @Override
	public List<VkAudio> getUserAudio(final String uid) throws VkErrorException {
		CacheETask<List<VkAudio>, VkErrorException> task = new CacheETask<List<VkAudio>, VkErrorException>() {
            @Override
			public List<VkAudio> run() throws VkErrorException {
                return vkApi.getUserAudio(uid);
            }
        };
        return memCache.getOrUpdate("getUserAudio_" + uid, MemCache.MIN * 10, task);
    }

    @Override
	public List<VkAlbum> getUserAlbums(final String uid) throws VkErrorException {
		CacheETask<List<VkAlbum>, VkErrorException> task = new CacheETask<List<VkAlbum>, VkErrorException>() {
            @Override
			public List<VkAlbum> run() throws VkErrorException {
                return vkApi.getUserAlbums(uid);
            }
        };
        return memCache.getOrUpdate("getUserAlbums_" + uid, MemCache.DAY, task);
    }

    @Override
	public List<VkAlbum> getGroupAlbums(final String gid) throws VkErrorException {
		CacheETask<List<VkAlbum>, VkErrorException> task = new CacheETask<List<VkAlbum>, VkErrorException>() {
            @Override
			public List<VkAlbum> run() throws VkErrorException {
                return vkApi.getGroupAlbums(gid);
            }
        };
        return memCache.getOrUpdate("getGroupAlbums_" + gid, MemCache.DAY, task);
    }

    @Override
	public List<VkAudio> getGroupAudio(final String gid) throws VkErrorException {
		CacheETask<List<VkAudio>, VkErrorException> task = new CacheETask<List<VkAudio>, VkErrorException>() {
            @Override
			public List<VkAudio> run() throws VkErrorException {
                return vkApi.getGroupAudio(gid);
            }
        };
        return memCache.getOrUpdate("getGroupAudio_" + gid, MemCache.DAY, task);
    }

    @Override
	public List<VkAudio> getUserAlbumAudio(final String uid, final String albumId) throws VkErrorException {
		CacheETask<List<VkAudio>, VkErrorException> task = new CacheETask<List<VkAudio>, VkErrorException>() {
            @Override
			public List<VkAudio> run() throws VkErrorException {
                return vkApi.getUserAlbumAudio(uid, albumId);
            }
        };
        return memCache.getOrUpdate("getUserAlbumAudio_" + uid + albumId, MemCache.DAY, task);
    }

    @Override
	public List<VkAudio> getGroupAlbumAudio(final String gid, final String albumId) throws VkErrorException {
		CacheETask<List<VkAudio>, VkErrorException> task = new CacheETask<List<VkAudio>, VkErrorException>() {
            @Override
			public List<VkAudio> run() throws VkErrorException {
                return vkApi.getGroupAlbumAudio(gid, albumId);
            }
        };
        return memCache.getOrUpdate("getGroupAlbumAudio_" + gid + albumId, MemCache.DAY, task);
    }

    @Override
	public List<VkGroup> getUserGroups() throws VkErrorException {
		CacheETask<List<VkGroup>, VkErrorException> task = new CacheETask<List<VkGroup>, VkErrorException>() {
            @Override
			public List<VkGroup> run() throws VkErrorException {
                return vkApi.getUserGroups();
            }
        };
        return memCache.getOrUpdate("getUserGroups", MemCache.DAY, task);
    }

    @Override
	public List<VKUser> getUserFriends(final String uid) throws VkErrorException {
		CacheETask<List<VKUser>, VkErrorException> task = new CacheETask<List<VKUser>, VkErrorException>() {
            @Override
			public List<VKUser> run() throws VkErrorException {
                return vkApi.getUserFriends(uid);
            }
        };
		return memCache.getOrUpdate("getUserFriends" + uid, MemCache.DAY, task);
    }

    @Override
	public VKUser getUserProfile(final String uid) throws VkErrorException {
		CacheETask<VKUser, VkErrorException> task = new CacheETask<VKUser, VkErrorException>() {
            @Override
			public VKUser run() throws VkErrorException {
                return vkApi.getUserProfile(uid);
            }
        };
		return memCache.getOrUpdate("getUserProfile" + uid, MemCache.DAY, task);
    }

}
