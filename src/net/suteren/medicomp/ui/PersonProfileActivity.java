package net.suteren.medicomp.ui;

import static net.suteren.medicomp.ui.PersonListActivity.PERSON_DATA_CHANGE_ACTION;
import static net.suteren.medicomp.ui.PersonListActivity.PERSON_DATA_CHANGE_CATEGORY;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Person;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;

public class PersonProfileActivity extends MedicompActivity {
	private Person person;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);
			MediCompDatabaseFactory.init(getApplicationContext());
			MediCompDatabaseFactory dbf = MediCompDatabaseFactory.getInstance();
			final Dao<Person, Integer> personDao = dbf.createDao(Person.class);

			int personId = -1;
			if (getIntent().getExtras() != null)
				personId = getIntent().getExtras().getInt("person");
			if (personId > 0) {
				Person personQuery = new Person();
				personQuery.setId(personId);
				person = personDao.queryForSameId(personQuery);
			} else {
				person = new Person();
			}
			setContentView(R.layout.person_profile);
			ListView listView = (ListView) getWindow().findViewById(
					R.id.personProfile);
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
					intent.addCategory(PERSON_DATA_CHANGE_CATEGORY);
					intent.setAction(PERSON_DATA_CHANGE_ACTION);
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
}
