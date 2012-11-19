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

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.text.InputType;

import com.foobnix.R;
import com.foobnix.ui.activity.other.VkCheckActivity;
import com.foobnix.util.LOG;
import com.foobnix.util.pref.Pref;

public class VkontakteAccountPreferences extends PrefMenuActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setPreferenceScreen(createPreferenceHierarchy());
	}

	private PreferenceScreen createPreferenceHierarchy() {
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
		root.setTitle("Vkontanke Account");

		login = new EditTextPreference(this);
		login.setTitle(R.string.Login);
		login.setKey(Pref.VKONTAKTE_EMAIL);
		
		root.addPreference(login);

		pass = new EditTextPreference(this);
		pass.getEditText().setInputType(android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
		pass.setTitle(R.string.Password);
		pass.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		pass.setKey(Pref.VKONTAKTE_PASS);
		root.addPreference(pass);

		token = new EditTextPreference(this);
		token.setTitle("token");

		token.setDefaultValue(Pref.getStr(this, Pref.VKONTAKTE_TOKEN));
		// root.addPreference(token);

		PreferenceScreen vk = getPreferenceManager().createPreferenceScreen(this);
		vk.setTitle(R.string.VKontakte_Authorization);
		vk.setOnPreferenceClickListener(onVK);
		vk.setSummary(R.string.Vk_message);
		root.addPreference(vk);

		return root;
	}

	OnPreferenceClickListener onVK = new OnPreferenceClickListener() {
		@Override
		public boolean onPreferenceClick(Preference preference) {

			Intent intent = new Intent(getApplicationContext(), VkCheckActivity.class);
			intent.putExtra(Pref.VKONTAKTE_EMAIL, login.getText());
			intent.putExtra(Pref.VKONTAKTE_PASS, pass.getText());
			startActivityForResult(intent,VkCheckActivity.OK_RESULT);

			// Pref.putStr(getApplicationContext(), Prefs.VKONTAKTE_TOKEN,
			// token.getText());
			// app.getIntegrationsQueryManager().getVkAdapter().setToken(token.getText());

			return false;
		}
	};
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LOG.d("onActivityResult", requestCode, resultCode, data);
        if (resultCode == VkCheckActivity.OK_RESULT) {
            finish();
        }
    }


	private EditTextPreference login;
	private EditTextPreference pass;

	private EditTextPreference token;
}