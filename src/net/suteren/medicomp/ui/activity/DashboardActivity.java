package net.suteren.medicomp.ui.activity;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.ui.adapter.DashboardAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class DashboardActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (!setupPerson()) {
			redirectToPersonList();
			return;
		}

	}

	@Override
	protected DashboardAdapter getAdapter() {
		try {
			Log.d(LOG_TAG, "Calling getAdapter: " + person.getId() + ", "
					+ person.getName());
			return new DashboardAdapter(DashboardActivity.this, person);
		} catch (SQLException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected ListView requestListView() {
		return (ListView) getWindow().findViewById(R.id.dashboard);
	}

	@Override
	protected int getContentViewId() {
		return R.layout.dashboard;
	}

	@Override
	protected void edit(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void delete(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onItemClick(View view, int position, int id) {
		Log.d(LOG_TAG, "clicked dashboard " + id);
		return getAdapter().getItemById((int) id).onClick(view, position, id);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.dashboard_contextmenu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.preferences:
			return getAdapter().getItemById((int) info.id)
					.showPreferencesPane();
		default:
			return super.onContextItemSelected(item);
		}
	}
}
