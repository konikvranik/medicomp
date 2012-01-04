package net.suteren.medicomp.domain;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import net.suteren.medicomp.dao.MediCompDatabaseFactory;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "fields")
public class Field<T> {

	private static final String COLUMN_NAME_NAME = "name";

	private static final String COLUMN_NAME_TYPE = "type";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_NAME)
	private String name;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_TYPE, foreign = true)
	private Type type;

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

	public T getValue() throws SQLException {
		if (this.value == null) {
			createValue(null);
		}
		if (value == null)
			return null;
		return value.getValue();
	}

	public void setValue(T value) throws SQLException {
		if (this.value == null) {
			createValue(value);
		} else if (value == null)
			return;
		this.value.setValue(value);
	}

	@SuppressWarnings("unchecked")
	private void createValue(T value) throws SQLException {
		MediCompDatabaseFactory dbf = MediCompDatabaseFactory.getInstance();
		if (value instanceof String) {
			Dao<StringValue, Integer> dao = (Dao<StringValue, Integer>) dbf
					.createDao(StringValue.class);
			List<StringValue> list = dao.queryForEq(
					StringValue.COLUMN_NAME_FIELD, this);
			if (list.size() > 0) {
				this.value = (Value<T>) list.get(0);
			} else if (value != null) {
				StringValue sValue = new StringValue((Field<String>) this,
						(String) value);
				dao.create(sValue);
				this.value = (Value<T>) sValue;
			}
		} else if (value instanceof Integer) {
			Dao<IntegerValue, Integer> dao = (Dao<IntegerValue, Integer>) dbf
					.createDao(IntegerValue.class);
			List<IntegerValue> list = dao.queryForEq(
					IntegerValue.COLUMN_NAME_FIELD, this);
			if (list.size() > 0) {
				this.value = (Value<T>) list.get(0);
			} else if (value != null) {
				IntegerValue sValue = new IntegerValue((Field<Integer>) this,
						(Integer) value);
				dao.create(sValue);
				this.value = (Value<T>) sValue;
			}
		} else if (value instanceof Double) {
			Dao<DoubleValue, Integer> dao = (Dao<DoubleValue, Integer>) dbf
					.createDao(DoubleValue.class);
			List<DoubleValue> list = dao.queryForEq(
					DoubleValue.COLUMN_NAME_FIELD, this);
			if (list.size() > 0) {
				this.value = (Value<T>) list.get(0);
			} else if (value != null) {
				DoubleValue sValue = new DoubleValue((Field<Double>) this,
						(Double) value);
				dao.create(sValue);
				this.value = (Value<T>) sValue;
			}
		} else if (value instanceof Date) {
			Dao<DateValue, Integer> dao = (Dao<DateValue, Integer>) dbf
					.createDao(DateValue.class);
			List<DateValue> list = dao.queryForEq(DateValue.COLUMN_NAME_FIELD,
					this);
			if (list.size() > 0) {
				this.value = (Value<T>) list.get(0);
			} else if (value != null) {
				DateValue sValue = new DateValue((Field<Date>) this,
						(Date) value);
				dao.create(sValue);
				this.value = (Value<T>) sValue;
			}
		} else {
			throw new UnsupportedOperationException("Type "
					+ value.getClass().getName() + " not supported!");
		}
	}
}
