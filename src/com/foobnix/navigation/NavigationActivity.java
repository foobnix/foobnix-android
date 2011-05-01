package com.foobnix.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.foobnix.PlayerActivity;
import com.foobnix.R;
import com.foobnix.db.DataHelper;
import com.foobnix.log.LOG;

public class NavigationActivity extends Activity {

	private DataHelper dh;

	@Override
	protected void onStart() {
		super.onStart();
		LOG.d("on Start NavigationActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		dh = new DataHelper(this);
		LOG.d("onCreate NavigationActivity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nav);

		ListView list = (ListView) findViewById(R.id.dir_list);
		list.setClickable(true);

		final NavigationAdapter navAdapter = new NavigationAdapter(this);
		list.setAdapter(navAdapter);
		
		Button add = (Button) findViewById(R.id.navAddAll);
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				navAdapter.checkAll();
			}
		});
		
		Button newList = (Button) findViewById(R.id.navNewPlayList);
		newList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dh.deleteAll();
				navAdapter.checkAll();
			}
		});


		Button cancel = (Button) findViewById(R.id.navPlayer);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), PlayerActivity.class);
				startActivity(myIntent);
			}
		});

	}

}
