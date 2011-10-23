package com.foobnizz.android.simple.activity.hierarchy;

import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.foobnix.commons.LOG;
import com.foobnizz.android.simple.R;
import com.foobnizz.android.simple.activity.DownloadsActivity;
import com.foobnizz.android.simple.activity.FoldersActivity;
import com.foobnizz.android.simple.activity.InfoActivity;
import com.foobnizz.android.simple.activity.LastfmActivity;
import com.foobnizz.android.simple.activity.PlaylistActivity;
import com.foobnizz.android.simple.activity.SearchActivity;
import com.foobnizz.android.simple.activity.SettingsActivity;
import com.foobnizz.android.simple.activity.VkontakteActivity;
import com.foobnizz.android.simple.mediaengine.MediaEngineState;

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
                LOG.d("on scroll TO", app.getScrollX(), app.getScrollY());
                scroll.scrollTo(app.getScrollX(), app.getScrollY());
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        app.setScrollX(scroll.getScrollX());
        app.setScrollY(scroll.getScrollY());
        LOG.d("on pause scroll", scroll.getScrollX(), scroll.getScrollY());
    }



    @Override
    public void onUpdateUIListener(MediaEngineState state) {
    }

}
