package net.suteren.medicomp.smartinput;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.WithId;
import net.suteren.medicomp.domain.record.Field;
import net.suteren.medicomp.domain.record.Record;
import net.suteren.medicomp.enums.Type;
import net.suteren.medicomp.ui.activity.MedicompActivity;
import net.suteren.medicomp.ui.adapter.AbstractListAdapter;
import net.suteren.medicomp.util.RecordFormat;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import static net.suteren.medicomp.ui.activity.MedicompActivity.TYPE_CHOOSER_DIALOG;

public class SmartInput {

	private ListView listView;
	private MedicompActivity context;
	private Dao<Record, Integer> recordDao;
	private HashMap<Type, Record> availableTypes;
	private Entry<Type, Record>[] availableArray;

	public SmartInput(MedicompActivity ma) throws SQLException {
		context = ma;
		listView = context.getListView();
		recordDao = MediCompDatabaseFactory.getInstance().createDao(
				Record.class);
	}

	private Map<Type, Record> determineAvaliableTypes(String input,
			Person person) {
		availableTypes = new HashMap<Type, Record>();
		RecordFormat rf = new RecordFormat(Locale.getDefault(), context, person);
		boolean smartInputStrictParse = true;
		for (Type type : Type.values()) {
			try {
				Record r;
				r = rf.format(input, type, smartInputStrictParse);
				Log.d(this.getClass().getCanonicalName(),
						"Type " + r.getTitle());
				if (r != null)
					availableTypes.put(type, r);
			} catch (ParseException e) {
				// Don not add if ParseException
			}

		}

		if (availableTypes.size() == 0) {
			smartInputStrictParse = false;
			for (Type type : Type.values()) {
				try {
					Record r;
					r = rf.format(input, type, smartInputStrictParse);
					Log.d(this.getClass().getCanonicalName(),
							"Type " + r.getTitle());
					if (r != null)
						availableTypes.put(type, r);
				} catch (ParseException e) {
					// Don not add if ParseException
				}
			}
		}
		return availableTypes;
	}

	public void processChoosedRecord(int index) {
		processChoosedRecord(availableArray[index].getValue());
	}

	public void processChoosedRecord(Record choosedRecord) {
		try {
			addRecord(choosedRecord);
		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(), "Failed: ", e);
			Toast.makeText(context, R.string.failedToAddRecord,
					Toast.LENGTH_SHORT);
		}
		refreshView();
	}

	private void addRecord(Record r) throws SQLException {
		recordDao.create(r);
		for (@SuppressWarnings("rawtypes")
		Field f : r.getFields())
			f.persist();
	}

	public void processSmartInput() {

		Map<Type, Record> availableTypes = determineAvaliableTypes(
				context.getSmartInputText(), context.getPerson());

		if (availableTypes.size() > 1) {
			Log.d(this.getClass().getCanonicalName(), "Showing dialog: "
					+ TYPE_CHOOSER_DIALOG);
			context.showDialog(TYPE_CHOOSER_DIALOG);
			Log.d(this.getClass().getCanonicalName(), "Dialog "
					+ TYPE_CHOOSER_DIALOG + " done");
		} else if (availableTypes.size() == 1) {
			processChoosedRecord(availableTypes.values().iterator().next());
		} else {
			Toast.makeText(context,
					context.getResources().getString(R.string.noAvailableType),
					Toast.LENGTH_SHORT).show();
		}

		context.clearSmartInputText();
	}

	@SuppressWarnings("unchecked")
	private void refreshView() {
		if (listView != null) {
			listView.invalidateViews();
			listView.invalidate();
			listView.forceLayout();
			try {
				((AbstractListAdapter<? extends WithId>) listView.getAdapter())
						.update();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public String[] getAvailableTypes() {
		SortedSet<Entry<Type, Record>> ss = new TreeSet<Map.Entry<Type, Record>>(
				new Comparator<Map.Entry<Type, Record>>() {
					public int compare(Entry<Type, Record> o1,
							Entry<Type, Record> o2) {
						if (o1 == null || o1.getKey() == null)
							return -1;
						if (o2 == null || o2.getKey() == null)
							return 1;
						Type k1 = o1.getKey();
						Type k2 = o2.getKey();
						return k1.ordinal() - k2.ordinal();
					}
				});
		ss.addAll(availableTypes.entrySet());
		availableArray = ss.toArray(new Entry[0]);
		int count = availableArray.length;
		String[] strings = new String[count];

		for (int i = 0; i < count; i++) {
			Log.d(this.getClass().getCanonicalName(), "Added type: "
					+ availableArray[i].getKey().name());
			strings[i] = availableArray[i].getKey().name();
		}
		return strings;
	}
}
