package net.suteren.medicomp.plugin.temperature;

import java.sql.SQLException;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import net.suteren.medicomp.R;
import net.suteren.medicomp.ui.activity.RecordListActivity;
import net.suteren.medicomp.ui.activity.RecordProfileActivity;
import net.suteren.medicomp.ui.adapter.RecordListAdapter;

public class TemperatureListActivity extends RecordListActivity {

	@Override
	protected RecordListAdapter getAdapter() {
		try {
			return new TemperatureListAdapter(this, person);
		} catch (SQLException e) {
			return null;
		}
	}

	@Override
	protected int getContentViewId() {
		return R.layout.record_list;
	}

	@Override
	public void edit(int id) {
		Intent intent = new Intent(this, RecordProfileActivity.class);
		intent.putExtra(RECORD_ID_EXTRA, id);
		this.startActivity(intent);
	}

	@Override
	protected void delete(int id) {
		Log.d(this.getClass().getCanonicalName(), "delete " + id);
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onItemClick(View view, int position, int id) {
		// TODO Auto-generated method stub
		return true;
	}

}
