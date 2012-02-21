package net.suteren.medicomp;

import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import net.suteren.medicomp.domain.Field;
import net.suteren.medicomp.domain.Type;

public class FieldFormatter {

	private Field field;

	public FieldFormatter(Field<?> field) {
		this.field = field;
	}

	private boolean isInstanceOf(Class a, Class b) {
		if (a == null || b == null)
			return false;
		if (b.equals(a))
			return true;
		else
			return isInstanceOf(a.getSuperclass(), b);
	}

	public void setValue(String value) throws ParseException, SQLException {
		Type type = field.getType();
		Class<?> clazz = type.getTypeClass();
		Format formatter = type.getFormatter();
		Object parsed = formatter.parseObject(value);
		Object inst;
		if (isInstanceOf(clazz, Double.class)) {
			if (parsed instanceof Number)
				field.setValue(((Number) parsed).doubleValue());
			else
				field.setValue(parsed);
		} else if (isInstanceOf(clazz, Integer.class)) {
			if (parsed instanceof Number)
				field.setValue(((Number) parsed).intValue());
			else
				field.setValue(parsed);
		} else if (isInstanceOf(clazz, Date.class)) {
			field.setValue(parsed);
		} else if (isInstanceOf(clazz, Calendar.class)) {
			field.setValue(((Calendar) parsed).getTime());
		} else if (isInstanceOf(clazz, String.class)) {
			field.setValue(parsed);
		}
	}

	public String getValue() {
		Type type = field.getType();
		Format formatter = type.getFormatter();
		return formatter.format(field.getValue());
	}

}
