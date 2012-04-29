package net.suteren.medicomp.domain;

import java.util.Collection;
import java.util.Date;

import net.suteren.medicomp.R;
import net.suteren.medicomp.SerialBitmap;
import net.suteren.medicomp.domain.record.Record;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "persons")
public class Person implements WithId {

	public static final String COLUMN_NAME_STATUTORY_INSURANCE = "statutory_insurance";
	public static final String _ID = "id";
	public static final String COLUMN_NAME_BIRTHDATE = "birthdate";
	public static final String COLUMN_NAME_PICTURE = "picture";
	public static final String COLUMN_NAME_NAME = "name";
	private static final String COLUMN_NAME_GENDER = "gender";
	public static final String PERSON_TABLE_NAME = "persons";

	@DatabaseField(generatedId = true, columnName = _ID)
	private int id;

	@DatabaseField(canBeNull = false, columnName = Person.COLUMN_NAME_NAME)
	private String name;

	@DatabaseField(canBeNull = true, dataType = DataType.SERIALIZABLE, columnName = Person.COLUMN_NAME_PICTURE)
	private SerialBitmap picture;

	@DatabaseField(canBeNull = true, columnName = Person.COLUMN_NAME_BIRTHDATE, dataType = DataType.DATE_STRING, format = "yyyy-MM-dd")
	private Date birthDate;

	@DatabaseField(canBeNull = true, columnName = COLUMN_NAME_STATUTORY_INSURANCE, foreign = true)
	private Insurance statutoryInsurance;

	@ForeignCollectionField(eager = false)
	private Collection<Insurance> otherInsurances;

	@ForeignCollectionField(eager = false)
	private Collection<Alergie> alergies;

	@DatabaseField(canBeNull = true, columnName = Person.COLUMN_NAME_GENDER)
	private Gender gender;

	@ForeignCollectionField(eager = false)
	private Collection<Record> records;

	public enum Gender {
		MALE, FEMALE, OTHER;

		@Override
		public String toString() {
			String[] res = ApplicationContextHolder.getContext().getResources()
					.getStringArray(R.array.genders);

			return res[ordinal() + 1];
		};
	}

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

	public SerialBitmap getPicture() {
		return picture;
	}

	public void setPicture(SerialBitmap picture) {
		this.picture = picture;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Insurance getStatutoryInsurance() {
		return statutoryInsurance;
	}

	public void setStatutoryInsurance(Insurance statutoryInsurance) {
		this.statutoryInsurance = statutoryInsurance;
		this.statutoryInsurance.setPerson(this);
	}

	public Collection<Insurance> getOtherInsurances() {
		return otherInsurances;
	}

	public void setOtherInsurances(Collection<Insurance> otherInsurances) {
		this.otherInsurances = otherInsurances;
		if (this.otherInsurances != null)
			for (Insurance i : this.otherInsurances)
				i.setPerson(this);
	}

	public Collection<Alergie> getAlergies() {
		return alergies;
	}

	public void setAlergies(Collection<Alergie> alergies) {
		this.alergies = alergies;
		if (this.alergies != null)
			for (Alergie a : this.alergies)
				a.setPerson(this);
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Collection<Record> getRecords() {
		return records;
	}

	public void setRecords(Collection<Record> records) {
		this.records = records;
		if (this.records != null)
			for (Record r : this.records)
				r.setPerson(this);
	}

}
