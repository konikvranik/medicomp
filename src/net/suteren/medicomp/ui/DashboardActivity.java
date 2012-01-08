package net.suteren.medicomp.ui;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Category;
import net.suteren.medicomp.domain.Field;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.Record;
import net.suteren.medicomp.domain.Type;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public class DashboardActivity extends MedicompActivity {

	private static final int TYPE_CHOOSER_DIALOG = 1;
	private Person person;
	private Type choosedType;
	private ArrayList<Type> availableTypes;
	private Dao<Person, Integer> personDao;
	private Dao<Record, Integer> recordDao;
	private Dao<Field, Integer> fieldDao;
	private ListView listView;
	private EditText inputTextField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MediCompDatabaseFactory.init(getApplicationContext());
		setContentView(R.layout.dashboard);
		listView = (ListView) getWindow().findViewById(R.id.dashboard);
		inputTextField = (EditText) getWindow().findViewById(R.id.editText1);
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

		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				processInput();
			}
		});

		setupPerson();

	}

	private void setupPerson() {
		try {
			int personId = 0;
			Intent intent = getIntent();
			Bundle extras = null;
			if (intent != null)
				extras = intent.getExtras();
			if (extras != null)
				personId = extras.getInt("person");
			if (personId == 0)
				personId = getSharedPreferences(MEDICOMP_PREFS,
						Context.MODE_WORLD_WRITEABLE).getInt(PERSON_ID, 0);
			if (personId == 0) {
				startActivity(new Intent(this, PersonListActivity.class));
				finish();
				return;
			}
			Log.d(MedicompActivity.LOG_TAG, "PersonId: " + personId);
			MediCompDatabaseFactory dbf = MediCompDatabaseFactory.getInstance();

			personDao = dbf.createDao(Person.class);
			recordDao = dbf.createDao(Record.class);
			fieldDao = dbf.createDao(Field.class);
			Person personQuery = new Person();
			personQuery.setId(personId);
			Log.d(MedicompActivity.LOG_TAG,
					"Person before: " + personQuery.getId());
			person = personDao.queryForSameId(personQuery);
			if (person == null) {
				startActivity(new Intent(this, PersonListActivity.class));
				finish();
				return;
			}
			Log.d(MedicompActivity.LOG_TAG, "Person after: " + person.getId()
					+ ", " + person.getName());

			listView.setAdapter(new DashboardAdapter(DashboardActivity.this,
					person));

		} catch (SQLException e) {
			Log.e(MedicompActivity.LOG_TAG, "Failed: ", e);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setupPerson();
		listView.invalidateViews();
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
			Toast.makeText(DashboardActivity.this,
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
					Toast.makeText(DashboardActivity.this,
							R.string.failedToAddTemperature, Toast.LENGTH_SHORT);
				}

				break;

			default:
				break;
			}
		inputTextField.setText("");
	}
}
