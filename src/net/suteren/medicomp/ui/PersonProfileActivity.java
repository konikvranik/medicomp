package net.suteren.medicomp.ui;

import static net.suteren.medicomp.PersonListActivity.LOG_TAG;
import static net.suteren.medicomp.PersonListActivity.PERSON_DATA_CHANGE_ACTION;
import static net.suteren.medicomp.PersonListActivity.PERSON_DATA_CHANGE_CATEGORY;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.ApplicationContextHolder;
import net.suteren.medicomp.domain.Person;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;

public class PersonProfileActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		ApplicationContextHolder.setContext(getApplicationContext());

		int personId = getIntent().getExtras().getInt("person");

		Log.d(LOG_TAG, "PersonId: " + personId);

		MediCompDatabaseFactory.init(getApplicationContext());

		MediCompDatabaseFactory dbf = MediCompDatabaseFactory.getInstance();

		try {
			final Dao<Person, Integer> personDao = dbf.createDao(Person.class);

			final Person personQuery = new Person();
			personQuery.setId(personId);

			Log.d(LOG_TAG, "Person before: " + personQuery.getName());

			final Person person = personDao.queryForSameId(personQuery);

			Log.d(LOG_TAG, "Person after: " + person.getName());

			if (person == null)
				return;

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
						Log.e(LOG_TAG, "Failed: ", e);
						Builder db = new AlertDialog.Builder(
								PersonProfileActivity.this);
						db.setMessage(R.string.personSaveFailed);
						AlertDialog ad = db.create();
						ad.show();
					}

					Intent intent = new Intent();
					intent.addCategory(PERSON_DATA_CHANGE_CATEGORY);
					intent.setAction(PERSON_DATA_CHANGE_ACTION);
//					intent.setData(Uri.parse("context://" + person.getId()));

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

			try {
				listView.setAdapter(new PersonProfileAdapter(
						PersonProfileActivity.this, person));
			} catch (Exception e) {
				Log.e(LOG_TAG, "Failed: ", e);
			}

		} catch (SQLException e1) {
			Log.e(LOG_TAG, "Failed: ", e1);
		}
	}
}
