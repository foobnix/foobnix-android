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
package com.foobnix.ui.activity.info;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.foobnix.R;
import com.foobnix.model.FModel;
import com.foobnix.ui.activity.FoobnixMenuActivity;
import com.foobnix.util.C;
import com.foobnix.util.Conf;
import com.foobnix.util.ImageUtil;
import com.foobnix.util.LOG;

import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;

public class AboutArtistActivity extends FoobnixMenuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.about_artist);

		TextView title = (TextView) findViewById(R.id.aboutArtistTitle);
		TextView bio = (TextView) findViewById(R.id.aboutArtistBio);
		ImageView image = (ImageView) findViewById(R.id.aboutImage);
		FModel song = app.getNowPlayingSong();

		String user = "l_user_";

		if (StringUtils.isNotEmpty(C.get().lastFmUser)) {
			user = C.get().lastFmUser;
		}
		try {
		Artist info = Artist.getInfo(song.getArtist(), Locale.getDefault(), user, Conf.LAST_FM_API_KEY);
		if (info != null) {
			String wikiText = info.getWikiText();
			String imageURL = info.getImageURL(ImageSize.EXTRALARGE);
				image.setImageBitmap(ImageUtil.fetchImage(imageURL));
			title.setText(song.getText());
			bio.setText(wikiText);
		}
		} catch (Exception e) {
			LOG.e("Info error", e);
			ToastShort("Erorr, try again a bit later");
		}

		onAcitvateMenuImages(this);
	}


	@Override
	public String getActivityTitle() {
		return "About Artist";
	}

	@Override
	protected void actionDialog(FModel item) {

	}

	@Override
	public Class<?> activityClazz() {
		// TODO Auto-generated method stub
		return null;
	}

}
