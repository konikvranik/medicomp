package net.suteren.medicomp.ui.activity;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.ui.adapter.PersonListAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PersonListActivity extends ListActivity {

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
	protected PersonListAdapter getAdapter() {
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

	@Override
	protected void edit(long id) {
		Log.d(LOG_TAG, "Edit " + id);
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, PersonProfileActivity.class);
		intent.putExtra(PERSON_ID_EXTRA, id);
		this.startActivity(intent);
	}

	@Override
	protected void delete(long id) {
		Log.d(LOG_TAG, "delete " + id);
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onItemClick(View view, long position, long id) {
		Editor prefs = this.getSharedPreferences(
				MedicompActivity.MEDICOMP_PREFS, Context.MODE_WORLD_WRITEABLE)
				.edit();
		prefs.putLong(PERSON_ID_EXTRA, id);
		prefs.commit();
		Intent intent = new Intent(this, DashboardActivity.class);
		intent.putExtra(PERSON_ID_EXTRA, id);
		this.startActivity(intent);
		return true;
	}
}