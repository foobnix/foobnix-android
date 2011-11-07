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
package com.foobnix.util;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import com.foobnix.commons.string.StringUtils;
import com.foobnix.mediaengine.MediaModel;

/**
 *
 */
public class SongUtil {
    private static final int KB = 1024;
    private static final int MB = KB * 1024;
    private static final int GB = MB * 1024;

	public static String capitilizeWords(String text) {
		String[] split = text.split(" ");
		StringBuilder result = new StringBuilder();
		for (String str : split) {
			String word = str.trim();
			if (StringUtils.isNotEmpty(word)) {
				String first = ("" + word.charAt(0)).toUpperCase();
				if (word.length() > 1) {
					first = first + word.substring(1);
				}
				result.append(first);
				result.append(" ");
			}
		}
		return result.toString().trim();

	}

    /**
     * Get remove url size in bytes
     * 
     * @param urlPath
     * @return
     */
    public static int getRemoteSize(String urlPath) {
        URL url;
        int len = 0;
        try {
            url = new URL(urlPath);
            URLConnection con = url.openConnection();
            len = con.getContentLength();
            con.getInputStream().close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        return len;
    }

    public static String getNumWithZero(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return "" + num;
    }

    public static double getMB(double bytes) {
        return (double) bytes / MB;
    }

    public static double getGB(double bytes) {
        return (double) bytes / GB;
    }


    public static String getArtist(String text) {
        int index = text.indexOf("-");
        if (index > 1) {
            return text.substring(0, index).trim();
        }
		return "Undefined";
    }

    public static String getTitle(String text) {
        int index = text.indexOf("-");
        if (index > 0) {
            return text.substring(index + 1, text.length()).trim();
        }
        return text;
    }

    public static boolean isRemote(String path) {
        if (StringUtils.isEmpty(path)) {
            return true;
        }
        if (path.startsWith("http")) {
            return true;
        }

        return false;
    }

    public static void updateArtist(List<MediaModel> models, String artist) {
        for (MediaModel model : models) {
            model.setArtist(artist);
        }
    }

    /*
     * public static void updateType(List<MediaModel> models, TYPE type) { for
     * (MediaModel model : models) { model.setType(type); } }
     */

    public static void updatePositions(List<MediaModel> models) {
        int pos = 0;
        for (MediaModel model : models) {
            model.setPosition(pos);
            pos++;
        }
    }

}