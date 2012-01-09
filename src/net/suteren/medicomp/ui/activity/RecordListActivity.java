package net.suteren.medicomp.ui.activity;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Field;
import net.suteren.medicomp.domain.Record;
import net.suteren.medicomp.ui.adapter.RecordListAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;

public class RecordListActivity extends MedicompActivity {

	private Dao<Record, Integer> recordDao;
	@SuppressWarnings("rawtypes")
	private Dao<Field, Integer> fieldDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			recordDao = dbf.createDao(Record.class);
			fieldDao = dbf.createDao(Field.class);
		} catch (SQLException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			throw new RuntimeException(e);
		}

		if (!setupPerson()) {
			redirectToPersonList();
			return;
		}

	}

	@Override
	protected ListView requestListView() {
		return (ListView) getWindow().findViewById(R.id.recordList);
	}

	@Override
	protected ListAdapter getAdapter() {
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
}
