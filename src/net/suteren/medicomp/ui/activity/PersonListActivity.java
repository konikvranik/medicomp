package net.suteren.medicomp.ui.activity;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.ui.adapter.PersonListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PersonListActivity extends MedicompActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (listView == null)
			listView = requestListView();
		if (listView != null)
			listView.setAdapter(getAdapter());
		super.onNewIntent(intent);
	}

	@Override
	protected void onResume() {
		if (listView == null)
			listView = requestListView();
		if (listView != null)
			listView.setAdapter(getAdapter());
		super.onResume();
	}

	@Override
	protected void onRestart() {
		if (listView == null)
			listView = requestListView();
		if (listView != null)
			listView.setAdapter(getAdapter());
		super.onRestart();
	}

	@Override
	protected ListView requestListView() {
		return (ListView) getWindow().findViewById(R.id.personList);
	}

	@Override
	protected ListAdapter getAdapter() {
		try {
			return new PersonListAdapter(this);
		} catch (SQLException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected int getContentViewId() {
		return R.layout.person_list;
	}
}