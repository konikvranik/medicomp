package net.suteren.medicomp.domain;

import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

public enum Type {
	TEMPERATURE(Double.class, NumberFormat.getInstance(Locale.getDefault()));

	private Class<?> clazz;
	private Format format;

	Type(Class<?> cls, Format format) {
		this.clazz = cls;
		this.format = format;
	}

	public Class<?> getTypeClass() {
		return clazz;
	}

	public Format getFormatter() {
		return format;
	}

}
