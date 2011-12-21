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

import org.apache.commons.lang.StringUtils;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;

import com.foobnix.R;
import com.foobnix.util.pref.Pref;

import de.umass.lastfm.Tasteometer.InputType;

public class LastFmAccountPreferences extends PrefMenuActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setPreferenceScreen(createPreferenceHierarchy());
	}

	private PreferenceScreen createPreferenceHierarchy() {
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
		root.setTitle(R.string.Last_Fm_Scrobbler);

		final CheckBoxPreference enable = new CheckBoxPreference(this);
		enable.setTitle(R.string.Enable_Scrobbler);
		enable.setKey(Pref.LASTFM_ENABLE);
		enable.setDefaultValue(Pref.getBool(this, Pref.LASTFM_ENABLE));
		root.addPreference(enable);

		final EditTextPreference login = new EditTextPreference(this);
		login.setTitle(R.string.Login);
		login.setKey(Pref.LASTFM_USER);
		root.addPreference(login);

		final EditTextPreference pass = new EditTextPreference(this);
		pass.getEditText().setInputType(android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
		pass.setTitle(R.string.Password);
		pass.setKey(Pref.LASTFM_PASS);
		root.addPreference(pass);


		enable.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				String loginValue = login.getText();
				String passValue = pass.getText();

				Pref.putBool(LastFmAccountPreferences.this, Pref.LASTFM_ENABLE, enable.isChecked());
				Pref.putStr(LastFmAccountPreferences.this, Pref.LASTFM_USER, loginValue);
				Pref.putStr(LastFmAccountPreferences.this, Pref.LASTFM_PASS, passValue);

				if (!enable.isChecked()) {
					enable.setSummary(R.string.Disable);
					return false;
				}

				if (StringUtils.isEmpty(loginValue) || StringUtils.isEmpty(passValue)) {
					enable.setSummary(R.string.Login_or_password_is_empty);
					return false;
				}


				boolean connect = app.getLastFmService().checkConnectionAuthorization(loginValue, passValue);

				if (connect) {
					enable.setSummary(getString(R.string.Success) + " " + loginValue);
				} else {
					enable.setSummary(getString(R.string.Fail) + " " + loginValue);
				}
				return false;
			}
		});

		return root;

	}

}
