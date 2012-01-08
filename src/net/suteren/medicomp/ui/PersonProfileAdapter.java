package net.suteren.medicomp.ui;

import java.util.Calendar;
import java.util.Locale;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.Person.Gender;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class PersonProfileAdapter implements ListAdapter {

	private LayoutInflater layoutInflater;
	private Context context;
	private Person person;

	public PersonProfileAdapter(Context context, Person person) {
		if (context == null)
			throw new NullPointerException("Context == null");
		if (person == null)
			throw new NullPointerException("Person == null!");
		this.context = context;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.person = person;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public Object getItem(int arg0) {
		return person;
	}

	@Override
	public long getItemId(int position) {
		return person.getId();
	}

	@Override
	public int getItemViewType(int position) {
		return Math.min(position, 3);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		switch (position) {
		case 0:
			if (convertView == null)
				convertView = layoutInflater.inflate(R.layout.name_edit,
						parent, false);
			EditText name = (EditText) convertView.findViewById(R.id.editText1);
			name.setText(person.getName());
			name.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					person.setName(((EditText) v).getText().toString());

				}
			});
			name.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					person.setName(v.getText().toString());
					return false;
				}
			});
			break;

		case 1:
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.birthday_edit,
						parent, false);

				DatePicker birthDay = (DatePicker) convertView
						.findViewById(R.id.datePicker1);
				Calendar birthDate = Calendar.getInstance(Locale.getDefault());
				if (person.getBirthDate() != null)
					birthDate.setTime(person.getBirthDate());
				birthDay.init(birthDate.get(Calendar.YEAR),
						birthDate.get(Calendar.MONTH),
						birthDate.get(Calendar.DAY_OF_MONTH),
						new OnDateChangedListener() {

							@Override
							public void onDateChanged(DatePicker view,
									int year, int monthOfYear, int dayOfMonth) {
								Calendar cal = Calendar.getInstance(Locale
										.getDefault());
								cal.set(Calendar.YEAR, year);
								cal.set(Calendar.MONTH, monthOfYear);
								cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
								person.setBirthDate(cal.getTime());
							}
						});
			}
			break;

		case 2:
			if (convertView == null)
				convertView = layoutInflater.inflate(R.layout.gender_edit,
						parent, false);
			Spinner gender = (Spinner) convertView.findViewById(R.id.spinner1);
			ArrayAdapter<Gender> genderAdapter = new ArrayAdapter<Gender>(
					context, android.R.layout.simple_dropdown_item_1line,
					Gender.values());
			gender.setAdapter(genderAdapter);
			gender.setSelection(genderAdapter.getPosition(person.getGender()));
			gender.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					person.setGender(Gender.values()[arg2]);

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					person.setGender(null);
				}

			});
			break;

		default:
			if (convertView == null)
				convertView = new View(context);
		}
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return person != null;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

}
