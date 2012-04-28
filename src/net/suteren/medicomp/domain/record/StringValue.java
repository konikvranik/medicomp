package net.suteren.medicomp.domain.record;

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

	public Field<String> getField() {
		return field;
	}

	public void setField(Field<String> field) {
		this.field = field;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
