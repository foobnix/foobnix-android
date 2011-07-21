/**
 * 
 */
package com.foobnix.api.vkontakte;

import java.util.List;

import com.foobnix.model.VKUser;
import com.foobnix.model.VkAudio;

/**
 * @author iivanenko
 *
 */
public interface VkApiCalls {

    public abstract void setToken(String token);

    public abstract List<VkAudio> audioSearch(final String text);

    public abstract List<VkAudio> getUserAudio(String uid);

    public abstract List<VkAlbum> getUserAlbums(String uid);

    public abstract List<VkAlbum> getGroupAlbums(String gid);

    public abstract List<VkAudio> getGroupAudio(String gid);

    public abstract List<VkAudio> getUserAlbumAudio(String uid, String albumId);

    public abstract List<VkAudio> getGroupAlbumAudio(String gid, String albumId);

    public abstract List<VkGroup> getUserGroups();

    public abstract List<VKUser> getUserFriends(String uid);

    public abstract VKUser getUserProfile(String uid);

}