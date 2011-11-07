package foobnix.foobnix_samba_player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jcifs.smb.SmbFile;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.foobnix.mediaengine.MediaPlayerCore;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

public class HelloAndroidActivity extends Activity {

    private static String TAG = "DEBUG";
    File bufferedFile;
    private MediaPlayerCore core;

    /**
     * Called when the activity is first created.
     * 
     * @param savedInstanceState
     *            If the activity is being re-initialized after previously being
     *            shut down then this Bundle contains the data it most recently
     *            supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
     *            is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bufferedFile = new File(Environment.getExternalStorageDirectory(), "demo.mp3");
        Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
        core = new MediaPlayerCore(this);
        new Copy().execute();
        FileInputStream file;
        try {
            file = new FileInputStream(bufferedFile);
            // Thread.sleep(2000);
            core.playDataSource(file.getFD());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Copy extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            try {
                Files.copy(new DemoSupplier(), bufferedFile);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ERROR", "copy error", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "Playing");
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }

    class DemoSupplier implements InputSupplier<InputStream> {
        @Override
        public InputStream getInput() throws IOException {
            SmbFile file = new SmbFile("smb://10.1.14.6/shara/Avantasia - Another Angel Down.mp3");
            return file.getInputStream();
        }
    };
}
