package net.suteren.medicomp.domain;

import com.j256.ormlite.field.DatabaseField;

public class Alergie {

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false, foreign = true)
	private Person person;

}
