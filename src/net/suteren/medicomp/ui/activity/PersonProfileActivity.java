package net.suteren.medicomp.ui.activity;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.ui.adapter.PersonProfileAdapter;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;

public class PersonProfileActivity extends ProfileActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {

			super.onCreate(savedInstanceState);

			if (!setupPerson()) {
				person = new Person();
			}

		} catch (Exception e) {
			Log.e(MedicompActivity.LOG_TAG, "Failed: ", e);
		}

	}

	@Override
	protected ListAdapter getAdapter() {
		return new PersonProfileAdapter(this, person);
	}

	@Override
	protected void onCancelEvent(View v) {
		PersonProfileActivity.this.finish();
	}

	@Override
	protected void onSaveEvent(View v) {

		PersonProfileAdapter adapter = (PersonProfileAdapter) listView
				.getAdapter();
		person.setName(adapter.getName());
		person.setGender(adapter.getGender());
		person.setBirthDate(adapter.getBirthday().getTime());

		try {
			personDao.createOrUpdate(person);
		} catch (SQLException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			Builder db = new AlertDialog.Builder(PersonProfileActivity.this);
			db.setMessage(R.string.personSaveFailed);
			AlertDialog ad = db.create();
			ad.show();
		}

		Intent intent = new Intent();
		intent.addCategory(MedicompActivity.PERSON_DATA_CHANGE_CATEGORY);
		intent.setAction(MedicompActivity.DATA_CHANGE_ACTION);
		// intent.setData(Uri.parse("context://" + person.getId()));
		sendBroadcast(intent);
		PersonProfileActivity.this.finish();
	}

	@Override
	protected void onDeleteEvent(View v) {

		try {
			personDao.delete(person);
		} catch (SQLException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			Builder db = new AlertDialog.Builder(PersonProfileActivity.this);
			db.setMessage(R.string.personDeleteFailed);
			AlertDialog ad = db.create();
			ad.show();
		}
		PersonProfileActivity.this.finish();
	}
}
