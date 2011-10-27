package com.foobnix.activity.hierarchy;

import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.foobnix.R;
import com.foobnix.activity.DownloadsActivity;
import com.foobnix.activity.FoldersActivity;
import com.foobnix.activity.InfoActivity;
import com.foobnix.activity.LastfmActivity;
import com.foobnix.activity.PlaylistActivity;
import com.foobnix.activity.SearchActivity;
import com.foobnix.activity.SettingsActivity;
import com.foobnix.activity.VkontakteActivity;
import com.foobnix.commons.pref.Pref;
import com.foobnix.mediaengine.MediaEngineState;

public class TopBarActivity extends AppActivity {
    private static Map<Integer, Class<?>> bindTabs = new LinkedHashMap<Integer, Class<?>>();
    private HorizontalScrollView scroll;
    static {
        bindTabs.put(R.id.topbarPlaylist, PlaylistActivity.class);
        bindTabs.put(R.id.topbarFolders, FoldersActivity.class);
        bindTabs.put(R.id.topbarSearch, SearchActivity.class);
        bindTabs.put(R.id.topbarLastfm, LastfmActivity.class);
        bindTabs.put(R.id.topbarVkontakte, VkontakteActivity.class);
        bindTabs.put(R.id.topbarDownloads, DownloadsActivity.class);
        bindTabs.put(R.id.topbarInfo, InfoActivity.class);
        bindTabs.put(R.id.topbarPreferences, SettingsActivity.class);

    }

    public void onActivate(final Activity activity) {
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        for (final Integer id : bindTabs.keySet()) {
            LinearLayout layout = (LinearLayout) activity.findViewById(id);

            layout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    Intent intent = new Intent(activity, bindTabs.get(id));
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    activity.startActivity(intent);
                }
            });

            if (activity.getClass().equals(bindTabs.get(id))) {
                layout.setSelected(true);
            }
        }
        scroll = (HorizontalScrollView) activity.findViewById(R.id.horizontalScrollView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scroll.post(new Runnable() {
            @Override
            public void run() {
                int x = Pref.getInt(getApplicationContext(), "ScrollX");
                int y = Pref.getInt(getApplicationContext(), "ScrollY");
                scroll.scrollTo(x, y);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Pref.putInt(getApplicationContext(), "ScrollX", scroll.getScrollX());
        Pref.putInt(getApplicationContext(), "ScrollY", scroll.getScrollY());
    }

    @Override
    public void onUpdateUIListener(MediaEngineState state) {
    }

}
