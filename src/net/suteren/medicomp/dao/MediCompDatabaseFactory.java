package net.suteren.medicomp.dao;

import java.sql.SQLException;

import android.content.Context;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

public class MediCompDatabaseFactory {

	private MediCompDatabaseHelper mediCompDatabaseHelper = null;
	private Context context = null;
	private static MediCompDatabaseFactory mineInstance = null;
	private ConnectionSource connectionSource = null;

	private MediCompDatabaseFactory(Context context) {
		if (context == null)
			throw new NullPointerException("Context can not be null");
		this.context = context;
	}

	public static void init(Context context) {
		getInstance(context);
	}

	public static MediCompDatabaseFactory getInstance(Context context) {
		if (mineInstance == null)
			mineInstance = new MediCompDatabaseFactory(context);
		return mineInstance;
	}

	public MediCompDatabaseHelper getHelper() {
		if (mediCompDatabaseHelper == null)
			mediCompDatabaseHelper = new MediCompDatabaseHelper(this);
		return mediCompDatabaseHelper;
	}

	public <T> Dao<T, Integer> createDao(Class<T> class1) throws SQLException {
		return DaoManager.createDao(getConnectionSource(), class1);
	}

	public ConnectionSource getConnectionSource() {
		if (connectionSource == null)
			connectionSource = new AndroidConnectionSource(getHelper());
		return connectionSource;
	}

	ConnectionSource getConnectionSource(MediCompDatabaseHelper helper) {
		if (connectionSource == null)
			connectionSource = new AndroidConnectionSource(helper);
		return connectionSource;
	}

	Context getContext() {
		return context;
	}

	public static MediCompDatabaseFactory getInstance() {
		if (mineInstance == null)
			throw new IllegalAccessError("Database not initialized yet!");
		return mineInstance;

	}

}
