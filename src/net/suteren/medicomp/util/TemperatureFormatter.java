package net.suteren.medicomp.util;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

import net.suteren.medicomp.enums.Quantity;
import net.suteren.medicomp.enums.Unit;

public class TemperatureFormatter extends Format {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4396349352747699509L;
	private Locale locale;
	private ParsePosition parsePosition = new ParsePosition(0);
	private Unit unit;

	public TemperatureFormatter(Locale locale) {
		this.locale = locale;
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
		return unit;
	}

	public Double parse(String input) throws ParseException {
		parsePosition = new ParsePosition(0);
		Double result = parseObject(input, parsePosition);

		if (parsePosition.getErrorIndex() > -1)
			throw new ParseException(input, parsePosition.getErrorIndex());
		return result;
	}
}
