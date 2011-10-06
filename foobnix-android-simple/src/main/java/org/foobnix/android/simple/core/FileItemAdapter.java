package org.foobnix.android.simple.core;

import java.util.List;

import org.foobnix.android.simple.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FileItemAdapter extends ArrayAdapter<FileItem> {

    private final Activity context;
    private final List<FileItem> items;
    private OnModelClickListener<FileItem> onModelClickListener;

    public FileItemAdapter(Activity context, List<FileItem> items) {
        super(context, -1, items);
        setNotifyOnChange(true);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FileItem fileItem = getItem(position);

        View newView;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            newView = inflater.inflate(R.layout.file_item, null);
        } else {
            newView = convertView;
        }

        ImageView image = (ImageView) newView.findViewById(R.id.fileImage);
        if (fileItem.getBitmap() != null) {
            image.setImageBitmap(fileItem.getBitmap());
        } else {
            image.setImageResource(R.drawable.icon);
        }

        TextView name = (TextView) newView.findViewById(R.id.fileName);
        name.setText(fileItem.getDisplayName());

        if (fileItem.getFile().isFile()) {
            name.setTypeface(null, Typeface.NORMAL);
        } else {
            name.setTypeface(null, Typeface.BOLD);
        }

        View clickable = (View) newView.findViewById(R.id.fileClickable);
        clickable.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (onModelClickListener != null) {
                    onModelClickListener.onClick(fileItem);
                    notifyDataSetChanged();
                }
            }
        });

        CheckBox checkBox = (CheckBox) newView.findViewById(R.id.fileCheckbox);
        if (fileItem instanceof TopFileItem) {
            checkBox.setVisibility(View.GONE);
        } else {
            checkBox.setVisibility(View.VISIBLE);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fileItem.setChecked(isChecked);
            }
        });

        return newView;
    }

    public void setOnModelClickListener(OnModelClickListener<FileItem> onModelClickListener) {
        this.onModelClickListener = onModelClickListener;
    }

    public OnModelClickListener<FileItem> getOnModelClickListener() {
        return onModelClickListener;
    }

}
