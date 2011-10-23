package com.foobnizz.android.simple.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foobnizz.android.simple.R;

public class PreflineWidget extends LinearLayout {

    private ImageView image;
    private TextView title;
    private TextView summary;

    public PreflineWidget(Context context, int imgId, String txt) {
        this(context, null);
        setImage(imgId);
        setTitle(txt);
    }

    public PreflineWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.item_pref, this);

        image = (ImageView) layout.findViewById(R.id.icon);
        title = (TextView) layout.findViewById(R.id.login);
        summary = (TextView) layout.findViewById(R.id.password);
    }
    
    public void setImage(int resId){
        image.setImageResource(resId);
    }
    public void setTitle(String txt){
        title.setText(txt);
    }

    public void setSummary(String txt) {
        summary.setText(txt);
    }
    

}
