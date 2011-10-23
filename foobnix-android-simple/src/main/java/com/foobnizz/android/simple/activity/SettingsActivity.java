package com.foobnizz.android.simple.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.foobnizz.android.simple.R;
import com.foobnizz.android.simple.activity.auth.LastfmLoginActivity;
import com.foobnizz.android.simple.activity.auth.VkLoginActivity;
import com.foobnizz.android.simple.activity.hierarchy.GeneralListActivity;
import com.foobnizz.android.simple.adapter.ModelListAdapter;
import com.foobnizz.android.simple.adapter.PrefItemAdapter;
import com.foobnizz.android.simple.model.PrefItem;

public class SettingsActivity extends GeneralListActivity<PrefItem> {
    private List<PrefItem> prefs = new ArrayList<PrefItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_list);

        prefs.add(new PrefItem(R.drawable.lastfm, "Last.Fm", "User and Audioscrobbler", new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LastfmLoginActivity.class));

            }
        }));
        prefs.add(new PrefItem(R.drawable.vk, "Vkontakte", "Social Network User", new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), VkLoginActivity.class));
            }
        }));

        onActivate(this);
        disableSecondTopLine();
        disablePlayerMenu();
        TextView text = new TextView(this);
        text.setText("Others");
        listView.addView(text);
    }

    @Override
    public Class<? extends ModelListAdapter<PrefItem>> getAdapter() {
        return PrefItemAdapter.class;
    }

    @Override
    public List<PrefItem> getInitItems() {
        return prefs;
    }

    @Override
    public void onModelItemClickListener(PrefItem model) {
        model.getAction().run();
    }

}