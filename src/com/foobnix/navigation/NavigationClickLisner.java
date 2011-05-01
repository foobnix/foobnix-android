package com.foobnix.navigation;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.foobnix.log.LOG;

public class NavigationClickLisner implements OnItemClickListener {

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		LOG.d("Click");
	}

}
