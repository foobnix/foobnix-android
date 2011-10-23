package com.foobnizz.android.simple.mediaengine;

import java.util.concurrent.atomic.AtomicBoolean;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.foobnix.commons.LOG;

public class MediaPlayerFeatures extends MediaPlayerEngine {
    IntentFilter filter;
    private Handler handler = new Handler();
    protected Context context;

    protected MediaPlayerFeatures(Context context) {
        super(null);
        this.context = context;
        TelephonyManager tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        tmgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        // plug un plug headset
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

        // income call
        context.registerReceiver(headsetReciever, filter);
    }

    private BroadcastReceiver headsetReciever = new BroadcastReceiver() {
        private boolean needResume = false;
        private AtomicBoolean lock = new AtomicBoolean(false);

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isIntentHeadsetRemoved(intent)) {
                if (lock.get()) {
                    LOG.d("Skip second time");
                    return;
                }
                needResume = isPlaying();
                LOG.d("Headphone pause, isplaygin", needResume);
                pause();
                lock.set(true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lock.set(false);
                    }
                }, 1000);
            } else if (isIntentHeadsetInserted(intent)) {
                LOG.d("Headphone resume", needResume);
                if (needResume) {
                    start();
                }
            }
        }

        private boolean isIntentHeadsetInserted(Intent intent) {
            return (intent.getAction().equalsIgnoreCase(Intent.ACTION_HEADSET_PLUG) && intent.getIntExtra("state", 0) != 0);
        }

        private boolean isIntentHeadsetRemoved(Intent intent) {
            return ((intent.getAction().equalsIgnoreCase(Intent.ACTION_HEADSET_PLUG) && intent.getIntExtra("state", 0) == 0) || intent
                    .getAction().equalsIgnoreCase(AudioManager.ACTION_AUDIO_BECOMING_NOISY));
        }
    };

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        private boolean needResume;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING || state == TelephonyManager.CALL_STATE_OFFHOOK) {
                needResume = isPlaying();
                pause();
            } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                if (needResume) {
                    start();
                }
            }
        }
    };

}
