package net.suteren.medicomp.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "alergie")
public class Alergie {

	private static final String COLUMN_NAME_PERSON = "person";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(columnName = COLUMN_NAME_PERSON, foreign = true)
	private Person person;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}
