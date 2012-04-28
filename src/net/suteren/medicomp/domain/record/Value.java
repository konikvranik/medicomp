package net.suteren.medicomp.domain.record;

public interface Value<T> {

	int getId();

	T getValue();

	void setValue(T value);

	Field<T> getField();

	void setField(Field<T> field);

}
