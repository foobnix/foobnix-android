package com.foobnix.navigation;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.foobnix.R;
import com.foobnix.db.DataHelper;
import com.foobnix.log.LOG;
import com.foobnix.model.NavItem;
import com.foobnix.model.Song;

public class NavigationAdapter extends ArrayAdapter<NavItem> {
	private final Activity context;
	private static List<NavItem> items = NavigationUtil.getRootNavItems();
	private final List<View> cache = new ArrayList<View>();
	private DataHelper dh;

	public NavigationAdapter(Activity context) {
		super(context, -1, items);
		this.context = context;
		dh = new DataHelper(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View newView;
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			newView = inflater.inflate(R.layout.nav_item, null);
		} else {
			newView = convertView;
		}
		cache.add(newView);

		final CheckBox checkBox = (CheckBox) newView.findViewById(R.id.navCheckBox);
		checkBox.setChecked(false);
		final TextView text = (TextView) newView.findViewById(R.id.navTxtView);
		final NavItem navItem = items.get(position);

		text.setOnClickListener(new OnTextItemLisner(context, navItem, checkBox));

		text.setText(items.get(position).getText());

		return newView;
	}

	public void checkAll() {
		LOG.d("check all");
		for (NavItem item : items) {
			if (item.isFile()) {
				dh.insert(new Song(item));
			}
		}
		for (View view : cache) {
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.navCheckBox);
			checkBox.setChecked(true);
		}
	}

	public void update(String path) {
		clear();
		for (NavItem item : NavigationUtil.getNavItemsByPath(path)) {
			add(item);
		}
	}

	public void setItems(List<NavItem> items) {
		this.items = items;
	}

	public List<NavItem> getItems() {
		return items;
	}

	class OnButtonItemLisner implements View.OnClickListener {
		protected NavItem navItem;
		private CheckBox checkBox;
		private Button button;

		public OnButtonItemLisner(Context context, NavItem navItem, CheckBox checkBox, Button button) {
			this.navItem = navItem;
			this.checkBox = checkBox;
			this.button = button;
		}

		@Override
		public void onClick(View v) {
			LOG.d("paren");
			if (navItem.isChecked()) {
				checkBox.setChecked(false);
				// button.setText("Add");
				navItem.setChecked(false);

			} else {
				dh.insert(new Song(navItem));

				checkBox.setChecked(true);
				// button.setText("Del");
				navItem.setChecked(true);
			}

		}
	}

	class OnTextItemLisner extends OnButtonItemLisner {

		public OnTextItemLisner(Context context, NavItem navItem, CheckBox checkBox) {
			super(context, navItem, checkBox, null);
		}

		@Override
		public void onClick(View v) {
			LOG.d("child");
			if (navItem.isFile()) {
				super.onClick(v);
			} else {
				update(navItem.getPath());
			}
		}

	}

}
