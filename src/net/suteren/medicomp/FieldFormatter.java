package net.suteren.medicomp;

import java.sql.SQLException;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;

import net.suteren.medicomp.domain.record.Field;
import net.suteren.medicomp.enums.Type;

public class FieldFormatter {

	@SuppressWarnings("rawtypes")
	private Field field;

	public FieldFormatter(Field<?> field) {
		this.field = field;
	}

	@SuppressWarnings("unchecked")
	public void setValue(String value) throws ParseException, SQLException {
		Type type = field.getType();
		Format formatter = getFormatter(type);

		switch (type) {
		case TEMPERATURE:
			field.setValue(((Number) formatter.parseObject(value))
					.doubleValue());
			break;

		default:
			field.setValue(value);
			break;
		}
	}

	private Format getFormatter(Type type) {
		switch (type) {
		case TEMPERATURE:
			return NumberFormat.getInstance();
		default:
			return null;
		}
	}

	public String getValue() {
		Type type = field.getType();
		Format formatter = getFormatter(type);
		switch (type) {
		case TEMPERATURE:
			return formatter.format(field.getValue());
		default:
			return field.getValue().toString();
		}
	}

}
