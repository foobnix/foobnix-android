/* Copyright (c) 2011 Ivan Ivanenko <ivan.ivanenko@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Software), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. */
package vkontakte;

import java.util.List;

import junit.framework.TestCase;

import com.foobnix.exception.VkErrorException;
import com.foobnix.vkontakte.VKUser;
import com.foobnix.vkontakte.VkAlbum;
import com.foobnix.vkontakte.VkApi;
import com.foobnix.vkontakte.VkApiCalls;
import com.foobnix.vkontakte.VkAudio;
import com.foobnix.vkontakte.VkHelper;

public class TestVkontakteApi extends TestCase {

    VkApiCalls vkApi;

    protected void setUp() throws Exception {
        // README.txt explain hot to generate this token
        vkApi = new VkApi("b461660bff274e61b144afb4b0b1a7d2e03b185b185c5379d686bff0777f835");
    };

    public void _testSearchResult() throws VkErrorException {
        List<VkAudio> search = vkApi.audioSearch("Madonna");
        assertNotNull(search);
        for (VkAudio song : search) {
            assertNotNull(song.getArtist());
            assertNotNull(song.getTitle());
            assertNotNull(song.getUrl());
            assertNotNull(song.getDuration());
        }
    }

    public void testSearchSongPath() throws VkErrorException {
        VkAudio search = VkHelper.getMostRelevantSong(vkApi.audioSearch("Madonna - Sorry"));
        assertNotNull(search);
    }

    public void testGetUserFriends() throws VkErrorException {
        List<VKUser> users = vkApi.getUserFriends("6851750");
        for (VKUser user : users) {
            assertNotNull(user.getUid());
            assertNotNull(user.getFirstName());
            assertNotNull(user.getLastName());
        }
    }

    public void testUserAudio() throws VkErrorException {
        List<VkAudio> search = vkApi.getUserAudio("6851750");
        assertNotNull(search);
        for (VkAudio song : search) {
            assertNotNull(song.getArtist());
            assertNotNull(song.getTitle());
            assertNotNull(song.getUrl());
            assertNotNull(song.getDuration());
        }
    }

    public void testGroupAudio() throws VkErrorException {
        List<VkAudio> search = vkApi.getGroupAudio("5073524");
        assertNotNull(search);
        for (VkAudio song : search) {
            assertNotNull(song.getArtist());
            assertNotNull(song.getTitle());
            assertNotNull(song.getUrl());
            assertNotNull(song.getDuration());
        }
    }

    public void testUserAlbums() throws VkErrorException {
        List<VkAlbum> search = vkApi.getUserAlbums("6851750");
        assertNotNull(search);
        for (VkAlbum song : search) {
            assertNotNull(song.getTitle());
            assertNotNull(song.getAlbumId());
            assertNotNull(song.getOwnerId());
        }
    }

    public void testGroupAlbums() throws VkErrorException {
        List<VkAlbum> search = vkApi.getGroupAlbums("3842582");
        assertNotNull(search);
        for (VkAlbum song : search) {
            assertNotNull(song.getTitle());
            assertNotNull(song.getAlbumId());
            assertNotNull(song.getOwnerId());
        }
    }

    public void testGroupAlbumSongs() throws VkErrorException {
        List<VkAudio> search = vkApi.getGroupAlbumAudio("5073524", "13705923");
        for (VkAudio song : search) {
            assertNotNull(song.getArtist());
            assertNotNull(song.getTitle());
            assertNotNull(song.getUrl());
            assertNotNull(song.getDuration());
        }
    }

    public void testUserAlbumSongs() throws VkErrorException {
        List<VkAudio> search = vkApi.getUserAlbumAudio("9091358", "16711661");
        for (VkAudio song : search) {
            assertNotNull(song.getArtist());
            assertNotNull(song.getTitle());
            assertNotNull(song.getUrl());
            assertNotNull(song.getDuration());
        }
    }

    public void testGetUserProfile() throws VkErrorException {
        VKUser user = vkApi.getUserProfile("6851750");
        assertNotNull(user.getUid());
        assertNotNull(user.getFirstName());
        assertNotNull(user.getLastName());
    }
}
