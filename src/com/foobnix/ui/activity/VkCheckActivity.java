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
package com.foobnix.ui.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.foobnix.R;
import com.foobnix.api.vkontakte.VkOld;
import com.foobnix.model.FModel;
import com.foobnix.util.C;
import com.foobnix.util.LOG;
import com.foobnix.util.Pref;
import com.foobnix.util.PrefKeys;
import com.foobnix.util.Res;

public class VkCheckActivity extends FoobnixMenuActivity {
    private final static String REDIRECT_URL = "http://android.foobnix.com/vk";
    private final static String API_URL = "http://api.vkontakte.ru";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        String OAUTH_URL = API_URL + "/oauth/authorize?client_id=" + Res.get(this, R.string.VK_APP_ID)
                + "&scope=audio,friends&redirect_uri=" + REDIRECT_URL + "&response_type=token&display=touch";

        LOG.d(OAUTH_URL);

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new VkontakteClient());
        webView.loadUrl(OAUTH_URL);

        Button webDefault = (Button) findViewById(R.id.webDefault);
        webDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] userPass = VkOld.getVKUserPass(REDIRECT_URL);
                webView.loadUrl(String.format(
                        "javascript:(function() {document.getElementsByName('email')[0].value='%s'})()", userPass[0]));
                webView.loadUrl(String.format(
                        "javascript:(function() {document.getElementsByName('pass')[0].value='%s'})()", userPass[1]));
            }
        });
    }

    private class VkontakteClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(REDIRECT_URL)) {
                LOG.d("Tocken founded");

                Pattern p = Pattern.compile(REDIRECT_URL + "#access_token=(.*)&expires_in=(.*)&user_id=(.*)");
                Matcher m = p.matcher(url);
                if (m.matches()) {
                    C.get().vkontakteToken = m.group(1);
                    C.get().vkontakteUserId = m.group(3);
                    Pref.put(VkCheckActivity.this, PrefKeys.VKONTAKTE_TOKEN, m.group(1));
                    app.getIntegrationsQueryManager().getVkAdapter().setToken(m.group(1));

                    LOG.d("Recive token and user", C.get().vkontakteToken, C.get().vkontakteUserId);
                    ToastLong(R.string.Success);
                    finish();
                    return true;
                } else {
                    C.get().vkontakteToken = "";
                }

            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LOG.d("On Finished", url);
        }
    }

    @Override
    protected void actionDialog(FModel item) {
    }

    @Override
    public String getActivityTitle() {
        return getString(R.string.Authorization_VKontakte_require_for_music_search);
    }

    @Override
    public Class<?> activityClazz() {
        // TODO Auto-generated method stub
        return null;
    }

}
