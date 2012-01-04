package net.suteren.medicomp.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "types")
public class Type {
	private static final String COLUMN_NAME_NAME = "name";

	private static final String COLUMN_NAME_DESCRIPTION = "desc";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_NAME)
	private String name;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_DESCRIPTION)
	private String description;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
