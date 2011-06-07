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
package com.foobnix.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.foobnix.R;

public class Dialogs {

	public static void showCreateFolder(final Context context, final CommandString okCommand) {
		final EditText name = new EditText(context);
		final AlertDialog createDialog = new AlertDialog.Builder(context)//
		        .setView(name)//
		        .setTitle(R.string.Create_Folder)//
		        .setPositiveButton(R.string.Create, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
				        okCommand.run(name.getText().toString());
			        }
		        })//
		        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
				        dialog.cancel();
			        }
		        }).show();
	}

}
