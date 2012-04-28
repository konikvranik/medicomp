package net.suteren.medicomp.dao;

import java.sql.SQLException;
import java.util.List;

import net.suteren.medicomp.domain.Alergie;
import net.suteren.medicomp.domain.Insurance;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.record.DateValue;
import net.suteren.medicomp.domain.record.DoubleValue;
import net.suteren.medicomp.domain.record.Field;
import net.suteren.medicomp.domain.record.IntegerValue;
import net.suteren.medicomp.domain.record.Record;
import net.suteren.medicomp.domain.record.StringValue;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class MediCompDatabaseHelper extends SQLiteOpenHelper {

	static final int DB_VERSION = 6;
	static final String DB_NAME = "MEDICOMP";
	public static final String _ID = "id";
	public static final String LOG_TABLE_NAME = "logs";
	public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
	public static final String COLUMN_NAME_TYPE = "type";
	public static final String COLUMN_NAME_NUMBER = "numeric";
	public static final String COLUMN_NAME_TEXT = "textual";
	public static final String COLUMN_NAME_TITLE = "title";
	public static final String COLUMN_NAME_PERSON = "person";
	private MediCompDatabaseFactory dbf;
	private static boolean called = false;

	public MediCompDatabaseHelper(MediCompDatabaseFactory factory) {
		super(factory.getContext(), DB_NAME, null, DB_VERSION);
		dbf = factory;
		factory.getContext();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (called)
			return;
		called = true;
		try {

			createTable(db, Person.class);
			createTable(db, Insurance.class);
			createTable(db, Alergie.class);

			createTable(db, Record.class);
			createTable(db, Field.class);
			createTable(db, StringValue.class);
			createTable(db, IntegerValue.class);
			createTable(db, DateValue.class);
			createTable(db, DoubleValue.class);

		} catch (SQLException e) {
			Log.e(this.getClass().getCanonicalName(), "Failed: ", e);
		}

	}

	private void createTable(SQLiteDatabase db, Class<?> class1)
			throws SQLException {
		ConnectionSource cs = dbf.getConnectionSource(this);
		List<String> statements = TableUtils.getCreateTableStatements(cs,
				class1);
		for (String string : statements) {
			db.execSQL(string);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if (called)
			return;
		called = true;

		if (newVersion != DB_VERSION)
			throw new IllegalArgumentException(
					"DB version mismatch. Requested " + newVersion
							+ " but providing " + DB_VERSION);
		db.beginTransaction();
		try {
			switch (oldVersion) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				db.execSQL("alter table FIELDS rename to FIELDS_OLD");
				createTable(db, Field.class);
				db.execSQL("INSERT INTO FIELDS ("
						+ "dateValue_id, doubleValue_id, "
						+ "integerValue_id, name, "
						+ "record, stringValue_id, type, id " + ") SELECT "
						+ "dateValue_id, doubleValue_id, "
						+ "integerValue_id, name, "
						+ "record, stringValue_id, type, id "
						+ "FROM FIELDS_OLD");
				db.execSQL("drop table FIELDS_OLD");
			}

			db.setTransactionSuccessful();

		} catch (SQLException e) {
			Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
		} finally {
			db.endTransaction();
		}
	}

	public int getVersion() {
		return DB_VERSION;
	}
}
