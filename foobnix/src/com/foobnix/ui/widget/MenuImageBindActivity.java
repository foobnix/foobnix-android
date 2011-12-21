package com.foobnix.ui.widget;

import android.app.Activity;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import com.foobnix.util.LOG;

public class MenuImageBindActivity {
	public MenuImageBindActivity(final Activity context, int buttonId, final View.OnClickListener onClick) {
		View view = (View) context.findViewById(buttonId);
		if (view == null) {
			return;
		}
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClick.onClick(v);
			}
		});
		view.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					v.setBackgroundColor(Color.GRAY);
					LOG.d("on image down");
				} else if (MotionEvent.ACTION_UP == event.getAction()) {
					v.setBackgroundColor(Color.TRANSPARENT);
				}
				return false;
			}

		});
	}
}