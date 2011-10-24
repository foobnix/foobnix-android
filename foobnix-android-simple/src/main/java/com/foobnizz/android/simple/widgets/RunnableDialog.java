/* Copyright (c) 2011 Ivan Ivanenko <ivan.ivanenko@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. */
package com.foobnizz.android.simple.widgets;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class RunnableDialog {
	private List<String> actions = new ArrayList<String>();
	private List<Runnable> commands = new ArrayList<Runnable>();

	private Builder dialogBuilder;
	private ListView list;
	private Context context;

    public RunnableDialog(Context context, String title) {
        this.context = context;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, actions);
		list = new ListView(context);
		list.setAdapter(adapter);
		dialogBuilder = new AlertDialog.Builder(context).setTitle(title).setView(list);
	}
	
	public RunnableDialog Action(String text, Runnable runnable) {
		actions.add(text);
		commands.add(runnable);
		return this;
	}

	public RunnableDialog Action(String text, Runnable runnable, boolean show) {
		if (!show) {
			return this;
		}
		actions.add(text);
		commands.add(runnable);
		return this;
	}

	public void show() {
        final AlertDialog dialog = dialogBuilder.create();
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                Toast.makeText(context, "Action: " + actions.get(pos), Toast.LENGTH_SHORT).show();
                commands.get(pos).run();
                dialog.hide();

            }
        });

        dialog.show();
	}
}
