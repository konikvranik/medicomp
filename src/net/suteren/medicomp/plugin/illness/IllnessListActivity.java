package net.suteren.medicomp.plugin.illness;

import java.sql.SQLException;

import net.suteren.medicomp.ui.activity.RecordListActivity;
import net.suteren.medicomp.ui.adapter.RecordListAdapter;

public class IllnessListActivity extends RecordListActivity {

	@Override
	protected RecordListAdapter getAdapter() {
		try {
			return new IllnessListAdapter(this, person);
		} catch (SQLException e) {
			return null;
		}
	}
}
