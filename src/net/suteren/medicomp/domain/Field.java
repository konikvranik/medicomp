package net.suteren.medicomp.domain;

import java.sql.SQLException;
import java.util.Date;

import net.suteren.medicomp.dao.MediCompDatabaseFactory;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "fields")
public class Field<T> {

	private static final String COLUMN_NAME_NAME = "name";

	private static final String COLUMN_NAME_TYPE = "type";

	private static final String COLUMN_NAME_RECORD = "record";

	private static final String _ID = "id";

	@DatabaseField(generatedId = true, columnName = _ID)
	private int id;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_NAME)
	private String name;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_TYPE)
	private Type type;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_RECORD, foreign = true)
	private Record record;

	@DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true)
	private StringValue stringValue;

	@DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true)
	private DoubleValue doubleValue;

	@DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true)
	private IntegerValue integerValue;

	@DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true)
	private DateValue dateValue;

	private Value<T> value;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public T getValue() {
		if (this.value == null || this.value.getValue() == null)
			this.value = (Value<T>) getStringValue();
		if (this.value == null || this.value.getValue() == null)
			this.value = (Value<T>) getIntegerValue();
		if (this.value == null || this.value.getValue() == null)
			this.value = (Value<T>) getDoubleValue();
		if (this.value == null || this.value.getValue() == null)
			this.value = (Value<T>) getDateValue();
		if (value == null)
			return null;
		return value.getValue();
	}

	@SuppressWarnings("unchecked")
	public void setValue(T value) throws SQLException {
		if (value == null)
			return;
		if (this.value == null) {
			if (value instanceof String) {
				this.value = (Value<T>) new StringValue();
			} else if (value instanceof Integer) {
				this.value = (Value<T>) new IntegerValue();
			} else if (value instanceof Double) {
				this.value = (Value<T>) new DoubleValue();
			} else if (value instanceof Date) {
				this.value = (Value<T>) new DateValue();
			}
			this.value.setField(this);
		}
		this.value.setValue(value);
		if (value instanceof String) {
			setStringValue((StringValue) this.value);
		} else if (value instanceof Integer) {
			setIntegerValue((IntegerValue) this.value);
		} else if (value instanceof Double) {
			setDoubleValue((DoubleValue) this.value);
		} else if (value instanceof Date) {
			setDateValue((DateValue) this.value);
		}
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	protected DateValue getDateValue() {
		return dateValue;
	}

	protected void setDateValue(DateValue value) throws SQLException {
		this.dateValue = value;
		MediCompDatabaseFactory.getInstance().createDao(DateValue.class)
				.createOrUpdate(value);
	}

	protected IntegerValue getIntegerValue() {
		return integerValue;
	}

	protected void setIntegerValue(IntegerValue value) throws SQLException {
		this.integerValue = value;
		MediCompDatabaseFactory.getInstance().createDao(IntegerValue.class)
				.createOrUpdate(value);
	}

	protected DoubleValue getDoubleValue() {
		return doubleValue;
	}

	protected void setDoubleValue(DoubleValue value) throws SQLException {
		this.doubleValue = value;
		MediCompDatabaseFactory.getInstance().createDao(DoubleValue.class)
				.createOrUpdate(value);
	}

	protected StringValue getStringValue() {
		return stringValue;

	}

	protected void setStringValue(StringValue value) throws SQLException {
		this.stringValue = value;
		MediCompDatabaseFactory.getInstance().createDao(StringValue.class)
				.createOrUpdate(value);
	}
}
