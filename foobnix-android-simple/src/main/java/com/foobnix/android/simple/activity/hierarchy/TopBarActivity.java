package com.foobnix.android.simple.activity.hierarchy;

import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foobnix.android.simple.R;
import com.foobnix.android.simple.activity.FoldersActivity;
import com.foobnix.android.simple.activity.PlaylistActivity;
import com.foobnix.commons.LOG;
import com.foobnix.commons.StringUtils;
import com.foobnix.commons.ViewBinder;

public class TopBarActivity extends MenuActivity {
    private LinearLayout menuLayout;
    private boolean isMenuVisible = true;
    private TextView infoLine;
    private LinearLayout settinsLayout;
    private ImageView settinsImage;
    private boolean isSettingsVisible = false;

    @Override
    public void onActivate(Activity activity) {
        super.onActivate(activity);
        LinearLayout playlist = (LinearLayout) activity.findViewById(R.id.topbarPlaylist);
        LinearLayout folders = (LinearLayout) activity.findViewById(R.id.topbarFolders);

        playlist.setOnClickListener(ViewBinder.onClickActivity(activity, PlaylistActivity.class));
        folders.setOnClickListener(ViewBinder.onClickActivity(activity, FoldersActivity.class));

        LOG.d("activity", activity);
        if (activity instanceof PlaylistActivity) {
            playlist.setSelected(true);
        }
        if (activity instanceof FoldersActivity) {
            folders.setSelected(true);
        }

        menuLayout = (LinearLayout) activity.findViewById(R.id.menuLayout);
        infoLine = (TextView) activity.findViewById(R.id.info_bar_line);

        settinsLayout = (LinearLayout) findViewById(R.id.fileSettinsLayout);
        settinsImage = (ImageView) findViewById(R.id.fileSettings);
        settinsImage.setOnClickListener(onOpenSettigns);

        hideShowSettinsLine(isSettingsVisible);
    }

    public void disableSettingButton() {
        settinsLayout.setVisibility(View.GONE);
        settinsImage.setVisibility(View.GONE);
    }

    public View addSettingView(View view){
        settinsLayout.addView(view);
        return view;
    }

    OnClickListener onOpenSettigns = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            isSettingsVisible = !isSettingsVisible;
            hideShowSettinsLine(isSettingsVisible);

        }
    };

    public void hideShowSettinsLine(boolean flag) {
        if (flag) {
            settinsLayout.setVisibility(View.VISIBLE);
            settinsImage.setImageResource(android.R.drawable.arrow_up_float);
        } else {
            settinsLayout.setVisibility(View.GONE);
            settinsImage.setImageResource(android.R.drawable.arrow_down_float);
        }
    }

    public void setInfoLineText(String text) {
        if (!StringUtils.isEmpty(text)) {
            infoLine.setText(text);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        LOG.d("On menu Press");
        isMenuVisible = !isMenuVisible;
        displayImageMenu();
        return false;
    }

    private void displayImageMenu() {
        if (menuLayout == null) {
            return;
        }
        if (isMenuVisible) {
            menuLayout.setVisibility(View.VISIBLE);
            Animation a = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
            a.setFillAfter(true);
            menuLayout.setLayoutAnimation(new LayoutAnimationController(a));
            menuLayout.startLayoutAnimation();

        } else {
            Animation a = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
            a.setFillAfter(true);
            menuLayout.setLayoutAnimation(new LayoutAnimationController(a));
            a.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    menuLayout.setVisibility(View.GONE);

                }
            });
            menuLayout.startLayoutAnimation();
        }
    }

}
