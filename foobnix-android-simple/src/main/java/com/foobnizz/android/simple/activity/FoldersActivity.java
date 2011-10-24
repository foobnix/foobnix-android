package com.foobnizz.android.simple.activity;

import java.io.File;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.foobnix.commons.LOG;
import com.foobnix.commons.RecurciveFiles;
import com.foobnix.commons.StringUtils;
import com.foobnix.util.pref.Pref;
import com.foobnizz.android.simple.R;
import com.foobnizz.android.simple.activity.hierarchy.GeneralListActivity;
import com.foobnizz.android.simple.activity.util.ModelsHelper;
import com.foobnizz.android.simple.adapter.FileItemAdapter;
import com.foobnizz.android.simple.adapter.ModelListAdapter;
import com.foobnizz.android.simple.core.FileItem;
import com.foobnizz.android.simple.core.FileItemProvider;
import com.foobnizz.android.simple.mediaengine.MediaModels;
import com.foobnizz.android.simple.widgets.RunnableDialog;

public class FoldersActivity extends GeneralListActivity<FileItem> {
    private static final String FOLDER_PATH = "FOLDER_PATH";
    final File ROOT_PATH = Environment.getExternalStorageDirectory();
    private TextView path;
    private File currentPath;

    @Override
    public Class<? extends ModelListAdapter<FileItem>> getAdapter() {
        return FileItemAdapter.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_list);
        onActivate(this);

        path = (TextView) findViewById(R.id.info_bar_line);

        String initPath = Pref.getStr(this, FOLDER_PATH, Environment.getExternalStorageDirectory().getPath());
        currentPath = new File(initPath);

        path.setText(currentPath.getPath());
        addItems(FileItemProvider.getFilesAndFoldersWithRoot(currentPath));

        Button btnCreateFolder = new Button(this, null, android.R.attr.buttonStyleSmall);
        btnCreateFolder.setText("Create a Download Directory");
        btnCreateFolder.setOnClickListener(onCreate);

        addSettingView(btnCreateFolder);

    }

    @Override
    public void onModelItemLongClickListener(FileItem model) {
        new RunnableDialog(this, "Action on " + model.getFile().getName())//

                .Action("Set As Playlist", new Runnable() {

                    @Override
                    public void run() {

                    }
                })//

                .Action("Add To Playlist", new Runnable() {
                    @Override
                    public void run() {

                    }
                }, model != null)//

                .Action("Delete", new Runnable() {

                    @Override
                    public void run() {

                    }
                }, model != null)//

                .Action("Set Download Location", new Runnable() {
                    @Override
                    public void run() {
                    }
                }, model.getFile().isDirectory())//
                .show();

    };

    @Override
    public List<FileItem> getInitItems() {
        return Collections.emptyList();
    }

    OnClickListener onDelete = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            for (FileItem item : getItems()) {
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
                        if (StringUtils.isEmpty(text)) {
                            File file = new File(currentPath, text);

                            LOG.d("Create file", file.getPath());
                            if (file.mkdir()) {
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getApplicationContext(), "Can't create directory", Toast.LENGTH_LONG).show();
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

    @Override
    public void onModelItemClickListener(FileItem fileItem) {
        if (fileItem.getFile().isDirectory()) {
            currentPath = fileItem.getFile();

            Pref.putStr(this, FOLDER_PATH, currentPath.getPath());

            path.setText(currentPath.getPath());

            getItems().clear();

            getItems().addAll(FileItemProvider.getFilesAndFoldersWithRoot(currentPath));
            adapter.notifyDataSetChanged();
        } else {
            List<FileItem> filesByPath = FileItemProvider.getFilesByPath(currentPath);

            MediaModels models = ModelsHelper.getModelsByFileItems(filesByPath);

            app.getPlaylist().setPlaylist(models.getItems());
            startActivity(new Intent(this, PlaylistActivity.class));
        }

    }

}
