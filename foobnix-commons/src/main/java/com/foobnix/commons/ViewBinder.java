package com.foobnix.commons;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class ViewBinder {

    public static TextView text(final Activity c, int id, final String text) {
        TextView textView = (TextView) c.findViewById(id);
        if (StringUtils.isEmpty(text)) {
            textView.setText("");
        } else {
            textView.setText(text);
        }
        return textView;
    }

    public static View onClickActivity(final Activity c, int id, final Class<?> clazz) {
        View button = (View) c.findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                c.startActivity(new Intent(c, clazz));
            }
        });
        return button;
    }

    public static View onClick(Activity context, int id, View.OnClickListener onClick) {
        View button = (View) context.findViewById(id);
        if (button == null) {
            return null;
        }
        button.setOnClickListener(onClick);
        return button;
    }

    public static View onClick(View parent, int id, View.OnClickListener onClick) {
        View button = (View) parent.findViewById(id);
        button.setOnClickListener(onClick);
        return button;
    }

}