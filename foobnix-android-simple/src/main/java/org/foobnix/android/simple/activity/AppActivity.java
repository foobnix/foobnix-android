package org.foobnix.android.simple.activity;

import org.foobnix.android.simple.FoobnixFolderPlayerApplication;

import android.app.Activity;
import android.os.Bundle;

public class AppActivity extends Activity {
    protected FoobnixFolderPlayerApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (FoobnixFolderPlayerApplication) getApplication();
    }

}
