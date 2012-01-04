package net.suteren.medicomp.domain;

import com.j256.ormlite.field.DatabaseField;

public class StringValue implements Value<String> {

	public static final String COLUMN_NAME_VALUE = "value";

	public static final String COLUMN_NAME_FIELD = "field";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_VALUE)
	private String value;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_FIELD, foreign = true)
	private Field<String> field;

	public StringValue() {
	}

	public StringValue(Field<String> field, String value) {
		setValue(value);
		setField(field);
	}

	@Override
	public Field<String> getField() {
		return field;
	}

	@Override
	public void setField(Field<String> field) {
		this.field = field;
	}

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

}
