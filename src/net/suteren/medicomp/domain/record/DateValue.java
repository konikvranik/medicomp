package net.suteren.medicomp.domain.record;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class DateValue implements Value<Date> {

	private static final String COLUMN_NAME_VALUE = "value";

	static final String COLUMN_NAME_FIELD = "field";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_VALUE, dataType = DataType.DATE, format = "yyyy-MM-DD")
	private Date value;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_FIELD, foreign = true)
	private Field<Date> field;

	public DateValue() {
	}

	public DateValue(Field<Date> field, Date value2) {
		setValue(value2);
		setField(field);
	}

	public Field<Date> getField() {
		return field;
	}

	public void setField(Field<Date> field) {
		this.field = field;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getValue() {
		return value;
	}

	public void setValue(Date value) {
		this.value = value;
	}

}
