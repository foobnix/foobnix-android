package org.foobnix.android.simple.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.foobnix.android.simple.R;
import org.foobnix.android.simple.core.FileItem;
import org.foobnix.android.simple.core.FileItemAdapter;
import org.foobnix.android.simple.core.FileItemProvider;
import org.foobnix.android.simple.core.OnModelClickListener;
import org.foobnix.android.simple.core.TopFileItem;
import org.foobnix.android.simple.mediaengine.MediaService;
import org.foobnix.android.simple.mediaengine.Models;
import org.foobnix.android.simple.mediaengine.ModelsHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.foobnix.commons.LOG;
import com.foobnix.commons.RecurciveFiles;
import com.foobnix.commons.ViewBinder;
import com.google.common.base.Strings;

public class FoldersActivity extends AppActivity implements OnModelClickListener<FileItem> {
    final List<FileItem> items = new ArrayList<FileItem>();
    File rootPath = Environment.getExternalStorageDirectory();
    private TextView path;
    private File currentPath;
    private boolean isSettingsVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folderes);

        path = (TextView) findViewById(R.id.fileCurrentPath);

        ListView listView = (ListView) findViewById(R.id.listView);

        path.setText(rootPath.getPath());
        currentPath = rootPath;

        items.addAll(FileItemProvider.getFilesAndFoldersByPath(rootPath));

        adapter = new FileItemAdapter(this, items);
        adapter.setOnModelClickListener(this);
        listView.setAdapter(adapter);

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
    private View settinsLayout;
    private ImageView settinsImage;

    @Override
    public void onClick(FileItem fileItem) {
        if (fileItem.getFile().isDirectory()) {
            currentPath = fileItem.getFile();
            path.setText(currentPath.getPath());

            File top = currentPath.getParentFile();
            items.clear();

            if (!top.equals(rootPath.getParentFile())) {
                items.add(new TopFileItem(top));
                LOG.d("top", rootPath.getPath(), top.getPath());
            } else {
                LOG.d("not top", rootPath.getPath(), top.getPath());
            }

            items.addAll(FileItemProvider.getFilesAndFoldersByPath(currentPath));
            adapter.notifyDataSetChanged();
        } else {
            MediaService.playPath(fileItem.getFile().getPath());

            Intent intent = new Intent(this, PlaylistActivity.class);
            List<FileItem> filesByPath = FileItemProvider.getFilesByPath(currentPath);

            Models models = ModelsHelper.getModelsByFileItems(filesByPath);
            app.getItems().clear();
            app.getItems().addAll(models.getItems());

            startActivity(intent);
        }

    }

}
