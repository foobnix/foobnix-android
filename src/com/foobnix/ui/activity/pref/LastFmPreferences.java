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
import com.foobnix.util.C;

public class LastFmPreferences extends PrefMenuActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setPreferenceScreen(createPreferenceHierarchy());
	}

	private PreferenceScreen createPreferenceHierarchy() {
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
		root.setTitle(R.string.Last_Fm_Scrobbler);

		final CheckBoxPreference enable = new CheckBoxPreference(this);
		enable.setTitle(R.string.Enable_Scrobbler);
		enable.setKey("enable");
		root.addPreference(enable);

		final EditTextPreference login = new EditTextPreference(this);
		login.setTitle(R.string.Login);
		login.setKey("login");
		root.addPreference(login);
		login.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				C.get().lastFmUser = login.getText();
				return true;
			}
		});

		final EditTextPreference pass = new EditTextPreference(this);
		pass.setTitle(R.string.Password);
		pass.setKey("pass");
		root.addPreference(pass);

		pass.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				C.get().lastFmPass = pass.getText();
				return true;
			}
		});

		enable.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				C.get().lastFmEnable = enable.isChecked();
				
				if (!enable.isChecked()) {
					enable.setSummary(R.string.Disable);
					return false;
				}
				if (StringUtils.isEmpty(login.getText()) || StringUtils.isEmpty(pass.getText())) {
					enable.setSummary(R.string.Login_or_password_is_empty);
					return false;
				}

				C.get().lastFmUser = login.getText();
				C.get().lastFmPass = pass.getText();

				boolean connect = app.getLastFmService().checkConnectionAuthorization();

				if (connect) {
					enable.setSummary(getString(R.string.Success) + " " + login.getText());
				} else {
					enable.setSummary(getString(R.string.Fail) + " " + login.getText());
				}
				return false;
			}
		});

		return root;

	}

}
