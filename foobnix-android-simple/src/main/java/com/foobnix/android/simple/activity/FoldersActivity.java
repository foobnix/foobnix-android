package com.foobnix.android.simple.activity;

import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foobnix.android.simple.R;
import com.foobnix.android.simple.core.FileItem;
import com.foobnix.android.simple.core.FileItemAdapter;
import com.foobnix.android.simple.core.FileItemProvider;
import com.foobnix.android.simple.core.ModelListAdapter;
import com.foobnix.commons.LOG;
import com.foobnix.commons.RecurciveFiles;
import com.foobnix.commons.StringUtils;
import com.foobnix.commons.ViewBinder;
import com.foobnix.mediaengine.MediaModel;
import com.foobnix.mediaengine.MediaModels;
import com.foobnix.util.pref.Pref;

public class FoldersActivity extends GeneralListActivity<FileItem> {
    private static final String FOLDER_PATH = "FOLDER_PATH";
    final File ROOT_PATH = Environment.getExternalStorageDirectory();
    private TextView path;
    private File currentPath;
    private boolean isSettingsVisible = false;

    @Override
    public Class<? extends ModelListAdapter<FileItem>> getAdapter() {
        return FileItemAdapter.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onActivate(this);

        path = (TextView) findViewById(R.id.info_bar_line);

        String initPath = Pref.getStr(this, FOLDER_PATH, Environment.getExternalStorageDirectory().getPath());
        currentPath = new File(initPath);

        path.setText(currentPath.getPath());
        addItems(FileItemProvider.getFilesAndFoldersWithRoot(currentPath));

        ViewBinder.onClick(this, R.id.fileDelete, onDelete);
        ViewBinder.onClick(this, R.id.fileCreate, onCreate);

        settinsImage = (ImageView) findViewById(R.id.fileSettings);
        settinsImage.setOnClickListener(onOpenSettigns);

        settinsLayout = findViewById(R.id.fileSettinsLayout);

        hideShowSettinsLine(isSettingsVisible);

    }

    public void hideShowSettinsLine(boolean flag) {
        if (flag) {
            settinsLayout.setVisibility(View.VISIBLE);
            settinsImage.setImageResource(android.R.drawable.arrow_up_float);
        } else {
            settinsLayout.setVisibility(View.GONE);
            settinsImage.setImageResource(android.R.drawable.arrow_down_float);
        }
    }

    OnClickListener onOpenSettigns = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            isSettingsVisible = !isSettingsVisible;
            hideShowSettinsLine(isSettingsVisible);

        }
    };

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

    private View settinsLayout;
    private ImageView settinsImage;

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
            for (MediaModel model : models.getItems()) {
                if (model == null) {
                    continue;
                }
            }
            LOG.d("Models", filesByPath.size());
            Intent playlist = new Intent(this, PlaylistActivity.class);
            playlist.putExtra(MediaModels.class.getName(), models);
            startActivity(playlist);
        }

    }

}
