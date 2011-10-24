package com.foobnix.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.foobnix.core.FileItem;
import com.foobnix.core.TopFileItem;
import com.foobnix.R;

public class FileItemAdapter extends ModelListAdapter<FileItem> {
    public FileItemAdapter(Activity context, List<FileItem> items) {
        super(context, items);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_file;
    }

    @Override
    public void getView(final FileItem item, View newView) {
        ImageView image = (ImageView) newView.findViewById(R.id.fileImage);
        if (item.getBitmap() != null) {
            image.setImageBitmap(item.getBitmap());
        } else {
            image.setImageResource(R.drawable.icon);
        }

        TextView name = (TextView) newView.findViewById(R.id.fileName);
        name.setText(item.getDisplayName());

        if (item.getFile().isFile()) {
            name.setTypeface(null, Typeface.NORMAL);
            image.setImageResource(R.drawable.music_file);
        } else {
            name.setTypeface(null, Typeface.BOLD);
            image.setImageResource(R.drawable.music_folder);
        }

        CheckBox checkBox = (CheckBox) newView.findViewById(R.id.fileCheckbox);
        if (item instanceof TopFileItem) {
            checkBox.setVisibility(View.GONE);
        } else {
            checkBox.setVisibility(View.GONE);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setChecked(isChecked);
            }
        });

    }

}
