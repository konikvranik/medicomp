package net.suteren.medicomp.provider;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

public class CalendarListCursor implements Cursor {

	private static String[] colNames = { "account_name", "account_type",
			"calendar_color", "_sync_id", "dirty", "ownerAccount",
			"maxReminders", "allowedReminders", "allowedAvailability",
			"allowedAttendeeTypes", "canModifyTimeZone", "canOrganizerRespond",
			"canPartiallyUpdate", "calendar_location", "calendar_timezone",
			"calendar_access_level", "deleted", "cal_sync1", "cal_sync2",
			"cal_sync3", "cal_sync4", "cal_sync5", "cal_sync6", "cal_sync7",
			"cal_sync8", "cal_sync9", "cal_sync10" };

	@SuppressWarnings("unchecked")
	private static Map<String, String>[] calendars = new Map[1];
	static {
		calendars[0] = new HashMap<String, String>() {
			private static final long serialVersionUID = -9096293132647351139L;
			{
				put(colNames[0], "MediComp");// account_name
				put(colNames[1], "MediComp");// account_type
				put(colNames[2], "red");// calendar_color
				put(colNames[3], null);// _sync_id
				put(colNames[4], "true");// dirty
				put(colNames[5], "MediComp");// ownerAccount
				put(colNames[6], "0");// maxReminders
				put(colNames[7], "0");// allowedReminders
				put(colNames[8], "1");// allowedAvailability
				put(colNames[9], "1");// allowedAttendeeTypes
				put(colNames[10], "1");// canModifyTimeZone
				put(colNames[11], "0");// canOrganizerRespond
				put(colNames[12], "0");// canPartiallyUpdate
				put(colNames[13], "MediComp");// calendar_location
				put(colNames[14], null);// calendar_timezone
				put(colNames[15], "4");// calendar_access_level
				put(colNames[16], "0");// deleted
				put(colNames[17], "0");// cal_sync1
				put(colNames[18], "1");// cal_sync2
				put(colNames[19], "2");// cal_sync3
				put(colNames[20], "3");// cal_sync4
				put(colNames[21], "4");// cal_sync5
				put(colNames[22], "5");// cal_sync6
				put(colNames[23], "6");// cal_sync7
				put(colNames[24], "7");// cal_sync8
				put(colNames[25], "8");// cal_sync9
				put(colNames[26], "9");// cal_sync10
			}
		};
	}

	private int position = -1;
	private boolean closed = false;
	private boolean active = true;
	private static final int CALENDARS_COUNT = 1;

	public void close() {
		closed = true;
	}

	public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
		buffer.data = calendars[position].get(colNames[columnIndex])
				.toCharArray();
		buffer.sizeCopied = buffer.data.length;
	}

	public void deactivate() {
		active = false;
	}

	public byte[] getBlob(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getColumnCount() {
		return colNames.length;
	}

	public int getColumnIndex(String columnName) {
		for (int i = 0; i < colNames.length; i++) {
			if (colNames[i].equals(columnName))
				return i;
		}
		return -1;
	}

	public int getColumnIndexOrThrow(String columnName)
			throws IllegalArgumentException {
		int idx = getColumnIndex(columnName);
		if (idx < 0)
			throw new IllegalArgumentException("No such column");
		return idx;
	}

	public String getColumnName(int columnIndex) {
		return colNames[columnIndex];
	}

	public String[] getColumnNames() {
		return colNames;
	}

	public int getCount() {
		return CALENDARS_COUNT;
	}

	public double getDouble(int columnIndex) {
		return new Double(getString(columnIndex));
	}

	public Bundle getExtras() {
		// TODO Auto-generated method stub
		return null;
	}

	public float getFloat(int columnIndex) {
		return new Float(getString(columnIndex));
	}

	public int getInt(int columnIndex) {
		return new Integer(getString(columnIndex));
	}

	public long getLong(int columnIndex) {
		return new Long(getString(columnIndex));
	}

	public int getPosition() {
		return position;
	}

	public short getShort(int columnIndex) {
		return new Short(getString(columnIndex));
	}

	public String getString(int columnIndex) {
		return calendars[position].get(colNames[columnIndex]);
	}

	public boolean getWantsAllOnMoveCalls() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isAfterLast() {
		return position > CALENDARS_COUNT - 1;
	}

	public boolean isBeforeFirst() {
		return position < 0;
	}

	public boolean isClosed() {
		return closed;
	}

	public boolean isFirst() {
		return position == 0;
	}

	public boolean isLast() {
		return position == CALENDARS_COUNT - 1;
	}

	public boolean isNull(int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean move(int offset) {
		return _move(position + offset);
	}

	private boolean _move(int pos) {
		position = pos;
		if (position > CALENDARS_COUNT)
			position = CALENDARS_COUNT;
		if (position < 0)
			position = -1;
		return -1 < position && position < CALENDARS_COUNT;
	}

	public boolean moveToFirst() {
		return _move(0);
	}

	public boolean moveToLast() {
		return _move(CALENDARS_COUNT - 1);
	}

	public boolean moveToNext() {
		return _move(++position);
	}

	public boolean moveToPosition(int position) {
		return _move(position);
	}

	public boolean moveToPrevious() {
		return _move(--position);
	}

	public void registerContentObserver(ContentObserver observer) {
		// TODO Auto-generated method stub

	}

	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	public boolean requery() {
		active = true;
		return false;
	}

	public Bundle respond(Bundle extras) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setNotificationUri(ContentResolver cr, Uri uri) {
		// TODO Auto-generated method stub

	}

	public void unregisterContentObserver(ContentObserver observer) {
		// TODO Auto-generated method stub

	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

}
