package net.suteren.medicomp.ui;

import java.sql.SQLException;
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
import android.view.MenuItem;
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
		}
	}

	public static final String PERSON_DATA_CHANGE_CATEGORY = "net.suteren.medicomp.PERSON";
	public static final String PERSON_DATA_CHANGE_ACTION = "net.suteren.medicomp.CHANGE";
	private ChangeReceiver changeReceiver;
	private ListView listView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.person_list);
		listView = (ListView) getWindow().findViewById(R.id.personList);

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

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		try {
			listView.setAdapter(new PersonListAdapter(this));
		} catch (SQLException e) {
			Log.e(MedicompActivity.LOG_TAG, e.getMessage(), e);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		try {
			listView.setAdapter(new PersonListAdapter(this));
		} catch (SQLException e) {
			Log.e(MedicompActivity.LOG_TAG, e.getMessage(), e);
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		try {
			listView.setAdapter(new PersonListAdapter(this));
		} catch (SQLException e) {
			Log.e(MedicompActivity.LOG_TAG, e.getMessage(), e);
		}
	}

	@Override
	protected void onDestroy() {
		if (changeReceiver != null)
			unregisterReceiver(changeReceiver);
		super.onDestroy();
	}
}