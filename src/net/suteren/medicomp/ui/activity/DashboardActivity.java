package net.suteren.medicomp.ui.activity;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Category;
import net.suteren.medicomp.domain.Field;
import net.suteren.medicomp.domain.Record;
import net.suteren.medicomp.domain.Type;
import net.suteren.medicomp.ui.adapter.DashboardAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public class DashboardActivity extends MedicompActivity {

	private static final int TYPE_CHOOSER_DIALOG = 1;
	private Type choosedType;
	private ArrayList<Type> availableTypes;
	private Dao<Record, Integer> recordDao;
	@SuppressWarnings("rawtypes")
	private Dao<Field, Integer> fieldDao;
	private EditText inputTextField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		try {
			recordDao = dbf.createDao(Record.class);
			fieldDao = dbf.createDao(Field.class);
		} catch (SQLException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			throw new RuntimeException(e);
		}

		if (!setupPerson()) {
			redirectToPersonList();
			return;
		}

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

	@Override
	protected ListAdapter getAdapter() {
		try {
			Log.d(LOG_TAG, "Calling getAdapter: " + person.getId() + ", "
					+ person.getName());
			return new DashboardAdapter(DashboardActivity.this, person);
		} catch (SQLException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected ListView requestListView() {
		return (ListView) getWindow().findViewById(R.id.dashboard);
	}

	@Override
	protected int getContentViewId() {
		return R.layout.dashboard;
	}
}
