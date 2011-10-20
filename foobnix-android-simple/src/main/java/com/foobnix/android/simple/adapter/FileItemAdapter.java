package com.foobnix.android.simple.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.foobnix.android.simple.R;
import com.foobnix.android.simple.core.FileItem;
import com.foobnix.android.simple.core.TopFileItem;

public class FileItemAdapter extends ModelListAdapter<FileItem> {
    public FileItemAdapter(Activity context, List<FileItem> items) {
        super(context, items);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FileItem item = getItem(position);

        View newView;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            newView = inflater.inflate(R.layout.item_file, null);
        } else {
            newView = convertView;
        }

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

        View clickable = (View) newView.findViewById(R.id.fileClickable);
        clickable.setOnClickListener(new OnModelClick(item));
        
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

        return newView;
    }
}
