package com.foobnix.activity.auth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.foobnix.activity.hierarchy.TopBarActivity;
import com.foobnix.commons.LOG;
import com.foobnix.prefs.Prefs;
import com.foobnix.util.pref.Pref;
import com.foobnix.util.pref.Res;
import com.foobnix.R;

public class VkWebActivity extends TopBarActivity {
    public final static String REDIRECT_URL = "http://android.foobnix.com/vk";
    public static final int RESULT_SUCCESS = 101;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_webview);
        onActivate(this);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new VkontakteClient());

        webView.loadUrl(getAuthorizeUrl(Res.get(this, R.string.VK_APP_ID), REDIRECT_URL));
    }

    private String getAuthorizeUrl(String appId, String redirectUrl) {
        return "http://api.vkontakte.ru/oauth/authorize?client_id=" + appId + //
                "&scope=audio,friends&redirect_uri=" + redirectUrl + //
                "&response_type=token&display=touch";

    }

    private class VkontakteClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(REDIRECT_URL)) {
                Pattern p = Pattern.compile(REDIRECT_URL + "#access_token=(.*)&expires_in=(.*)&user_id=(.*)");
                Matcher m = p.matcher(url);
                if (m.matches()) {
                    String token = m.group(1);
                    String userId = m.group(3);
                    LOG.d("Success Recive token and user", token, userId);

                    Pref.putStr(VkWebActivity.this, Prefs.VKONTAKTE_TOKEN, token);
                    Pref.putStr(VkWebActivity.this, Prefs.VKONTAKTE_USER_ID, userId);

                    Toast.makeText(view.getContext(), "Success", Toast.LENGTH_LONG).show();

                    setResult(RESULT_SUCCESS);
                    finish();
                    return true;
                }

            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LOG.d("On Finished", url);
            handler.removeCallbacks(task);
            handler.postDelayed(task, 500);
        }
    }

    Handler handler = new Handler();

    Runnable task = new Runnable() {

        @Override
        public void run() {
            String login = getIntent().getStringExtra("LOGIN");
            String pass = getIntent().getStringExtra("PASSWORD");

            webView.loadUrl(String.format(
                    "javascript:(function() {document.getElementsByName('email')[0].value='%s'})()", login));
            webView.loadUrl(String.format(
                    "javascript:(function() {document.getElementsByName('pass')[0].value='%s'})()", pass));

            // webView.loadUrl("javascript:(function() {document.getElementById('login_submit').submit()})()");

        }
    };

}
