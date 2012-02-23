package net.suteren.medicomp.ui.adapter;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import net.suteren.medicomp.FieldFormatter;
import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Field;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.Record;
import net.suteren.medicomp.domain.Type;
import net.suteren.medicomp.ui.activity.RecordProfileActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class RecordProfileAdapter extends ProfileAdapter<Record> {

	private LayoutInflater layoutInflater;
	private RecordProfileActivity recordProfileActivity;
	private Record record;
	@SuppressWarnings("rawtypes")
	private ArrayList<Field> fields;
	private String name;
	private DatePicker dp;
	private TimePicker tp;
	private Calendar ts = Calendar.getInstance(Locale.getDefault());

	@SuppressWarnings("rawtypes")
	public RecordProfileAdapter(RecordProfileActivity context, Person person,
			Record record) {
		if (context == null)
			throw new NullPointerException("Context == null");
		if (record == null)
			throw new NullPointerException("Record == null!");
		// if (person == null)
		// throw new NullPointerException("Person == null!");
		this.recordProfileActivity = context;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.record = record;
		this.fields = new ArrayList<Field>(record.getFields());
	}

	@Override
	public int getCount() {
		return record.getFields().size() + 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		switch (position) {
		case 0:
			if (convertView == null) {
				convertView = layoutInflater.inflate(
						R.layout.record_profile_header, parent, false);
				EditText name = (EditText) convertView
						.findViewById(R.id.editText1);

				ts.setTime(record.getTimestamp());
				dp = (DatePicker) convertView.findViewById(R.id.datePicker1);
				tp = (TimePicker) convertView.findViewById(R.id.timePicker1);
				dp.init(ts.get(Calendar.YEAR), ts.get(Calendar.MONTH),
						ts.get(Calendar.DAY_OF_MONTH),
						new OnDateChangedListener() {
							@Override
							public void onDateChanged(DatePicker view,
									int year, int monthOfYear, int dayOfMonth) {
								ts.set(Calendar.YEAR, year);
								ts.set(Calendar.MONTH, monthOfYear);
								ts.set(Calendar.DAY_OF_MONTH, dayOfMonth);
							}
						});
				tp.setCurrentHour(ts.get(Calendar.HOUR_OF_DAY));
				tp.setCurrentMinute(ts.get(Calendar.MINUTE));
				tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

					@Override
					public void onTimeChanged(TimePicker view, int hourOfDay,
							int minute) {
						ts.set(Calendar.HOUR_OF_DAY, view.getCurrentHour());
						ts.set(Calendar.MINUTE, view.getCurrentMinute());
					}
				});
				name.setText(record.getTitle());
				name.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View view, boolean flag) {
						RecordProfileAdapter.this.name = ((EditText) view)
								.getText().toString();
					}
				});
			}
			break;

		default:
			final Field<?> field = fields.get(position - 1);
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.record_field,
						parent, false);

				String fieldName = field.getName();
				Object fieldValue = field.getValue();
				final Type type = field.getType();

				TextView v = (TextView) convertView.findViewById(R.id.name);
				EditText nameView = (EditText) v;
				v = (TextView) convertView.findViewById(R.id.type);
				TextView typeView = (TextView) v;
				v = (TextView) convertView.findViewById(R.id.value);
				EditText valueView = (EditText) v;
				nameView.setText(fieldName);
				nameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View view, boolean flag) {
						field.setName(((TextView) view).getText().toString());
					}
				});
				typeView.setText(type.toString());
				FieldFormatter f = new FieldFormatter(field);
				valueView.setText(f.getValue());
				valueView
						.setOnFocusChangeListener(new View.OnFocusChangeListener() {
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								FieldFormatter f = new FieldFormatter(field);
								try {
									f.setValue(((TextView) v).getText()
											.toString());
								} catch (ParseException e) {
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
							}
						});
			}
			break;

		}
		return convertView;
	}

	public String getTitle() {
		if (name == null)
			return null;
		return name;
	}

	public Calendar getTimestamp() {
		return ts;
	}

	public void saveFields() throws SQLException, ParseException {
		for (Field f : record.getFields()) {
			f.persist();
		}
	}
}
