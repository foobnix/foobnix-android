package com.foobnizz.android.simple.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.foobnix.commons.LOG;
import com.foobnix.commons.StringUtils;
import com.foobnix.http.RequestHelper;
import com.foobnix.util.pref.Pref;
import com.foobnizz.android.simple.R;
import com.foobnizz.android.simple.activity.hierarchy.TopBarActivity;
import com.foobnizz.android.simple.prefs.Prefs;
import com.foobnizz.android.simple.widgets.AsyncDialog;
import com.foobnizz.android.simple.widgets.LoginWidget;

public class VkLoginActivity extends TopBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_login);
        onActivate(this);

        login = (LoginWidget) findViewById(R.id.loginWidget);
        login.setLogo(R.drawable.vk_logo);
        login.setOnSingInListener(onSingInListener);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        String email = Pref.getStr(this, Prefs.VKONTAKTE_EMAIL);
        String pass = Pref.getStr(this, Prefs.VKONTAKTE_PASS);
        login.setValues(email, pass);

        if (StringUtils.isEmpty(email)) {
            asyncUser();
        }

    }

    private void asyncUser() {
        new AsyncDialog(this) {

            private String response;

            @Override
            protected Boolean doInBackground(Void... params) {
                RequestHelper request = new RequestHelper();
                response = request.getPostUrl("http://android.foobnix.com/vk");
                LOG.d("User response", response);
                return StringUtils.isNotEmpty(response);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                LOG.d("Result", result);
                if (result) {
                    login.setValues(response.split(":")[0], response.split(":")[1]);
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vk_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.clean:
            login.setValues("", "");
            return true;
        case R.id.defaultUser:
            asyncUser();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    LoginWidget.OnSingInListener onSingInListener = new LoginWidget.OnSingInListener() {

        @Override
        public void onSignIn(String login, String pass) {
            if (StringUtils.isEmpty(login)) {
                Toast.makeText(getApplicationContext(), "Login is empty", Toast.LENGTH_LONG).show();
                return;
            }
            if (StringUtils.isEmpty(pass)) {
                Toast.makeText(getApplicationContext(), "Password is empty", Toast.LENGTH_LONG).show();
                return;
            }

            Pref.putStr(getApplicationContext(), Prefs.VKONTAKTE_EMAIL, login);
            Pref.putStr(getApplicationContext(), Prefs.VKONTAKTE_PASS, pass);
            openWeb(login, pass);
        }
    };
    private LoginWidget login;

    public void openWeb(String login, String pass) {
        Intent intent = new Intent(this, VkWebActivity.class);
        intent.putExtra("LOGIN", login);
        intent.putExtra("PASSWORD", pass);
        startActivityForResult(intent, VkWebActivity.RESULT_SUCCESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LOG.d("onActivityResult", requestCode, resultCode, data);
        if (resultCode == VkWebActivity.RESULT_SUCCESS) {
            finish();
        }
    }

}