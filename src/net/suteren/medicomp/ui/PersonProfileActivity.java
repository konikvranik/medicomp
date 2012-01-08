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
		super.onCreate(savedInstanceState);
		int personId = getIntent().getExtras().getInt("person");
		Log.d(MedicompActivity.LOG_TAG, "PersonId: " + personId);
		MediCompDatabaseFactory.init(getApplicationContext());
		MediCompDatabaseFactory dbf = MediCompDatabaseFactory.getInstance();

		try {
			final Dao<Person, Integer> personDao = dbf.createDao(Person.class);
			Person personQuery = new Person();
			personQuery.setId(personId);
			Log.d(MedicompActivity.LOG_TAG, "Person before: " + personQuery.getName());
			person = personDao.queryForSameId(personQuery);
			Log.d(MedicompActivity.LOG_TAG, "Person after: " + person.getName());

			setContentView(R.layout.person_profile);
			ListView listView = (ListView) getWindow().findViewById(
					R.id.personProfile);
			Button okButton = (Button) getWindow().findViewById(R.id.button1);
			Button cancelButton = (Button) getWindow().findViewById(
					R.id.button2);

			okButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						personDao.createOrUpdate(person);
					} catch (SQLException e) {
						Log.e(MedicompActivity.LOG_TAG, "Failed: ", e);
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

			listView.setAdapter(new PersonProfileAdapter(
					PersonProfileActivity.this, person));
		} catch (Exception e) {
			Log.e(MedicompActivity.LOG_TAG, "Failed: ", e);
		}

	}
}
