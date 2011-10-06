package org.foobnix.android.simple.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.foobnix.android.simple.R;
import org.foobnix.android.simple.core.FileItem;
import org.foobnix.android.simple.core.FileItemAdapter;
import org.foobnix.android.simple.core.OnModelClickListener;
import org.foobnix.android.simple.core.TopFileItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.foobnix.commons.LOG;
import com.foobnix.commons.RecurciveFiles;
import com.foobnix.commons.ViewBinder;
import com.google.common.base.Strings;

public class HomeActivity extends Activity implements OnModelClickListener<FileItem> {
    final List<FileItem> items = new ArrayList<FileItem>();
    private final File rootPath = Environment.getExternalStorageDirectory();
    private TextView path;
    private File currentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        path = (TextView) findViewById(R.id.fileCurrentPath);

        ListView listView = (ListView) findViewById(R.id.listView);

        path.setText(rootPath.getPath());
        currentPath = rootPath;

        File[] listFiles = rootPath.listFiles();
        for (File file : listFiles) {
            items.add(new FileItem(file));
        }

        adapter = new FileItemAdapter(this, items);
        adapter.setOnModelClickListener(this);
        listView.setAdapter(adapter);

        ViewBinder.onClick(this, R.id.fileDelete, onDelete);
        ViewBinder.onClick(this, R.id.fileCreate, onCreate);

    }

    OnClickListener onDelete = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            for (FileItem item : items) {
                if (item.isChecked()) {
                    RecurciveFiles.deleteFileOrDir(item.getFile());
                    LOG.d("Delete", item.getFile().getPath());
                }
            }
            adapter.notifyDataSetChanged();

        }
    };

    OnClickListener onCreate = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            createFolderDialog();
        }
    };

    public void createFolderDialog() {
        final EditText name = new EditText(this);
        new AlertDialog.Builder(this)//
                .setView(name)//
                .setTitle("Create Directory")//
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String text = name.getText().toString();
                        if (!Strings.isNullOrEmpty(text)) {
                            File file = new File(currentPath, text);

                            LOG.d("Create file", file.getPath());
                            if (file.mkdir()) {
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getApplicationContext(), "Can't create directory", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }

                    }
                })//
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                }).show();

    }

    private FileItemAdapter adapter;

    @Override
    public void onClick(FileItem fileItem) {
        if (fileItem.getFile().isDirectory()) {
            currentPath = fileItem.getFile();
            path.setText(currentPath.getPath());

            File[] listFiles = currentPath.listFiles();
            items.clear();

            File top = currentPath.getParentFile();

            if (!top.equals(rootPath.getParentFile())) {
                items.add(new TopFileItem(top));
                LOG.d("top", rootPath.getPath(), top.getPath());
            } else {
                LOG.d("not top", rootPath.getPath(), top.getPath());
            }

            for (File file : listFiles) {
                items.add(new FileItem(file));
            }
        }

    }

}
