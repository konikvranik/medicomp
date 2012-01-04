package net.suteren.medicomp.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "insurance")
public class Insurance {
	public static final String INSURANCE_TABLE_NAME = "insurance";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false, foreign = true)
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
