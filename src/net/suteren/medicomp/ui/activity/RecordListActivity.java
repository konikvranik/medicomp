package net.suteren.medicomp.ui.activity;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.ui.adapter.RecordListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class RecordListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (!setupPerson()) {
			redirectToPersonList();
			return;
		}

		listView.setAdapter(getAdapter());

	}

	@Override
	protected ListView requestListView() {
		return (ListView) getWindow().findViewById(R.id.recordList);
	}

	@Override
	protected RecordListAdapter getAdapter() {
		try {
			return new RecordListAdapter(this, person);
		} catch (SQLException e) {
			Log.d(LOG_TAG, e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected int getContentViewId() {
		return R.layout.record_list;
	}

	@Override
	public void edit(long id) {
		Log.d(LOG_TAG, "Edit " + id);
		Intent intent = new Intent(this, RecordProfileActivity.class);
		intent.putExtra(RECORD_ID_EXTRA, (int) id);
		this.startActivity(intent);
	}

	@Override
	protected void delete(long id) {
		Log.d(LOG_TAG, "delete " + id);
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onItemClick(View view, long position, long id) {
		// TODO Auto-generated method stub
		return true;
	}

}
