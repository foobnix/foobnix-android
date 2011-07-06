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
package com.foobnix.ui.activity.pref;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

import com.foobnix.R;
import com.foobnix.service.DMService;
import com.foobnix.service.FoobnixService;
import com.foobnix.service.MODE;
import com.foobnix.ui.activity.VkCheckActivity;
import com.foobnix.ui.activity.pref.base.BgImagePreferences;
import com.foobnix.ui.activity.pref.base.NotificationIconPreference;
import com.foobnix.ui.activity.pref.base.RandomModePreference;
import com.foobnix.ui.activity.pref.base.StopTimerPreference;
import com.foobnix.util.C;
import com.foobnix.util.Conf;

public class PlayerPreferences extends PrefMenuActivity {
	private PreferenceScreen root;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setPreferenceScreen(createPreferenceHierarchy());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private PreferenceScreen createPreferenceHierarchy() {
		root = getPreferenceManager().createPreferenceScreen(this);
		root.setTitle(R.string.Foobnix_Settings);

		// Base services
		PreferenceCategory category = new PreferenceCategory(this);
		category.setTitle(R.string.Services);
		root.addPreference(category);

		PreferenceScreen lastFm = getPreferenceManager().createPreferenceScreen(this);
		lastFm.setTitle(R.string.Last_Fm_Scrobbler);
		category.addPreference(lastFm);

		if (C.get().lastFmEnable) {
			if (app.getLastFmService().isConnectedAndEnable()) {
				lastFm.setSummary(getString(R.string.Enable) + " " + C.get().lastFmUser);
			} else {
				lastFm.setSummary(getString(R.string.Fail) + " " + C.get().lastFmUser);
			}
		} else {
			lastFm.setSummary(R.string.Disable);
		}

		lastFm.setOnPreferenceClickListener(onLastfm);

		PreferenceScreen vk = getPreferenceManager().createPreferenceScreen(this);
		vk.setTitle(R.string.VKontakte_Authorization);
		vk.setOnPreferenceClickListener(onVK);
		if (StringUtils.isNotEmpty(C.get().vkontakteToken)) {
			vk.setSummary(R.string.Success_please_recheck_if_expired);
		} else {
			vk.setSummary(R.string.Disable);
		}
		category.addPreference(vk);

		// Config
		PreferenceCategory config = new PreferenceCategory(this);
		config.setTitle(R.string.Functional);
		root.addPreference(config);

		// config.addPreference(new AlarmPreference().Builder(this, app));
		config.addPreference(new NotificationIconPreference().Builder(this, app));
		config.addPreference(new StopTimerPreference().Builder(this, app));
		config.addPreference(new RandomModePreference().Builder(this));
		config.addPreference(new BgImagePreferences().Builder(this));

		// Other
		PreferenceCategory other = new PreferenceCategory(this);
		other.setTitle(R.string.Other);
		root.addPreference(other);

		PreferenceScreen version = getPreferenceManager().createPreferenceScreen(this);
		version.setTitle(R.string.app_name);
		version.setSummary(Conf.getFoobnixVersion(getApplicationContext()));
		other.addPreference(version);

		PreferenceScreen web = getPreferenceManager().createPreferenceScreen(this);
		web.setTitle(R.string.Site);

		web.setSummary("http://android.foobnix.com");
		web.setOnPreferenceClickListener(onWeb);
		other.addPreference(web);

		PreferenceScreen about = getPreferenceManager().createPreferenceScreen(this);
		about.setTitle(R.string.About);
		about.setSummary("Ivan Ivanenko <ivan.ivanenko@gmail.com>");
		other.addPreference(about);

		PreferenceScreen exit = getPreferenceManager().createPreferenceScreen(this);
		exit.setTitle(R.string.Exit);
		exit.setOnPreferenceClickListener(onExit);
		other.addPreference(exit);

		return root;

	}

	OnPreferenceClickListener onExit = new OnPreferenceClickListener() {

		@Override
		public boolean onPreferenceClick(Preference preference) {
			stopService(new Intent(PlayerPreferences.this, FoobnixService.class));
			stopService(new Intent(PlayerPreferences.this, DMService.class));
			app.getNotification().displayNotifcation(MODE.HIDE);
			finish();
			return true;
		}
	};

	OnPreferenceClickListener onWeb = new OnPreferenceClickListener() {

		@Override
		public boolean onPreferenceClick(Preference preference) {
			Locale locale = Locale.getDefault();
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://android.foobnix.com/news?lang="
			        + locale.getLanguage())));
			return false;
		}
	};

	OnPreferenceClickListener onVK = new OnPreferenceClickListener() {
		@Override
		public boolean onPreferenceClick(Preference preference) {
			startActivity(new Intent(PlayerPreferences.this, VkCheckActivity.class));
			return false;
		}
	};

	OnPreferenceClickListener onLastfm = new OnPreferenceClickListener() {

		public boolean onPreferenceClick(Preference p) {
			startActivity(new Intent(getApplicationContext(), LastFmPreferences.class));
			return false;
		}
	};

}
