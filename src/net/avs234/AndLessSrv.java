package net.avs234;


public class AndLessSrv {

	static {
		System.loadLibrary("lossless");
	}

	public static native int audioInit(int ctx, int mode);

	public static native boolean audioExit(int ctx);

	public static native boolean audioStop(int ctx);

	public static native boolean audioPause(int ctx);

	public static native boolean audioResume(int ctx);

	public static native int audioGetDuration(int ctx);

	public static native int audioGetCurPosition(int ctx);

	public static native boolean audioSetVolume(int ctx, int vol);

	public static native int alacPlay(int ctx, String file, int start);

	public static native int flacPlay(int ctx, String file, int start);

	public static native int apePlay(int ctx, String file, int start);

	public static native int wavPlay(int ctx, String file, int start);

	public static native int wvPlay(int ctx, String file, int start);

	public static native int mpcPlay(int ctx, String file, int start);

	public static native int[] extractFlacCUE(String file);

	public static native int wvDuration(int ctx, String file);

	public static native int apeDuration(int ctx, String file);

	public static native boolean libInit(int sdk);

	public static native boolean libExit();

	// errors returned by xxxPlay functions
	public static final int LIBLOSSLESS_ERR_NOCTX = 1;
	public static final int LIBLOSSLESS_ERR_INV_PARM = 2;
	public static final int LIBLOSSLESS_ERR_NOFILE = 3;
	public static final int LIBLOSSLESS_ERR_FORMAT = 4;
	public static final int LIBLOSSLESS_ERR_AU_GETCONF = 6;
	public static final int LIBLOSSLESS_ERR_AU_SETCONF = 7;
	public static final int LIBLOSSLESS_ERR_AU_BUFF = 8;
	public static final int LIBLOSSLESS_ERR_AU_SETUP = 9;
	public static final int LIBLOSSLESS_ERR_AU_START = 10;
	public static final int LIBLOSSLESS_ERR_IO_WRITE = 11;
	public static final int LIBLOSSLESS_ERR_IO_READ = 12;
	public static final int LIBLOSSLESS_ERR_DECODE = 13;
	public static final int LIBLOSSLESS_ERR_OFFSET = 14;
	public static final int LIBLOSSLESS_ERR_NOMEM = 15;

	// Audio context descriptor returned by audioInit().
	// Actually, it's a pointer to struct msm_ctx, see native code.
	// Used in all subsequent calls of native functions.
	private static int ctx = 0;

	public static final int MODE_NONE = 0;
	public static final int MODE_DIRECT = 1;
	public static final int MODE_LIBMEDIA = 2;
	public static final int MODE_CALLBACK = 3;

	// Ad hoc value. 0x2000 seems to be a maximum used by the driver. MSM
	// datasheets needed.
	public static int DEFAULT_VOLUME = 0x1000;

	public static void updateTrackLen(int time) {
	}
}
