package net.suteren.medicomp.plugin.temperature;

import java.sql.SQLException;
import java.util.Calendar;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Record;
import net.suteren.medicomp.ui.activity.RecordProfileActivity;
import net.suteren.medicomp.ui.adapter.RecordProfileAdapter;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;

public class TemperatureProfileActivity extends RecordProfileActivity {

	private Record record;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {

			super.onCreate(savedInstanceState);

			recordDao = dbf.createDao(Record.class);
			if (!setupRecord()) {
				record = new Record();
			}

			listView.setAdapter(getAdapter());

		} catch (SQLException e) {
			Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			throw new RuntimeException(e);
		}

	}

	@Override
	protected ListAdapter getAdapter() {
		if (record == null)
			return null;
		return new TemperatureProfileAdapter(this, person, record);
	}

	protected boolean setupRecord() {
		Record recordQuery = new Record();
		Integer recordId = determineRecordId();
		if (recordId == null)
			return false;
		record = setupObject(recordDao, recordId);
		return record != null;
	}

	protected Integer determineRecordId() {
		return determineId(RECORD_ID_EXTRA, true);
	}

	@Override
	protected void onCancelEvent(View v) {
		TemperatureProfileActivity.this.finish();
	}

	@Override
	protected void onSaveEvent(View v) {

		RecordProfileAdapter recordAdapter = (RecordProfileAdapter) listView
				.getAdapter();

		String title = recordAdapter.getTitle();
		if (title != null)
			record.setTitle(title);

		Calendar ts = recordAdapter.getTimestamp();
		if (ts != null)
			record.setTimestamp(ts.getTime());

		try {
			recordDao.createOrUpdate(record);
			recordAdapter.saveFields();
		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			Builder db = new AlertDialog.Builder(TemperatureProfileActivity.this);
			db.setMessage(R.string.personSaveFailed);
			AlertDialog ad = db.create();
			ad.show();
		}

		Intent intent = new Intent();
		intent.addCategory(RECORD_DATA_CHANGE_CATEGORY);
		intent.setAction(DATA_CHANGE_ACTION);
		// intent.setData(Uri.parse("context://" + person.getId()));
		sendBroadcast(intent);
		TemperatureProfileActivity.this.finish();
	}

	@Override
	protected void onDeleteEvent(View v) {
		try {
			recordDao.delete(record);
		} catch (SQLException e) {
			Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			Builder db = new AlertDialog.Builder(TemperatureProfileActivity.this);
			db.setMessage(R.string.personDeleteFailed);
			AlertDialog ad = db.create();
			ad.show();
		}
		TemperatureProfileActivity.this.finish();
	}

}
