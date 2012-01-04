package net.suteren.medicomp.domain;

import com.j256.ormlite.field.DatabaseField;

public class IntegerValue implements Value<Integer> {

	private static final String COLUMN_NAME_VALUE = "value";

	static final String COLUMN_NAME_FIELD = "field";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_VALUE)
	private Integer value;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_FIELD, foreign = true)
	private Field<Integer> field;

	public IntegerValue() {
	}

	public IntegerValue(Field<Integer> field, Integer value2) {
		setValue(value2);
		setField(field);
	}

	@Override
	public Field<Integer> getField() {
		return field;
	}

	@Override
	public void setField(Field<Integer> field) {
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
	public Integer getValue() {
		return value;
	}

	@Override
	public void setValue(Integer value) {
		this.value = value;
	}

}
