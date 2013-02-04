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
package com.foobnix.ui.activity.other;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.foobnix.R;
import com.foobnix.ui.activity.AppActivity;
import com.foobnix.util.LOG;
import com.foobnix.util.Res;
import com.foobnix.util.pref.Pref;
import com.foobnix.util.pref.Prefs;

public class VkCheckActivity extends AppActivity {
	private final static String REDIRECT_URL = "http://android.foobnix.com/vk";
	private final static String API_URL = "http://api.vkontakte.ru";
    public static final int OK_RESULT = 66;
	private String OAUTH_URL = "";
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);
		setTitle(R.string.vk_authorization);

		OAUTH_URL = API_URL + "/oauth/authorize?client_id=" + Res.get(this, R.string.VK_APP_ID)
		        + "&scope=audio,friends&redirect_uri=" + REDIRECT_URL + "&response_type=token&display=touch";

		LOG.d(OAUTH_URL);

		webView = (WebView) findViewById(R.id.webView1);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.clearHistory();
		webView.clearFormData();
		webView.clearCache(true);

		webView.setWebViewClient(new VkontakteClient());


		webView.loadUrl(OAUTH_URL);

	}

	Handler handler = new Handler();


	private class VkontakteClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith(REDIRECT_URL)) {
				LOG.d("Tocken founded");

				Pattern p = Pattern.compile(REDIRECT_URL + "#access_token=(.*)&expires_in=(.*)&user_id=(.*)");
				Matcher m = p.matcher(url);
				if (m.matches()) {
					String token = m.group(1);
					String userId = m.group(3);
					LOG.d("Success Recive token and user", token, userId);

					Pref.putStr(VkCheckActivity.this, Prefs.VKONTAKTE_TOKEN, token);
					Pref.putStr(VkCheckActivity.this, Prefs.VKONTAKTE_USER_ID, userId);

					app.getIntegrationsQueryManager().getVkAdapter().setToken(token);

					Toast.makeText(view.getContext(), R.string.Success, Toast.LENGTH_LONG).show();
					setResult(OK_RESULT);
					finish();
					return true;
				} else {
					Pref.putStr(VkCheckActivity.this, Prefs.VKONTAKTE_TOKEN, "");
					Pref.putStr(VkCheckActivity.this, Prefs.VKONTAKTE_USER_ID, "");
				}

			}
			view.loadUrl(url);
			return true;
		}

	}
}
