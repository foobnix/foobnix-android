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

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foobnix.R;

public class StarTab extends LinearLayout {

	private LinearLayout view;
	private ImageView image;
	private TextView text;

	/**
	 * @param context
	 * @param attrs
	 */
	public StarTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = (LinearLayout) layoutInflater.inflate(R.layout.star_tab, this);
		image = (ImageView) view.findViewById(R.id.starImage);
		text = (TextView) view.findViewById(R.id.starText);
	}

	public void active() {
		image.setImageResource(android.R.drawable.btn_star_big_on);
		text.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

	}

	public void unactive() {
		image.setImageResource(android.R.drawable.btn_star_big_off);
		text.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);

	}

	public void setText(CharSequence txt) {
		text.setText(txt);
	}

	/**
	 * @param idText
	 */
	public void setText(int id) {
		text.setText(id);

	}

}
