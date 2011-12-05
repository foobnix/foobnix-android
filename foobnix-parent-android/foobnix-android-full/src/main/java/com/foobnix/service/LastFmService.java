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
package com.foobnix.service;


import android.content.Context;

import com.foobnix.R;
import com.foobnix.commons.string.StringUtils;
import com.foobnix.model.FModel;
import com.foobnix.util.LOG;
import com.foobnix.util.Res;
import com.foobnix.util.pref.Pref;

import de.umass.lastfm.Authenticator;
import de.umass.lastfm.Session;
import de.umass.lastfm.Track;
import de.umass.lastfm.scrobble.ScrobbleResult;

public class LastFmService {

    private Session lfmSession;
    private boolean enable;
    private final Context context;
	private String login;
	private String pass;

    public LastFmService(Context context) {
        this.context = context;
		enable = Pref.getBool(context, Pref.LASTFM_ENABLE);
		login = Pref.getStr(context, Pref.LASTFM_USER);
		pass = Pref.getStr(context, Pref.LASTFM_PASS);
    }

	public boolean checkConnectionAuthorization(String login, String pass) {
		this.login = login;
		this.pass = pass;

        if (StringUtils.isEmpty(login) || StringUtils.isEmpty(pass)) {
            LOG.d("Login or password is empty");
            return false;
        }
        try {
            lfmSession = Authenticator.getMobileSession(login, pass, Res.get(context, R.string.LAST_FM_API_KEY),
                    Res.get(context, R.string.LAST_FM_API_SECRET));
        } catch (Exception e) {
            LOG.e("Last fm connection error", e);
            lfmSession = null;
            return false;
        }
        if (lfmSession == null) {
            LOG.d("Last.fm authrization error");
            return false;
        }

        return true;
    }

    public boolean isConnectedAndEnable() {
		enable = Pref.getBool(context, Pref.LASTFM_ENABLE);

		if (!enable) {
            return false;
        }


        if (lfmSession != null && enable) {
            return true;
        }

		return checkConnectionAuthorization(login, pass);
    }

    public void scrobble(FModel model) {
        if (model == null) {
            return;
        }
        if (enable && lfmSession != null) {
            int now = (int) (System.currentTimeMillis() / 1000);
            try {
                ScrobbleResult scrobble = Track.scrobble(model.getArtist(), model.getTitle(), now, lfmSession);
                LOG.d("Scrobbleed", model, scrobble.getErrorMessage());
            } catch (Exception e) {
                LOG.e("Last.FM Inner error scrobble " + model, e);
            }
        }

    }

    public void updateNowPlaying(FModel model) {
        LOG.d(enable, model, "!!!");
        if (model == null) {
            LOG.d("Update now playing null model");
            return;
        }
        if (enable && lfmSession != null) {
            try {
                ScrobbleResult res = Track.updateNowPlaying(model.getArtist(), model.getTitle(), lfmSession);
                LOG.d("Update now playing ", model.getArtist(), model.getTitle(), res.getErrorMessage());
            } catch (Exception e) {
                LOG.e("Last.FM Inner now playing " + model, e);
            }
        } else {
            LOG.d("Update now playing disable");
        }
    }

    public Session getLfmSession() {
        return lfmSession;
    }

}
