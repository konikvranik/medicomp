package net.suteren.medicomp.plugin.person;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.ui.activity.DashboardActivity;
import net.suteren.medicomp.ui.activity.ListActivity;
import net.suteren.medicomp.ui.activity.MedicompActivity;
import net.suteren.medicomp.ui.adapter.PersonListAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
			Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected int getContentViewId() {
		return R.layout.person_list;
	}

	@Override
	protected void edit(int id) {
		Intent intent = new Intent(this, PersonProfileActivity.class);
		intent.putExtra(PERSON_ID_EXTRA, id);
		this.startActivity(intent);
	}

	@Override
	protected void delete(int id) {
		Log.d(this.getClass().getCanonicalName(), "delete " + id);
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onItemClick(View view, int position, int id) {
		Editor prefs = this.getSharedPreferences(
				MedicompActivity.MEDICOMP_PREFS, Context.MODE_WORLD_WRITEABLE)
				.edit();
		prefs.putInt(PERSON_ID_EXTRA, id);
		prefs.commit();
		Intent intent = new Intent(this, DashboardActivity.class);
		intent.putExtra(PERSON_ID_EXTRA, id);
		this.startActivity(intent);
		return true;
	}
}