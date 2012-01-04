package net.suteren.medicomp.domain;

public interface Value<T> {

	int getId();

	T getValue();

	void setValue(T value);

	Field<T> getField();

	void setField(Field<T> field);

}
