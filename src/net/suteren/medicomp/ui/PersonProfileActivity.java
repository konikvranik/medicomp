package net.suteren.medicomp.ui;

import static net.suteren.medicomp.PersonListActivity.LOG_TAG;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Person;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;

public class PersonProfileActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		int personId = getIntent().getExtras().getInt("person");

		Log.d(LOG_TAG, "PersonId: " + personId);

		MediCompDatabaseFactory.init(getApplicationContext());

		MediCompDatabaseFactory dbf = MediCompDatabaseFactory.getInstance();

		try {
			Dao<Person, Integer> personDao = dbf.createDao(Person.class);

			Person person = new Person();
			person.setId(personId);

			Log.d(LOG_TAG, "Person before: " + person.getName());

			person = personDao.queryForSameId(person);

			Log.d(LOG_TAG, "Person after: " + person.getName());

			if (person == null)
				return;

			setContentView(R.layout.person_profile);
			ListView listView = (ListView) getWindow().findViewById(
					R.id.personProfile);

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					// TODO Auto-generated method stub

				}
			});

			listView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					arg0.getSelectedItem();
					// TODO Auto-generated method stub
					return false;
				}
			});

			try {
				listView.setAdapter(new PersonProfileAdapter(
						getApplicationContext(), person));
			} catch (Exception e) {
				Log.e(LOG_TAG, "Failed: ", e);
			}

		} catch (SQLException e1) {
			Log.e(LOG_TAG, "Failed: ", e1);
		}
	}
}
