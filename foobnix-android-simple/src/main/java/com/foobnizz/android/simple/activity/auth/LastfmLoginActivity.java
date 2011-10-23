package com.foobnizz.android.simple.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.foobnix.commons.LOG;
import com.foobnix.commons.StringUtils;
import com.foobnix.util.pref.Pref;
import com.foobnizz.android.simple.R;
import com.foobnizz.android.simple.activity.hierarchy.TopBarActivity;
import com.foobnizz.android.simple.prefs.Prefs;
import com.foobnizz.android.simple.widgets.LoginWidget;

public class LastfmLoginActivity extends TopBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_login);
        onActivate(this);

        login = (LoginWidget) findViewById(R.id.loginWidget);
        login.setLogo(R.drawable.lastfm_logo_line);
        login.setOnSingInListener(onSingInListener);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        String email = Pref.getStr(this, Prefs.LASTFM_USER);
        String pass = Pref.getStr(this, Prefs.LASTFM_PASS);
        login.setValues(email, pass);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutParent);
        CheckBox child = new CheckBox(this);
        child.setText("Enable Last.FM");

        layout.addView(child);

    }

    private void asyncUser() {
        login.setValues("foobnix", "foobnix");
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