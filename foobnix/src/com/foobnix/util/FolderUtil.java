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

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import android.os.Environment;

import com.foobnix.model.FModel;
import com.foobnix.model.FModelBuilder;

public class FolderUtil {
	public final static String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();

	public static boolean createParentDir(String path, String name) {
		File file = new File(path);
		if (file.isDirectory()) {
			return createDir(file.getParent(), name);
		}
		return createDir(path, name);
	}

	public static boolean createDir(String path, String name) {
		File file = new File(getFolderPath(path), name);
		if (file.exists()) {
			return false;
		}
		LOG.d("Create Dirs", path, name);
		boolean result = file.mkdirs();
		return result;

	}

	public static String normalizePath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.startsWith(ROOT_PATH)) {
			return path.substring(FolderUtil.ROOT_PATH.length());
		} else {
			return path;
		}
	}

	public static String getNameFromPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		int lastIndexOf = path.lastIndexOf('/');
		return path.substring(lastIndexOf + 1);
	}

	public static String getFolderPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return FolderUtil.ROOT_PATH;
		}
		File file = new File(path);
		if (file.isDirectory()) {
			return path;
		} else {
			return file.getParent();
		}

	}

	public static List<FModel> getRootNavItems() {
		return getNavItemsByPath(ROOT_PATH);
	}

	public static String getSizeMb(File file) {
		long available = file.length();
		return String.format("%.1fM", (double) available / 1048576);
	}


	public static String getExt(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		int index = path.lastIndexOf(".");
		if (index == -1) {
			return "";
		}
		return path.substring(index + 1).toUpperCase();
	}

	public static List<FModel> getAllFilesRecursive(String path) {
		List<FModel> result = new ArrayList<FModel>();
		String[] extensions = {"mp3", "ogg"};
		Collection<File> listFiles = FileUtils.listFiles(new File(path), extensions, true);

		for (File file : listFiles) {
			String fileName = file.getName();
			String cutName = fileName.substring(0, fileName.length() - 4);
			FModel navItem = FModelBuilder//
			        .PatternText(cutName)//
			        .addPath(file.getPath())//
			        .addExt(getExt(fileName))//
			        .addSize(getSizeMb(file));
			result.add(navItem);
		}

		return result;
		
	}

	public static List<FModel> getNavItemsByPath(String path) {
		LOG.d("Get items by path " + path);

		List<FModel> result = new ArrayList<FModel>();
		String parent = new File(path).getParent();

		if (!parent.equals("/")) {
			result.add(FModelBuilder.Folder("..").addPath(parent));
		}

		File file = new File(path);

		if (file.isFile()) {
			FModel navItem = FModelBuilder//
			        .PatternText(file.getName())//
			        .addPath(file.getPath())//
			        .addExt(getExt(file.getName()))//
			        .addSize(getSizeMb(file));
			result.add(navItem);
			return result;

		}

		String[] dirs = file.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				LOG.d(dir, filename);
				File root = new File(dir, filename);

				if (root.isFile()) {
					LOG.d("is file", filename);
					return isSupportedExt(filename);
				}
				
				return true;
			}
		});

		if (dirs == null) {
			return result;
		}
		for (String name : dirs) {
			File current = new File(path, name);

			String fileName = current.getName();

			if (current.isFile()) {
				String cutName = fileName.substring(0, fileName.length() - 4);
				FModel navItem = FModelBuilder//
				        .PatternText(cutName)//
				        .addPath(current.getPath())//
				        .addExt(getExt(fileName))//
				        .addSize(getSizeMb(new File(current.getParent(), fileName)));

				result.add(navItem);

			} else {
				result.add(FModelBuilder.Folder(fileName).addPath(current.getPath()));
			}
		}
		Collections.sort(result, new FileComparator());
		return result;
	}

	public static void deleteFiles(String path) {

		File file = new File(path);
		if (file.isFile()) {
			file.delete();
			return;
		}

		String deleteCmd = "rm -r " + path;
		LOG.d("DELETE", path);

		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec(deleteCmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean isSupportedExt(String name) {
		for (String ext : C.get().supportedExts) {
			if (name.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}
}
