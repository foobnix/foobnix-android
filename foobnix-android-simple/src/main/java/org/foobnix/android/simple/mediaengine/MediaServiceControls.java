package org.foobnix.android.simple.mediaengine;

import android.app.Service;
import android.content.Context;
import android.content.Intent;

public abstract class MediaServiceControls extends Service {
	protected static String EXTRA_PATH = "PATH";
	public enum Controls {
		PLAY_PAHT
	}

	public static void playPath(Context context, String path) {
		Intent service = new Intent(context, MediaService.class);
		service.setAction(Controls.PLAY_PAHT.name());
		service.putExtra(EXTRA_PATH, path);
		context.startService(service);
	}

}
