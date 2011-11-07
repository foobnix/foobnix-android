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
package com.foobnix.ui.activity.stars;

import java.util.Locale;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

import com.foobnix.R;
import com.foobnix.model.FModel;
import com.foobnix.ui.activity.TabActivity;
import com.foobnix.util.LOG;
import com.foobnix.util.Res;
import com.foobnix.util.pref.Pref;

import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;

public class AboutArtistActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

		onActivateStarTabs(this, R.layout.about_artist);

		if (!app.isOnline()) {
			LOG.d("Offline");
			Toast.makeText(this, "No internet", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		if (menuLyaout != null) {
		menuLyaout.setVisibility(View.GONE);
		}

		// setContentView(R.layout.about_artist);
		// onActivateMenuActivity(this);

        WebView bio = (WebView) findViewById(R.id.webBio);

        FModel song = app.getNowPlayingSong();

		String user = Pref.getStr(this, Pref.LASTFM_USER, "foobnix");

        try {
            Artist info = Artist.getInfo(song.getArtist(), Locale.getDefault(), user,
                    Res.get(this, R.string.LAST_FM_API_KEY));
            if (info != null) {
                String wikiText = info.getWikiText();
                if (wikiText == null) {
                    wikiText = "";
                }
                String imageURL = info.getImageURL(ImageSize.EXTRALARGE);
                String header = "<h3>" + song.getArtist() + " - " + song.getTitle() + "</h3>";
                header += "<img width='100%' src='" + imageURL + "' /><br/>";
                bio.loadDataWithBaseURL(null, header + wikiText, "text/html", "UTF-8", null);

            }
        } catch (Exception e) {
            LOG.e("Info error", e);
        }

    }

}
