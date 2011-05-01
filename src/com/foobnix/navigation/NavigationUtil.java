package com.foobnix.navigation;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Environment;

import com.foobnix.log.LOG;
import com.foobnix.model.NavItem;

public class NavigationUtil {
	private final static String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
	private final static List<String> SUPPORTED_EXT = Arrays.asList(".mp3");

	public static List<NavItem> getRootNavItems() {
		return getNavItemsByPath(ROOT_PATH);
	}

	public static List<NavItem> getNavItemsByPath(String path) {
		LOG.d("Get items by path " + path);

		List<NavItem> result = new ArrayList<NavItem>();
		result.add(new NavItem("..", new File(path).getParent(), false));

		File file = new File(path);

		if (file.isFile()) {
			new NavItem(file.getName(), file.getPath(), true);
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

				String[] subFiles = root.list();
				if (subFiles == null) {
					return true;
				}

				for (String name : subFiles) {
					File current = new File(root, name);
					if (current.isFile() && isSupportedExt(name)) {
						return true;
					} else if (current.isDirectory()) {
						return true;
					}
				}
				return false;
			}
		});

		for (String name : dirs) {
			File current = new File(path, name);
			
			String fileName = current.getName();

			if (current.isFile()) {
				fileName = fileName.substring(0, fileName.length() - 4);
				result.add(new NavItem(fileName, current.getPath(), current.isFile()));
			} else {
				result.add(new NavItem(String.format("[%s]", fileName), current.getPath(), current.isFile()));
			}
		}

		return result;
	}

	private static boolean isSupportedExt(String name) {
		for (String ext : SUPPORTED_EXT) {
			if (name.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}
}
