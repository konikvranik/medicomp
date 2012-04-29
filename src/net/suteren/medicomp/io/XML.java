package net.suteren.medicomp.io;

import static net.suteren.medicomp.io.PersonMarshaller.PERSON_ELEMENT_NAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.record.Field;
import net.suteren.medicomp.domain.record.Record;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public class XML {

	private static final String APP_VERSION_ATTRIBUTE_NAME = "applicationVersion";
	private static final String DB_VERSION_ATTRIBUTE_NAME = "databaseVersion";
	private static final String TIMESTAMP_ATTRIBUTE_NAME = "timestamp";
	private static final String APP_CODE_ATTRIBUTE_NAME = "applicationCode";
	private static final String MEDICOMP_ROOT_ELEMENT = "MediComp";

	private Context context;
	private Dao<Person, Integer> personDao;
	private Version appVersion;
	private DocumentBuilder documentBuilder = DocumentBuilderFactory
			.newInstance().newDocumentBuilder();
	private Version xmlVersion;
	private Dao<Record, Integer> recordDao;
	SimpleDateFormat dateTimeformat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSSZ");

	public XML(Context context, Dao<Person, Integer> personDao,
			Dao<Record, Integer> recordDao) throws ParserConfigurationException {
		this.context = context;
		this.personDao = personDao;
		this.recordDao = recordDao;
		appVersion = new Version();
	}

	public void exportData() {

		try {
			Document doc = documentBuilder.newDocument();
			PersonMarshaller mrsh = getMarshaller(doc);
			Node rootElement = makeRoot(doc);
			for (Person p : personDao.queryForAll()) {
				mrsh.marshall(rootElement, p);
			}

			Result stre;

			File file = getExtFile();
			if (file == null) {
				stre = new StreamResult(context.openFileOutput(
						context.getPackageName(), Context.MODE_WORLD_READABLE));
			} else {
				stre = new StreamResult(file);
			}

			Transformer trsf = TransformerFactory.newInstance()
					.newTransformer();
			trsf.setOutputProperty(OutputKeys.INDENT, "yes");
			trsf.transform(new DOMSource(doc), stre);

		} catch (Exception e) {
			Log.e(getClass().getCanonicalName(), e.getMessage(), e);
			Toast.makeText(context, R.string.failedToExport, Toast.LENGTH_LONG);
		}

	}

	private File getExtFile() {
		String state = Environment.getExternalStorageState();
		File file = null;
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File path = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			file = new File(path.getParentFile(), "MediComp.xml");
		}
		return file;
	}

	private PersonMarshaller getMarshaller(Document doc)
			throws ParserConfigurationException {
		PersonMarshaller mrsh = new PersonMarshaller(doc);
		return mrsh;
	}

	public void importData() {

		try {
			InputStream stsr;

			File file = getExtFile();
			if (file == null) {
				stsr = context.openFileInput(context.getPackageName());
			} else {
				stsr = new FileInputStream(file);
			}

			Document doc = documentBuilder.parse(stsr);

			PersonMarshaller pmrsh = new PersonMarshaller(doc);

			Element root = getRoot(doc);

			Node child = root.getFirstChild();
			for (; child != null; child = child.getNextSibling()) {
				if (child.getNodeType() == Node.ELEMENT_NODE
						&& PERSON_ELEMENT_NAME.equals(child.getNodeName())) {
					try {
						Person p = pmrsh.unmarshall((Element) child);
						personDao.createOrUpdate(p);
						for (Record r : p.getRecords()) {
							recordDao.createOrUpdate(r);
							for (@SuppressWarnings("rawtypes")
							Field f : r.getFields()) {
								f.persist();
							}
						}
						p.getStatutoryInsurance();
						p.getAlergies();
						p.getOtherInsurances();
					} catch (ClassCastException e) {
						Log.e(getClass().getCanonicalName(), e.getMessage(), e);
					}
				}
			}
		} catch (Exception e) {
			Log.e(getClass().getCanonicalName(), e.getMessage(), e);
			Toast.makeText(context, R.string.failedToImortData,
					Toast.LENGTH_LONG);
		}
	}

	private Element getRoot(Document doc) {
		Element rootElement = doc.getDocumentElement();
		xmlVersion = new Version(rootElement);
		checkVersion(appVersion, xmlVersion);
		return rootElement;
	}

	private boolean checkVersion(Version appVersion2, Version xmlVersion2) {
		// TODO
		return true;
	}

	private Node makeRoot(Document document) {
		Element rootElement = document.createElement(MEDICOMP_ROOT_ELEMENT);

		if (appVersion != null) {
			if (appVersion.getAppVersionCode() != null)
				rootElement.setAttribute(APP_VERSION_ATTRIBUTE_NAME, appVersion
						.getAppVersionCode().toString());
			if (appVersion.getAppVersionName() != null)
				rootElement.setAttribute(APP_CODE_ATTRIBUTE_NAME,
						appVersion.getAppVersionName());
			if (appVersion.getDbVersion() != null)
				rootElement.setAttribute(DB_VERSION_ATTRIBUTE_NAME, appVersion
						.getDbVersion().toString());
		}

		rootElement.setAttribute(TIMESTAMP_ATTRIBUTE_NAME,
				dateTimeformat.format(new Date()));

		document.appendChild(rootElement);

		return rootElement;
	}

	public class Version {
		Integer appVersionCode;
		String appVersionName;
		Integer dbVersion;

		public Version() {
			PackageInfo packageInfo;
			try {
				packageInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				appVersionName = packageInfo.versionName;
				appVersionCode = packageInfo.versionCode;
			} catch (NameNotFoundException e) {
				Log.e(getClass().getCanonicalName(), e.getMessage(), e);
				Toast.makeText(context, R.string.failedToGetAppVersion,
						Toast.LENGTH_LONG);
			}
			dbVersion = MediCompDatabaseFactory.getInstance().getHelper()
					.getVersion();

		}

		public Version(Element rootNode) {
			if (rootNode == null
					|| !MEDICOMP_ROOT_ELEMENT.equals(rootNode.getNodeName()))
				return;
			try {
				dbVersion = Integer.parseInt(rootNode
						.getAttribute(DB_VERSION_ATTRIBUTE_NAME));
			} catch (NumberFormatException e) {
				dbVersion = null;
			}
			try {
				appVersionCode = Integer.parseInt(rootNode
						.getAttribute(APP_CODE_ATTRIBUTE_NAME));
			} catch (NumberFormatException e) {
				appVersionCode = null;
			}
			appVersionName = rootNode.getAttribute(APP_VERSION_ATTRIBUTE_NAME);
		}

		public Integer getAppVersionCode() {
			return appVersionCode;
		}

		public void setAppVersionCode(Integer appVersionCode) {
			this.appVersionCode = appVersionCode;
		}

		public String getAppVersionName() {
			return appVersionName;
		}

		public void setAppVersionName(String appVersionName) {
			this.appVersionName = appVersionName;
		}

		public Integer getDbVersion() {
			return dbVersion;
		}

		public void setDbVersion(Integer dbVersion) {
			this.dbVersion = dbVersion;
		}
	}
}
