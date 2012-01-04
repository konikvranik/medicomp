package net.suteren.medicomp.domain;

import com.j256.ormlite.field.DatabaseField;

public class DoubleValue implements Value<Double> {

	private static final String COLUMN_NAME_VALUE = "value";

	static final String COLUMN_NAME_FIELD = "field";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_VALUE)
	private Double value;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_FIELD, foreign = true)
	private Field<Double> field;

	
	public DoubleValue() {
	}
	
	public DoubleValue(Field<Double> field,Double value2) {
		setValue(value2);
		setField(field);
	}

	@Override
	public Field<Double> getField() {
		return field;
	}

	@Override
	public void setField(Field<Double> field) {
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
	public Double getValue() {
		return value;
	}

	@Override
	public void setValue(Double value) {
		this.value = value;
	}

}
