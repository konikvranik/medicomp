package net.suteren.medicomp.ui.adapter;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

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
	private EditText name;
	private DatePicker dp;
	private TimePicker tp;
	private Calendar ts = Calendar.getInstance(Locale.getDefault());
	private Map<Field, View> fieldMap = new HashMap<Field, View>();

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
				name = (EditText) convertView.findViewById(R.id.editText1);

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
				name.setText(record.getTitle());
			}
			name = (EditText) convertView.findViewById(R.id.editText1);
			break;

		default:
			Field<?> field = fields.get(position - 1);
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.record_field,
						parent, false);

				String fieldName = field.getName();
				Object fieldValue = field.getValue();
				Type type = field.getType();

				TextView v = (TextView) convertView.findViewById(R.id.name);
				EditText nameView = (EditText) v;
				v = (TextView) convertView.findViewById(R.id.type);
				TextView typeView = (TextView) v;
				v = (TextView) convertView.findViewById(R.id.value);
				EditText valueView = (EditText) v;
				nameView.setText(fieldName);
				typeView.setText(type.toString());
				valueView.setText(fieldValue.toString());
			}
			fieldMap.put(field, convertView);
			break;

		}
		return convertView;
	}

	public String getTitle() {
		if (name == null)
			return null;
		return name.getEditableText().toString();
	}

	public Calendar getTimestamp() {
		ts.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
		ts.set(Calendar.MINUTE, tp.getCurrentMinute());
		return ts;
	}

	public void saveFields() throws SQLException, ParseException {
		for (Entry<Field, View> element : fieldMap.entrySet()) {
			Field field = element.getKey();
			View convertView = element.getValue();

			EditText nameView = (EditText) convertView
					.findViewById(R.id.name);
			field.setName(nameView.getEditableText().toString());

			EditText valueView = (EditText) convertView
					.findViewById(R.id.value);

			NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
			field.setValue(nf.parse(valueView.getEditableText().toString())
					.doubleValue());
		}
	}
}
