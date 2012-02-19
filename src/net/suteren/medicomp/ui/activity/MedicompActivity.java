package net.suteren.medicomp.ui.activity;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.ApplicationContextHolder;
import net.suteren.medicomp.domain.Category;
import net.suteren.medicomp.domain.Field;
import net.suteren.medicomp.domain.PersistableWithId;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.Record;
import net.suteren.medicomp.domain.Type;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public abstract class MedicompActivity extends Activity {

	private final class ChangeReceiver extends BroadcastReceiver {

		private ChangeReceiver() {
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			listView.invalidateViews();
		}
	}

	public static String LOG_TAG = "MEDICOMP";
	public static final String PERSON_ID_EXTRA = "personId";
	public static final String RECORD_ID_EXTRA = "recordId";
	public static final String MEDICOMP_PREFS = "medicomp_preferences";
	public static final String DATA_CHANGE_ACTION = "net.suteren.medicomp.CHANGE";
	public static final String PERSON_DATA_CHANGE_CATEGORY = "net.suteren.medicomp.PERSON";
	public static final String RECORD_DATA_CHANGE_CATEGORY = "net.suteren.medicomp.RECORD";
	protected Dao<Person, Integer> personDao;
	protected Person person;
	protected ListView listView;
	protected ChangeReceiver changeReceiver;
	protected Context context;
	protected MediCompDatabaseFactory dbf;
	protected Dao<Record, Integer> recordDao;
	@SuppressWarnings("rawtypes")
	protected Dao<Field, Integer> fieldDao;
	private EditText inputTextField;
	private Type choosedType;
	private ArrayList<Type> availableTypes;
	private static final int TYPE_CHOOSER_DIALOG = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Log.d(LOG_TAG, "Activity: " + this.getClass().getName());

		ApplicationContextHolder.setContext(this);
		MediCompDatabaseFactory.init(this);
		try {
			dbf = MediCompDatabaseFactory.getInstance();
			recordDao = dbf.createDao(Record.class);
			personDao = dbf.createDao(Person.class);
			fieldDao = dbf.createDao(Field.class);
		} catch (SQLException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			throw new RuntimeException(e);
		}

		super.onCreate(savedInstanceState);

		setContentView(getContentViewId());

		listView = requestListView();

		if (listView != null) {
			changeReceiver = new ChangeReceiver();
			IntentFilter intFilter = new IntentFilter();
			intFilter.addCategory(MedicompActivity.PERSON_DATA_CHANGE_CATEGORY);
			intFilter.addAction(MedicompActivity.DATA_CHANGE_ACTION);
			registerReceiver(changeReceiver, intFilter);
		}

		inputTextField = (EditText) getWindow().findViewById(R.id.editText1);
		if (inputTextField != null)
			inputTextField.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {

					if (event.getAction() == KeyEvent.ACTION_DOWN)
						switch (keyCode) {
						case KeyEvent.KEYCODE_ENTER:
							processInput();
							break;
						default:
							break;
						}
					return false;
				}
			});
		ImageButton ib = (ImageButton) getWindow().findViewById(
				R.id.imageButton1);
		if (ib != null)
			ib.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					processInput();
				}
			});

	}

	protected abstract int getContentViewId();

	protected ListView requestListView() {
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.choosePerson:
			startActivity(new Intent(this, PersonListActivity.class));
			break;

		case R.id.addPerson:
			Intent intent = new Intent(this, PersonProfileActivity.class);
			Editor prefs = getSharedPreferences(MEDICOMP_PREFS,
					Context.MODE_WORLD_WRITEABLE).edit();
			prefs.remove(PERSON_ID_EXTRA);
			prefs.commit();
			this.startActivity(intent);
			break;

		case R.id.showRecords:
			startActivity(new Intent(this, RecordListActivity.class));
			break;

		default:
			break;
		}

		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	protected <T extends PersistableWithId> T setupObject(
			Dao<T, Integer> objectDao, T object) {
		try {

			if (object == null) {
				return null;
			}

			Log.d(MedicompActivity.LOG_TAG, "Object before: " + object.getId());
			object = objectDao.queryForSameId(object);
			if (object != null)
				Log.d(MedicompActivity.LOG_TAG,
						"Object after: " + object.getId() + ", ");
			return object;
		} catch (SQLException e) {
			Log.e(MedicompActivity.LOG_TAG, "Failed: ", e);
			return null;
		}
	}

	protected boolean setupPerson() {
		Person personQuery = new Person();
		Integer personId = determinePersonId();
		if (personId == null)
			return false;
		personQuery.setId(personId);
		person = setupObject(personDao, personQuery);
		return person != null;
	}

	protected void redirectToPersonList() {
		startActivity(new Intent(this, PersonListActivity.class));
		finish();
	}

	protected Integer determinePersonId() {
		return determineId(PERSON_ID_EXTRA, true);
	}

	protected Integer determineId(String name, boolean cache) {
		Integer personId = null;
		Intent intent = getIntent();
		Bundle extras = null;
		if (intent != null)
			extras = intent.getExtras();
		if (extras != null)
			personId = extras.getInt(name);
		if (personId != null && personId > 0)
			return personId;

		if (cache)
			personId = getSharedPreferences(MEDICOMP_PREFS,
					Context.MODE_WORLD_WRITEABLE).getInt(PERSON_ID_EXTRA, 0);
		if (personId != 0)
			return personId;

		return null;

	}

	protected abstract ListAdapter getAdapter();

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		actualizeList();
	}

	@Override
	protected void onResume() {
		super.onResume();
		actualizeList();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		actualizeList();
	}

	@Override
	protected void onDestroy() {
		if (changeReceiver != null)
			unregisterReceiver(changeReceiver);
		super.onDestroy();
	}

	protected void actualizeList() {
		setupPerson();
		if (listView != null) {
			listView.setAdapter(getAdapter());
			listView.invalidateViews();
		}
	}

	private void processInput() {
		NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
		choosedType = null;
		availableTypes = new ArrayList<Type>();
		boolean isNumber = false;
		Number n = null;
		try {
			n = nf.parse(inputTextField.getText().toString());
			isNumber = true;
		} catch (ParseException e) {
			Log.d(LOG_TAG, "Not a number");
		}

		if (isNumber)
			availableTypes.add(Type.TEMPERATURE);

		Log.d(LOG_TAG, "Types: " + availableTypes.size());

		if (availableTypes.size() > 1) {
			showDialog(TYPE_CHOOSER_DIALOG);
		} else if (availableTypes.size() == 1) {
			choosedType = availableTypes.get(0);
		} else {
			Toast.makeText(MedicompActivity.this,
					getResources().getString(R.string.noAvailableType),
					Toast.LENGTH_SHORT).show();
		}

		if (choosedType != null)
			switch (choosedType) {
			case TEMPERATURE:

				try {

					Record r = new Record();
					r.setPerson(person);
					r.setTitle("temperature");
					r.setType(Type.TEMPERATURE);
					r.setCategory(Category.MEASURE);

					Field<Double> f = new Field<Double>();
					f.setType(Type.TEMPERATURE);
					f.setName("temperature");
					f.setRecord(r);

					recordDao.create(r);
					fieldDao.create(f);

					f.setValue(n.doubleValue());
					fieldDao.update(f);

					listView.invalidateViews();

				} catch (SQLException e) {
					Log.e(LOG_TAG, "Failed: ", e);
					Toast.makeText(MedicompActivity.this,
							R.string.failedToAddTemperature, Toast.LENGTH_SHORT);
				}

				break;

			default:
				break;
			}
		inputTextField.setText("");
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case TYPE_CHOOSER_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Pick a color");

			String[] strings = new String[availableTypes.size()];
			for (int i = 0; i < availableTypes.size(); i++) {
				strings[i] = availableTypes.get(i).toString();
			}

			builder.setItems(strings, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					choosedType = Type.values()[item];
				}
			});
			dialog = builder.create();
			break;
		default:
			super.onPrepareDialog(id, dialog);
		}
	}
}
