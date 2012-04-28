package net.suteren.medicomp.plugin.temperature;

import java.sql.SQLException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Locale;

import net.suteren.medicomp.domain.record.Record;
import net.suteren.medicomp.enums.Category;
import net.suteren.medicomp.enums.Quantity;
import net.suteren.medicomp.enums.Type;
import net.suteren.medicomp.enums.Unit;
import net.suteren.medicomp.format.RecordFormatter;
import net.suteren.medicomp.ui.activity.MedicompActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TemperatureFormatter extends Format implements RecordFormatter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4396349352747699509L;
	private Locale locale;
	private ParsePosition parsePosition = new ParsePosition(0);
	private Unit unit;
	private Context context;

	public TemperatureFormatter(Context context, Locale locale) {
		this.locale = locale;
		this.context = context;
	}

	@Override
	public StringBuffer format(Object obj, StringBuffer stringbuffer,
			FieldPosition fieldposition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double parseObject(String s, ParsePosition parseposition) {
		boolean parsingUnit = false;
		boolean decimal = false;
		double multiplier = .1;
		int start = parseposition.getIndex();
		int i;
		Double retval = 0d;
		OUTER: for (i = start; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (!parsingUnit) {
				if (ch >= '0' && ch <= '9') {
					int v = (ch - '0');
					if (decimal) {
						retval += multiplier * v;
						multiplier = multiplier * .1;
					} else {
						retval = retval * 10 + v;
					}
				} else if (!decimal && (ch == '.' || ch == ',')) {
					decimal = true;
				} else {
					if (i == start) {
						parseposition.setErrorIndex(i);
						return null;
					} else {
						parsingUnit = true;
					}
				}
			}
			if (parsingUnit) {
				if (ch == ' ')
					continue;
				String unitPart = s.substring(i);
				if (unitPart == null)
					break OUTER;
				unitPart = unitPart.trim();
				if ("".equals(unitPart))
					break OUTER;
				for (Unit unit : Quantity.TEMPERATURE.getUnits()) {
					if (unit == null)
						continue;
					if (unitPart.startsWith(unit.getUnit())) {
						this.unit = unit;
						parseposition.setIndex(s.length() < i
								+ unit.getUnit().length() ? s.length() : i
								+ unit.getUnit().length());
						break OUTER;
					}
					for (String unitFormat : unit.getAlternativeUnits()) {
						if (unitPart.startsWith(unitFormat)) {
							this.unit = unit;
							parseposition.setIndex(s.length() < i
									+ unitPart.length() ? s.length() : i
									+ unitPart.length());
							break OUTER;
						}
					}
				}
				parseposition.setErrorIndex(i);
			}
		}
		parseposition.setIndex(i + 1);
		return retval;
	}

	public Unit getUnit() {
		if (unit == null) {
			SharedPreferences prefs = context.getSharedPreferences(
					MedicompActivity.MEDICOMP_PREFS, Context.MODE_PRIVATE);
			unit = Unit.valueOf(prefs.getString("temperature_unit", "CELSIUS"));
		}
		return unit;
	}

	public Record parse(String input, boolean strict) throws ParseException {
		parsePosition = new ParsePosition(0);
		Double numericValue = parseObject(input, parsePosition);
		if (parsePosition.getErrorIndex() > -1 || (getUnit() == null && strict))
			throw new ParseException(input, parsePosition.getErrorIndex());

		Record r = new Record();
		try {
			r.setTitle("temperature");
			r.setType(Type.TEMPERATURE);
			r.setCategory(Category.MEASURE);
			r.setTimestamp(Calendar.getInstance(locale).getTime());
			net.suteren.medicomp.domain.record.Field<Double> f = new net.suteren.medicomp.domain.record.Field<Double>();
			f.setType(Type.TEMPERATURE);
			f.setName("temperature");
			f.setRecord(r);
			f.setUnit(getUnit());
			f.setValue(numericValue);
		} catch (SQLException e) {
			throw new ParseException(input, parsePosition.getErrorIndex());
		}
		Log.d(getClass().getCanonicalName(), "Record: " + r.toString());
		return r;

	}

	public Record parse(String input) throws ParseException {
		return parse(input, false);
	}

}
