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
package com.foobnix.api.vkontakte;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;

import com.foobnix.api.RequestHelper;
import com.foobnix.exception.VkErrorException;
import com.foobnix.model.VKUser;
import com.foobnix.model.VkAudio;

public class VkApi implements VkApiCalls {
    private static String API_URL = "https://api.vkontakte.ru/method/";
    private RequestHelper requestHelper;

    public VkApi(String token) {
        requestHelper = new RequestHelper(API_URL);
    }

    @Override
    public void setToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new IllegalArgumentException("Empty token");
        }
        requestHelper.setDefaultParam(new BasicNameValuePair("access_token", token));
    }

    @Override
	public List<VkAudio> audioSearch(final String text) throws VkErrorException {
        BasicNameValuePair q = new BasicNameValuePair("q", text);
        BasicNameValuePair count = new BasicNameValuePair("count", "100");

        String json = requestHelper.get("audio.search", q, count);
        return VkGsonResponse.toModels(json, VkAudio.class);

    }

    @Override
	public List<VkAudio> getUserAudio(String uid) throws VkErrorException {
        BasicNameValuePair param1 = new BasicNameValuePair("uid", uid);
        String json = requestHelper.get("audio.get", param1);

        List<VkAudio> result = VkGsonResponse.toModels(json, VkAudio.class);
        return result;
    }

    @Override
	public List<VkAlbum> getUserAlbums(String uid) throws VkErrorException {
        BasicNameValuePair param1 = new BasicNameValuePair("uid", uid);
        String json = requestHelper.get("audio.getAlbums", param1);
        List<VkAlbum> result = VkGsonResponse.toModels(json, VkAlbum.class);
        return result;
    }

    @Override
	public List<VkAlbum> getGroupAlbums(String gid) throws VkErrorException {
        BasicNameValuePair param1 = new BasicNameValuePair("gid", gid);
        String json = requestHelper.get("audio.getAlbums", param1);
        List<VkAlbum> result = VkGsonResponse.toModels(json, VkAlbum.class);
        return result;
    }

    @Override
	public List<VkAudio> getGroupAudio(String gid) throws VkErrorException {
        BasicNameValuePair param1 = new BasicNameValuePair("gid", gid);
        String json = requestHelper.get("audio.get", param1);
        List<VkAudio> result = VkGsonResponse.toModels(json, VkAudio.class);
        return result;
    }

    @Override
	public List<VkAudio> getUserAlbumAudio(String uid, String albumId) throws VkErrorException {
        BasicNameValuePair param1 = new BasicNameValuePair("album_id", albumId);
        BasicNameValuePair param2 = new BasicNameValuePair("uid", uid);
        String json = requestHelper.get("audio.get", param1, param2);
        List<VkAudio> result = VkGsonResponse.toModels(json, VkAudio.class);
        return result;
    }

    @Override
	public List<VkAudio> getGroupAlbumAudio(String gid, String albumId) throws VkErrorException {
        BasicNameValuePair param1 = new BasicNameValuePair("album_id", albumId);
        BasicNameValuePair param2 = new BasicNameValuePair("gid", gid);
        String json = requestHelper.get("audio.get", param1, param2);
        List<VkAudio> result = VkGsonResponse.toModels(json, VkAudio.class);
        return result;
    }

    @Override
	public List<VkGroup> getUserGroups() throws VkErrorException {
        String json = requestHelper.get("getGroupsFull");
        List<VkGroup> result = VkGsonResponse.toModels(json, VkGroup.class, 0);
        return result;
    }

    @Override
	public List<VKUser> getUserFriends(String uid) throws VkErrorException {
        BasicNameValuePair uidParam = new BasicNameValuePair("uid", uid);
        BasicNameValuePair fields = new BasicNameValuePair("fields", "uid,first_name,last_name,online");

        String json = requestHelper.get("friends.get", uidParam, fields);
        List<VKUser> result = VkGsonResponse.toModels(json, VKUser.class, 0);
        return result;
    }

    @Override
	public VKUser getUserProfile(String uid) throws VkErrorException {
        BasicNameValuePair uidParam = new BasicNameValuePair("uids", uid);
        BasicNameValuePair fields = new BasicNameValuePair("fields", "uid,first_name,last_name,online");

        String json = requestHelper.get("getProfiles", uidParam, fields);
        List<VKUser> result = VkGsonResponse.toModels(json, VKUser.class, 0);
        return result.get(0);
    }

}
