package com.foobnix.activity.hierarchy;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foobnix.commons.StringUtils;
import com.foobnix.R;

public abstract class TopSecondLineActivity extends TopBarActivity {
    private LinearLayout settinsLayout;
    private View secondTopLine;
    private ImageView secondTopImage;
    private boolean isSettingsVisible = false;
    private TextView infoLine;

    public void onActivate(Activity activity) {
        super.onActivate(activity);
        settinsLayout = (LinearLayout) findViewById(R.id.fileSettinsLayout);
        secondTopLine = (View) findViewById(R.id.secondTopLine);
        secondTopImage = (ImageView) findViewById(R.id.secondTopLineImage);
        secondTopLine.setOnClickListener(onOpenSettigns);
        infoLine = (TextView) activity.findViewById(R.id.info_bar_line);

        hideShowSettinsLine(isSettingsVisible);
    }

    OnClickListener onOpenSettigns = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            isSettingsVisible = !isSettingsVisible;
            hideShowSettinsLine(isSettingsVisible);

        }
    };

    public void setInfoLineText(String text) {
        if (!StringUtils.isEmpty(text)) {
            infoLine.setText(text);
        }
    }

    public void disableSecondTopLine() {
        settinsLayout.setVisibility(View.GONE);
        secondTopLine.setVisibility(View.GONE);
    }

    public View addSettingView(View view) {
        settinsLayout.addView(view);
        return view;
    }

    public void hideShowSettinsLine(boolean flag) {
        if (flag) {
            settinsLayout.setVisibility(View.VISIBLE);
            secondTopImage.setImageResource(android.R.drawable.arrow_up_float);
        } else {
            settinsLayout.setVisibility(View.GONE);
            secondTopImage.setImageResource(android.R.drawable.arrow_down_float);
        }
    }

}
