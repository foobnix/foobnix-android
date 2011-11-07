package com.foobnix.activity;

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

import com.foobnix.R;
import com.foobnix.activity.hierarchy.GeneralListActivity;
import com.foobnix.activity.util.ModelsHelper;
import com.foobnix.adapter.FileItemAdapter;
import com.foobnix.adapter.ModelListAdapter;
import com.foobnix.commons.log.LOG;
import com.foobnix.commons.pref.Pref;
import com.foobnix.commons.string.StringUtils;
import com.foobnix.core.FileItem;
import com.foobnix.core.FileItemProvider;
import com.foobnix.mediaengine.MediaModels;
import com.foobnix.prefs.Prefs;
import com.foobnix.widgets.RunnableDialog;

public class FoldersActivity extends GeneralListActivity<FileItem> {
    private TextView path;
    private File currentPath;
    String[] EXTS = new String[] { ".mp3" };

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

        String initPath = Pref.getStr(this, Prefs.FOLDER_PREV_PATH, Environment.getExternalStorageDirectory().getPath());
        currentPath = new File(initPath);

        path.setText(currentPath.getPath());

        List<FileItem> rootList = FileItemProvider.getRootList(currentPath, EXTS);
        LOG.d("add list size", rootList.size());
        addItems(rootList);

        
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
                        List<FileItem> filesByPath = FileItemProvider.getFilesByPath(currentPath);
                        MediaModels models = ModelsHelper.getModelsByFileItems(filesByPath);
                        app.getPlaylist().setPlaylist(models.getItems());
                        startActivity(new Intent(getApplicationContext(), PlaylistActivity.class));
                    }
                })//

                .Action("Add To Playlist", new Runnable() {
                    @Override
                    public void run() {
                        List<FileItem> filesByPath = FileItemProvider.getFilesByPath(currentPath);
                        MediaModels models = ModelsHelper.getModelsByFileItems(filesByPath);
                        app.getPlaylist().addToPlaylist(models.getItems());
                        startActivity(new Intent(getApplicationContext(), PlaylistActivity.class));

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
                    // RecurciveFiles.deleteFileOrDir(item.getFile());
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

            Pref.putStr(this, Prefs.FOLDER_PREV_PATH, currentPath.getPath());
            path.setText(currentPath.getPath());

            getItems().clear();
            getItems().addAll(FileItemProvider.getRootList(currentPath, EXTS));
            adapter.notifyDataSetChanged();
        } else {
            List<FileItem> filesByPath = FileItemProvider.getFilesByPath(currentPath);
            MediaModels models = ModelsHelper.getModelsByFileItems(filesByPath);
            app.getPlaylist().setPlaylist(models.getItems());
            startActivity(new Intent(this, PlaylistActivity.class));
        }

    }

}
