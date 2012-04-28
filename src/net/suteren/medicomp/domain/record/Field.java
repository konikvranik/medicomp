package net.suteren.medicomp.domain.record;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.enums.Type;
import net.suteren.medicomp.enums.Unit;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "fields")
public class Field<T> {

	private static final String COLUMN_NAME_NAME = "name";

	private static final String COLUMN_NAME_TYPE = "type";

	private static final String COLUMN_NAME_UNIT = "unit";

	private static final String COLUMN_NAME_RECORD = "record";

	private static final String _ID = "id";

	private Dao<StringValue, Integer> stringValueDao;
	private Dao<DateValue, Integer> dateValueDao;
	private Dao<DoubleValue, Integer> doubleValueDao;
	private Dao<IntegerValue, Integer> integerValueDao;
	@SuppressWarnings("rawtypes")
	private Dao<Field, Integer> fieldValueDao;

	public Field() throws SQLException {
		stringValueDao = MediCompDatabaseFactory.getInstance().createDao(
				StringValue.class);
		integerValueDao = MediCompDatabaseFactory.getInstance().createDao(
				IntegerValue.class);
		doubleValueDao = MediCompDatabaseFactory.getInstance().createDao(
				DoubleValue.class);
		dateValueDao = MediCompDatabaseFactory.getInstance().createDao(
				DateValue.class);
		fieldValueDao = MediCompDatabaseFactory.getInstance().createDao(
				Field.class);
	}

	@DatabaseField(generatedId = true, columnName = _ID)
	private int id;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_NAME)
	private String name;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_TYPE)
	private Type type;

	@DatabaseField(canBeNull = true, columnName = COLUMN_NAME_UNIT)
	private Unit unit;

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
		}
		this.value.setField(this);
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

	@SuppressWarnings("rawtypes")
	public void setRecord(Record record) {
		Collection<Field> fields = record.getFields();
		if (fields == null)
			record.setFields(fields = new HashSet<Field>());
		fields.add(this);
		this.record = record;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	protected DateValue getDateValue() {
		return dateValue;
	}

	@SuppressWarnings("unchecked")
	protected void setDateValue(DateValue value) throws SQLException {
		value.setField((Field<Date>) this);
		this.dateValue = value;
	}

	protected IntegerValue getIntegerValue() {
		return integerValue;
	}

	@SuppressWarnings("unchecked")
	protected void setIntegerValue(IntegerValue value) throws SQLException {
		value.setField((Field<Integer>) this);
		this.integerValue = value;
	}

	protected DoubleValue getDoubleValue() {
		return doubleValue;
	}

	@SuppressWarnings("unchecked")
	protected void setDoubleValue(DoubleValue value) throws SQLException {
		value.setField((Field<Double>) this);
		this.doubleValue = value;
	}

	protected StringValue getStringValue() {
		return stringValue;

	}

	@SuppressWarnings("unchecked")
	protected void setStringValue(StringValue value) throws SQLException {
		value.setField((Field<String>) this);
		this.stringValue = value;
	}

	public void persist() throws SQLException {
		fieldValueDao.createOrUpdate(this);
		CreateOrUpdateStatus st = null;
		if (this.stringValue != null)
			st = stringValueDao.createOrUpdate(this.stringValue);
		if (this.integerValue != null)
			st = integerValueDao.createOrUpdate(this.integerValue);
		if (this.dateValue != null)
			st = dateValueDao.createOrUpdate(this.dateValue);
		if (this.doubleValue != null)
			st = doubleValueDao.createOrUpdate(this.doubleValue);
		if (st != null && st.isCreated())
			fieldValueDao.createOrUpdate(this);
	}

	@Override
	protected void finalize() throws Throwable {

		fieldValueDao.closeLastIterator();
		fieldValueDao.clearObjectCache();

		stringValueDao.closeLastIterator();
		stringValueDao.clearObjectCache();

		integerValueDao.closeLastIterator();
		integerValueDao.clearObjectCache();

		dateValueDao.closeLastIterator();
		dateValueDao.clearObjectCache();

		doubleValueDao.closeLastIterator();
		doubleValueDao.clearObjectCache();

		super.finalize();
	}

	@Override
	public String toString() {

		return "{id: " + getId() + ", name: '" + getName() + "', type: "
				+ getType() + ", recordId: " + getRecord().getId()
				+ ", value: " + getValue() + ", unit: " + getUnit() + " }";
	}

}
