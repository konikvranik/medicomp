package net.suteren.medicomp.ui.adapter;

import java.util.Calendar;
import java.util.Locale;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.Person.Gender;
import net.suteren.medicomp.ui.activity.PersonProfileActivity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PersonProfileAdapter extends ProfileAdapter<Person> {

	private LayoutInflater layoutInflater;
	private PersonProfileActivity profileActivity;
	private Person person;
	private Calendar ts = Calendar.getInstance(Locale.getDefault());
	private CharSequence nameEditText;
	private Gender gender;

	public PersonProfileAdapter(PersonProfileActivity context, Person person) {
		if (context == null)
			throw new NullPointerException("Context == null");
		if (person == null)
			throw new NullPointerException("Person == null!");
		this.profileActivity = context;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.person = person;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		switch (position) {
		case 0:
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.name_edit,
						parent, false);
				EditText nameEditText = (EditText) convertView
						.findViewById(R.id.editText1);
				nameEditText.setText(person.getName());
				nameEditText
						.setOnFocusChangeListener(new View.OnFocusChangeListener() {
							@Override
							public void onFocusChange(View view, boolean flag) {
								PersonProfileAdapter.this.nameEditText = ((EditText) view)
										.getText();

							}
						});
			}
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
								ts.set(Calendar.YEAR, year);
								ts.set(Calendar.MONTH, monthOfYear);
								ts.set(Calendar.DAY_OF_MONTH, dayOfMonth);
							}
						});
			}
			break;

		case 2:
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.gender_edit,
						parent, false);
				Spinner gender = (Spinner) convertView
						.findViewById(R.id.spinner1);
				ArrayAdapter<Gender> genderAdapter = new ArrayAdapter<Gender>(
						profileActivity,
						android.R.layout.simple_dropdown_item_1line,
						Gender.values());
				gender.setAdapter(genderAdapter);
				gender.setSelection(genderAdapter.getPosition(person
						.getGender()));
				gender.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						PersonProfileAdapter.this.gender = Gender.values()[arg2];

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						PersonProfileAdapter.this.gender = null;
					}
				});
			}
			break;

		default:
			if (convertView == null)
				convertView = new View(profileActivity);
		}
		return convertView;
	}

	public Calendar getBirthday() {
		return ts;
	}

	public String getName() {
		if (nameEditText == null)
			return null;
		return nameEditText.toString();
	}

	public Gender getGender() {
		return gender;
	}
}
