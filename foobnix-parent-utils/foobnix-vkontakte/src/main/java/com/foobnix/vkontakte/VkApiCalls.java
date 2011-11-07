/**
 * 
 */
package com.foobnix.vkontakte;

import java.util.List;

import com.foobnix.exception.VkErrorException;

/**
 * @author iivanenko
 *
 */
public interface VkApiCalls {

    public abstract void setToken(String token);

	public abstract List<VkAudio> audioSearch(final String text) throws VkErrorException;

    public abstract List<VkAudio> getUserAudio(String uid) throws VkErrorException;

    public abstract List<VkAlbum> getUserAlbums(String uid) throws VkErrorException;

    public abstract List<VkAlbum> getGroupAlbums(String gid) throws VkErrorException;

    public abstract List<VkAudio> getGroupAudio(String gid) throws VkErrorException;

    public abstract List<VkAudio> getUserAlbumAudio(String uid, String albumId) throws VkErrorException;

    public abstract List<VkAudio> getGroupAlbumAudio(String gid, String albumId) throws VkErrorException;

    public abstract List<VkGroup> getUserGroups() throws VkErrorException;

    public abstract List<VKUser> getUserFriends(String uid) throws VkErrorException;

    public abstract VKUser getUserProfile(String uid) throws VkErrorException;

}