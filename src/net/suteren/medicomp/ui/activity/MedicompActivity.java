package net.suteren.medicomp.ui.activity;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.ApplicationContextHolder;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.WithId;
import net.suteren.medicomp.domain.record.Field;
import net.suteren.medicomp.domain.record.Record;
import net.suteren.medicomp.io.XML;
import net.suteren.medicomp.plugin.MediCompPluginManager;
import net.suteren.medicomp.plugin.Plugin;
import net.suteren.medicomp.plugin.PluginActivity;
import net.suteren.medicomp.plugin.PluginManager;
import net.suteren.medicomp.plugin.chart.ChartPlugin;
import net.suteren.medicomp.plugin.person.PersonListActivity;
import net.suteren.medicomp.plugin.person.PersonPlugin;
import net.suteren.medicomp.plugin.person.PersonProfileActivity;
import net.suteren.medicomp.plugin.temperature.TemperatureListActivity;
import net.suteren.medicomp.plugin.temperature.TemperaturePlugin;
import net.suteren.medicomp.smartinput.SmartInput;
import net.suteren.medicomp.ui.AboutActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public abstract class MedicompActivity extends Activity {

	private final class ChangeReceiver extends BroadcastReceiver {

		private ChangeReceiver() {
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			listView.invalidateViews();
		}
	}

	public static final String PERSON_ID_EXTRA = "personId";
	public static final String RECORD_ID_EXTRA = "recordId";
	public static final String MEDICOMP_PREFS = "medicomp_preferences";
	public static final String DATA_CHANGE_ACTION = "net.suteren.medicomp.CHANGE";
	public static final String PERSON_DATA_CHANGE_CATEGORY = "net.suteren.medicomp.PERSON";
	public static final String RECORD_DATA_CHANGE_CATEGORY = "net.suteren.medicomp.RECORD";
	protected Dao<Person, Integer> personDao;
	protected Person person;
	protected ListView listView;
	protected ChangeReceiver changeReceiver;
	protected Context context;
	protected MediCompDatabaseFactory dbf;
	protected Dao<Record, Integer> recordDao;
	@SuppressWarnings("rawtypes")
	protected Dao<Field, Integer> fieldDao;
	private EditText smartInput;
	protected NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
	private PluginManager pluginManager;
	private LayoutInflater layoutInflater;
	public static final int TYPE_CHOOSER_DIALOG = 1;
	public static final String REGISTERED_PLUGINS_PREFS = "registered_plugins";

	public SmartInput si;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		pluginManager = MediCompPluginManager.getInstance(this);

		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		registerCorePlugins(pluginManager);

		ApplicationContextHolder.setContext(this);
		MediCompDatabaseFactory.init(this);
		try {
			dbf = MediCompDatabaseFactory.getInstance();
			recordDao = dbf.createDao(Record.class);
			personDao = dbf.createDao(Person.class);
			fieldDao = dbf.createDao(Field.class);
		} catch (SQLException e) {
			Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			throw new RuntimeException(e);
		}

		super.onCreate(savedInstanceState);

		setContentView(getContentViewId());

		listView = requestListView();

		try {
			si = new SmartInput(this);
		} catch (SQLException e) {
			Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			throw new Error(e.getMessage(), e);
		}

		if (listView != null) {
			changeReceiver = new ChangeReceiver();
			IntentFilter intFilter = new IntentFilter();
			intFilter.addCategory(MedicompActivity.PERSON_DATA_CHANGE_CATEGORY);
			intFilter.addAction(MedicompActivity.DATA_CHANGE_ACTION);
			registerReceiver(changeReceiver, intFilter);
			listView.setFocusable(false);
		}

		smartInput = (EditText) getWindow().findViewById(R.id.smart_input);
		if (smartInput != null) {
			smartInput.setRawInputType(Configuration.KEYBOARD_QWERTY);
			smartInput.setOnKeyListener(new OnKeyListener() {

				public boolean onKey(View v, int keyCode, KeyEvent event) {

					if (event.getAction() == KeyEvent.ACTION_DOWN)
						switch (keyCode) {
						case KeyEvent.KEYCODE_ENTER:
							si.processSmartInput();
							break;
						default:
							break;
						}
					return false;
				}
			});
		}
		ImageButton ib = (ImageButton) getWindow().findViewById(
				R.id.smart_input_button);
		if (ib != null)
			ib.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					si.processSmartInput();
				}
			});

	}

	protected View inflateView(int id, ViewGroup parent) {
		return layoutInflater.inflate(id, parent, false);
	}

	private void registerCorePlugins(PluginManager pluginManager2) {
		pluginManager2.registerPlugin(new PersonPlugin());
		pluginManager2.registerPlugin(new TemperaturePlugin());
		pluginManager2.registerPlugin(new ChartPlugin());
		// pluginManager2.registerPlugin(new IllnessPlugin());
	}

	protected abstract int getContentViewId();

	protected ListView requestListView() {
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		SubMenu modulesMenu = menu.findItem(R.id.modules).getSubMenu();
		for (final Plugin plugin : getPluginManager().getActivePlugins()) {
			if (plugin.hasActivity()) {
				MenuItem menuItem = modulesMenu.add(plugin.getName());
				final PluginActivity pluginActivity = plugin
						.newActivityInstance(MedicompActivity.this);
				menuItem.setIcon(pluginActivity.getIcon());
				menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						startActivity(pluginActivity.newIntent());
						return true;
					}
				});
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.choosePerson:
			startActivity(new Intent(this, PersonListActivity.class));
			break;

		case R.id.addPerson:
			Intent intent = new Intent(this, PersonProfileActivity.class);
			Editor prefs = getSharedPreferences(MEDICOMP_PREFS,
					Context.MODE_PRIVATE).edit();
			prefs.remove(PERSON_ID_EXTRA);
			prefs.commit();
			this.startActivity(intent);
			break;

		case R.id.showRecords:
			startActivity(new Intent(this, TemperatureListActivity.class));
			break;

		case R.id.quit:
			this.finish();
			break;

		case R.id.preferences:
			startActivity(new Intent(this, MedicompPreferencesActivity.class));
			break;

		case R.id.export:
			XML xmlExport;
			try {
				xmlExport = new XML(this, personDao, recordDao);
				xmlExport.exportData();
			} catch (ParserConfigurationException e) {
				Log.e(getClass().getCanonicalName(), e.getMessage(), e);
				Toast.makeText(context, R.string.failedToExport,
						Toast.LENGTH_LONG);
			}
			break;

		case R.id.imp:
			try {
				XML xmlImport = new XML(this, personDao, recordDao);
				xmlImport.importData();
			} catch (ParserConfigurationException e) {
				Log.e(getClass().getCanonicalName(), e.getMessage(), e);
				Toast.makeText(context, R.string.failedToImortData,
						Toast.LENGTH_LONG);
			}
			actualizeList();
			break;

		case R.id.about:
			startActivity(new Intent(this, AboutActivity.class));
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	protected <T extends WithId> T setupObject(Dao<T, Integer> objectDao, int id) {
		try {
			T object = objectDao.queryForId(id);
			objectDao.closeLastIterator();
			return object;
		} catch (SQLException e) {
			Log.e(this.getClass().getCanonicalName(), "Failed: ", e);
			return null;
		}
	}

	protected boolean setupPerson() {
		Integer personId = determinePersonId();
		if (personId == null)
			return false;
		person = setupObject(personDao, personId);
		return person != null;
	}

	protected void redirectToPersonList() {
		startActivity(new Intent(this, PersonListActivity.class));
		finish();
	}

	protected Integer determinePersonId() {
		return determineId(PERSON_ID_EXTRA, true);
	}

	protected Integer determineId(String name, boolean cache) {
		Integer personId = null;
		Intent intent = getIntent();
		Bundle extras = null;
		if (intent != null)
			extras = intent.getExtras();
		if (extras != null)
			personId = extras.getInt(name);
		if (personId != null && personId > 0)
			return personId;

		if (cache)
			try {
				personId = getSharedPreferences(MEDICOMP_PREFS,
						Context.MODE_PRIVATE).getInt(PERSON_ID_EXTRA, 0);
			} catch (ClassCastException e) {
				return null;
			}
		if (personId != 0)
			return personId;

		return null;

	}

	protected ListAdapter getAdapter() {
		return null;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		actualizeList();
	}

	@Override
	protected void onResume() {
		super.onResume();
		actualizeList();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		actualizeList();
	}

	@Override
	protected void onDestroy() {
		if (changeReceiver != null)
			unregisterReceiver(changeReceiver);
		super.onDestroy();
	}

	protected void actualizeList() {
		setupPerson();
		if (listView != null) {
			listView.setAdapter(getAdapter());
			listView.invalidateViews();
		}
	}

	@Override
	protected Dialog onCreateDialog(final int id) {

		Log.d(this.getClass().getCanonicalName(), "Creating dialog: " + id);

		switch (id) {
		case TYPE_CHOOSER_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Pick a color");
			builder.setCancelable(true);
			builder.setItems(si.getAvailableTypes(),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							si.processChoosedRecord(item);
							removeDialog(id);
						}
					});
			return builder.create();
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	protected void onPrepareDialog(final int id, Dialog dialog) {

		Log.d(this.getClass().getCanonicalName(), "Preparing dialog: " + id);

		switch (id) {
		case TYPE_CHOOSER_DIALOG:
		default:
			super.onPrepareDialog(id, dialog);
		}
	}

	public PluginManager getPluginManager() {
		return pluginManager;
	}

	public ListView getListView() {
		return listView;
	}

	public String getSmartInputText() {
		return smartInput.getText().toString();
	}

	public void setSmartInputText(String text) {
		smartInput.setText(text);
	}

	public void clearSmartInputText() {
		smartInput.setText("");
	}

	public Person getPerson() {
		return person;
	}

}
