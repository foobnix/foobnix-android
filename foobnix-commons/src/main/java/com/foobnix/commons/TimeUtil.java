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
package com.foobnix.commons;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	public static String durationSecToString(String millis) {
		return durationToString(Integer.parseInt(millis) * 1000);
	}

	public static String getTimeFormated(int[] values) {
		if (values[0] < 0 || values[1] < 0) {
			return "00:00";
		}
		String h = "" + values[0];
		String m = "" + values[1];

		if (m.length() == 1) {
			m = "0" + m;
		}

		return h + ":" + m;
	}

	public static String durationSecToString(int millis) {
		try {
			return durationToString(millis * 1000);
		} catch (Exception e) {
			e.printStackTrace();
			return "00:00";
		}
	}

	public static String durationToString(int millis) {
		String seconds = "" + (int) ((millis / 1000) % 60);
		int m = (int) ((millis / 1000) / 60);
		if (m > 60) {
			return "";
		}
		String minutes = "" + m;
		String hours = "" + (int) ((millis / 1000) / 3600);

		if (seconds.length() == 1) {
			seconds = "0" + seconds;
		}
		if (minutes.length() == 1) {
			minutes = "0" + minutes;
		}

		return String.format("%s:%s", minutes, seconds);
	}

	public static int durationToSec(int millis) {
		return (int) ((millis / 1000) % 60);
	}

	public static String getYear(Date date) {
		if (date == null) {
			return null;
		}
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		return "" + instance.get(Calendar.YEAR);
	}
}
