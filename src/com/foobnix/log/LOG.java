package com.foobnix.log;

import android.util.Log;

public class LOG {

	public static void d(Object... messages) {
		for (Object msg : messages) {
			Log.d("~~FOOBNIX~~", msg.toString());
		}
	}
}
