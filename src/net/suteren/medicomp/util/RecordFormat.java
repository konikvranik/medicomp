package net.suteren.medicomp.util;

import java.sql.SQLException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.record.Record;
import net.suteren.medicomp.enums.Category;
import net.suteren.medicomp.enums.Type;
import net.suteren.medicomp.enums.Unit;
import net.suteren.medicomp.plugin.temperature.TemperatureFormatter;
import net.suteren.medicomp.ui.activity.MedicompActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

@Deprecated
public class RecordFormat extends Format {

	private static final long serialVersionUID = 8879817995238793613L;
	private Locale locale;
	private Person person;
	private Context context;
	private int errorOffset;

	public RecordFormat(Locale locale, Context context, Person person) {
		this.person = person;
		this.locale = locale;
		this.context = context;
	}

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo,
			FieldPosition pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		// TODO Auto-generated method stub
		return null;
	}

	public Record format(String text, Type type, boolean strict)
			throws ParseException {
		try {
			switch (type) {
			case TEMPERATURE:
				return formatTemperature(text, type);
			case DISEASE:
				return formatDisease(text, type, strict);
			case MEDICATION:
				return formatMedication(text, type, strict);
			default:
				throw new ParseException("Unsupported type " + type, 0);
			}
		} catch (SQLException e) {
			throw new ParseException(e.getMessage(), 0);
		}
	}

	private Record formatMedication(String text, Type type, boolean strict)
			throws ParseException {

		TemperatureFormatter tf = new TemperatureFormatter(context,
				Locale.getDefault());
		Double temp = tf.parseObject(text, new ParsePosition(0));

		// TODO Auto-generated method stub
		throw new ParseException("Unsupported type " + type, 0);
	}

	private Record formatDisease(String text, Type type, boolean strict)
			throws ParseException, SQLException {
		Record r = newRecord();
		r.setTitle(text);
		r.setType(Type.DISEASE);
		r.setCategory(Category.STATE);
		net.suteren.medicomp.domain.record.Field<Date> f;
		f = new net.suteren.medicomp.domain.record.Field<Date>();
		f.setType(Type.BEGIN);
		f.setName(text);
		f.setRecord(r);
		f.setValue(Calendar.getInstance(locale).getTime());
		return r;
	}

	private Record formatTemperature(String text, Type type)
			throws ParseException, SQLException {
		Record r = newRecord();
		r.setTitle("temperature");
		r.setType(Type.TEMPERATURE);
		r.setCategory(Category.MEASURE);
		r.setTimestamp(Calendar.getInstance(locale).getTime());
		net.suteren.medicomp.domain.record.Field<Double> f;
		f = new net.suteren.medicomp.domain.record.Field<Double>();
		f.setType(Type.TEMPERATURE);
		f.setName("temperature");
		f.setRecord(r);

		TemperatureFormatter formatter = new TemperatureFormatter(context,
				Locale.getDefault());
		Double value = formatter.parseObject(text, new ParsePosition(0));
		Unit unit = formatter.getUnit();

		Log.d(this.getClass().getCanonicalName(), "Unit: " + unit);
		if (unit == null) {
			SharedPreferences prefs = context.getSharedPreferences(
					MedicompActivity.MEDICOMP_PREFS, Context.MODE_PRIVATE);
			unit = Unit.valueOf(prefs.getString("temperature_unit", "CELSIUS"));
		}
		Log.d(this.getClass().getCanonicalName(), "Input: " + text + " value: "
				+ value + " unit: " + unit);

		f.setUnit(unit);
		f.setValue(value);

		return r;
	}

	private Record newRecord() {
		Record r = new Record();
		r.setPerson(person);
		return r;
	}
}
