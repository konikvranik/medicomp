package net.suteren.medicomp.domain.record;

import java.util.Collection;
import java.util.Date;

import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.WithId;
import net.suteren.medicomp.enums.Category;
import net.suteren.medicomp.enums.Type;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "records")
public class Record implements WithId {

	public static final String _ID = "id";

	public static final String COLUMN_NAME_TITLE = "title";

	public static final String COLUMN_NAME_TIMESTAMP = "timestamp";

	public static final String COLUMN_NAME_TYPE = "type";

	public static final String COLUMN_NAME_CATEGORY = "category";

	public static final String COLUMN_NAME_PARENT = "parent";

	public static final String COLUMN_NAME_PERSON = "person";

	@DatabaseField(generatedId = true, columnName = _ID)
	private int id;

	@DatabaseField(canBeNull = true, columnName = COLUMN_NAME_PERSON, foreign = true)
	private Person person;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_TITLE)
	private String title;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_TIMESTAMP, dataType = DataType.DATE_STRING, format = "yyyy-MM-dd HH:mm:ss")
	private Date timestamp;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_TYPE)
	private Type type;

	@DatabaseField(canBeNull = false, columnName = COLUMN_NAME_CATEGORY)
	private Category category;

	@DatabaseField(canBeNull = true, columnName = COLUMN_NAME_PARENT, foreign = true)
	private Record parent;

	@ForeignCollectionField(eager = false)
	private Collection<Field> fields;

	public Record() {
		setTimestamp(new Date());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Record getParent() {
		return parent;
	}

	public void setParent(Record parent) {
		this.parent = parent;
	}

	public Collection<Field> getFields() {
		return fields;
	}

	public void setFields(Collection<Field> fields) {
		this.fields = fields;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}
