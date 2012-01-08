package net.suteren.medicomp.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Person;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;

public class PersonListActivity extends MedicompActivity {
	private final class ChangeReceiver extends BroadcastReceiver {
		private final ListView listView;

		private ChangeReceiver(ListView listView) {
			this.listView = listView;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			listView.invalidateViews();
			Log.d(MedicompActivity.LOG_TAG, "Broadcast recieved.");
		}
	}

	public static final String PERSON_DATA_CHANGE_CATEGORY = "net.suteren.medicomp.PERSON";
	public static final String PERSON_DATA_CHANGE_ACTION = "net.suteren.medicomp.CHANGE";
	private ChangeReceiver changeReceiver;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createDemoData();

		setContentView(R.layout.person_list);
		final ListView listView = (ListView) getWindow().findViewById(
				R.id.personList);

		try {

			IntentFilter intFilter = new IntentFilter();
			intFilter.addCategory(PERSON_DATA_CHANGE_CATEGORY);
			intFilter.addAction(PERSON_DATA_CHANGE_ACTION);
			changeReceiver = new ChangeReceiver(listView);
			registerReceiver(changeReceiver, intFilter);

			listView.setAdapter(new PersonListAdapter(this));
			listView.setItemsCanFocus(false);

		} catch (Exception e) {
			Log.e(MedicompActivity.LOG_TAG, "Failed: ", e);
		}

	}

	private void createDemoData() {
		MediCompDatabaseFactory.init(getApplicationContext());

		MediCompDatabaseFactory dbf = MediCompDatabaseFactory.getInstance();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			Dao<Person, Integer> personDao = dbf.createDao(Person.class);

			Person person = new Person();
			person.setName("Daddy");
			person.setBirthDate(dateFormat.parse("1970-06-16"));

			personDao.create(person);

			person = new Person();
			person.setName("Baby");
			person.setBirthDate(dateFormat.parse("2011-06-30"));

			personDao.create(person);

		} catch (Exception e) {
			Log.e(MedicompActivity.LOG_TAG, "Failed:", e);
		}
	}

	@Override
	protected void onDestroy() {
		if (changeReceiver != null)
			unregisterReceiver(changeReceiver);
		super.onDestroy();
	}
}