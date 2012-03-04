package net.suteren.medicomp.plugin.illness;

import java.sql.SQLException;

import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.ui.activity.RecordListActivity;
import net.suteren.medicomp.ui.adapter.RecordListAdapter;

public class IllnessListAdapter extends RecordListAdapter {

	public IllnessListAdapter(RecordListActivity recordListActivity,
			Person person) throws SQLException {
		super(recordListActivity, person);
	}

}
