package com.foobnix.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.foobnix.R;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class ADS {
	
	public static void gone(Activity a){
		FrameLayout frame = (FrameLayout) a.findViewById(R.id.adFrame);
		frame.setVisibility(View.GONE);
	}
	
	public static void activate(Activity context, AdView adView){
		String unitID = context.getString(R.string.AD_UNIT_ID);
		Log.d("DEBUG", "AD_UNIT_ID:" + unitID);
		adView = new AdView(context, AdSize.BANNER, unitID);
		FrameLayout frame = (FrameLayout) context.findViewById(R.id.adFrame);
		frame.addView(adView);

		AdRequest adRequest = new AdRequest();
		adView.loadAd(adRequest);
	}
	public static void destory(AdView adView){
		if (adView != null) {
			adView.destroy();
		}
	}

}
