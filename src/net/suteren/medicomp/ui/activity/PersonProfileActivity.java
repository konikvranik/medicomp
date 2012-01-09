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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PersonProfileActivity extends MedicompActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {

			super.onCreate(savedInstanceState);

			if (!setupPerson()) {
				person = new Person();
			}
			listView.setAdapter(getAdapter());

			Button okButton = (Button) getWindow().findViewById(R.id.button1);
			Button cancelButton = (Button) getWindow().findViewById(
					R.id.button2);
			Button deleteButton = (Button) getWindow().findViewById(
					R.id.button3);

			okButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						personDao.createOrUpdate(person);
					} catch (SQLException e) {
						Log.e(LOG_TAG, e.getMessage(), e);
						Builder db = new AlertDialog.Builder(
								PersonProfileActivity.this);
						db.setMessage(R.string.personSaveFailed);
						AlertDialog ad = db.create();
						ad.show();
					}

					Intent intent = new Intent();
					intent.addCategory(MedicompActivity.PERSON_DATA_CHANGE_CATEGORY);
					intent.setAction(MedicompActivity.PERSON_DATA_CHANGE_ACTION);
					// intent.setData(Uri.parse("context://" + person.getId()));

					sendBroadcast(intent);

					PersonProfileActivity.this.finish();

				}
			});

			cancelButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					PersonProfileActivity.this.finish();
				}
			});

			deleteButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						personDao.delete(person);
					} catch (SQLException e) {
						Log.e(LOG_TAG, e.getMessage(), e);
						Builder db = new AlertDialog.Builder(
								PersonProfileActivity.this);
						db.setMessage(R.string.personDeleteFailed);
						AlertDialog ad = db.create();
						ad.show();
					}
					PersonProfileActivity.this.finish();

				}
			});
			listView.setAdapter(new PersonProfileAdapter(
					PersonProfileActivity.this, person));
		} catch (Exception e) {
			Log.e(MedicompActivity.LOG_TAG, "Failed: ", e);
		}

	}

	@Override
	protected ListView requestListView() {
		return (ListView) getWindow().findViewById(R.id.personProfile);
	}

	@Override
	protected ListAdapter getAdapter() {
		return new PersonProfileAdapter(this, person);
	}

	@Override
	protected int getContentViewId() {
		return R.layout.person_profile;
	}
}
