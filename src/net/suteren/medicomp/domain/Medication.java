package net.suteren.medicomp.domain;

import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "medications")
public class Medication implements WithId {

	public static final String _ID = "id";

	private static final String COLUMN_NAME_INTERVAL = "interval";

	private static final String COLUMN_NAME_COUNT = "count";

	private static final String COLUMN_NAME_USAGE = "timeToUse";

	private static final String COLUMN_NAME_NAME = "name";

	private static final String COLUMN_NAME_DESCRIPTION = "description";

	private static final String COLUMN_NAME_CONTRAINDICATION = "contraindication";

	@DatabaseField(generatedId = true, columnName = _ID)
	private int id;

	@DatabaseField(canBeNull = true, columnName = COLUMN_NAME_INTERVAL)
	private Integer intervalMinutes;

	@DatabaseField(canBeNull = true, columnName = COLUMN_NAME_COUNT)
	private Integer count;

	@DatabaseField(canBeNull = true, columnName = COLUMN_NAME_USAGE)
	private Integer usageMinutes;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_NAME)
	private String name;

	@DatabaseField(canBeNull = true, columnName = COLUMN_NAME_DESCRIPTION)
	private String description;

	@ForeignCollectionField(columnName = COLUMN_NAME_CONTRAINDICATION, eager = false)
	private Collection<Contraindication> contraindication;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
