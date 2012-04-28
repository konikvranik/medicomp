package net.suteren.medicomp.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.record.Record;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.util.Base64;

public class PersonMarshaller {

	private static final String MEDICOMP_ROOT_ELEMENT = "MediComp";
	private static final String PERSON_ELEMENT_NAME = "Person";
	private static final String ID_ATTRIBUTE_NAME = "id";
	private static final String GENDER_ATTRIBUTE_NAME = "gender";
	private static final String NAME_ATTRIBUTE_NAME = "name";
	private static final String BIRTHDAY_ATTRIBUTE_NAME = "birthday";
	private static final String PICTURE_ELEMENT_NAME = "Picture";
	private static final String APP_VERSION_ATTRIBUTE_NAME = "applicationVersion";
	private static final String DB_VERSION_ATTRIBUTE_NAME = "databaseVersion";
	private static final String TIMESTAMP_ATTRIBUTE_NAME = "timestamp";
	private static final String APP_CODE_ATTRIBUTE_NAME = "applicationCode";
	private Document document;
	private Element rootElement;

	public PersonMarshaller(Document doc, String appVersion,
			String appVersionName, String dbVersion)
			throws ParserConfigurationException {
		document = doc;
		if (doc == null)
			document = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
		rootElement = document.createElement(MEDICOMP_ROOT_ELEMENT);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		if (appVersion != null)
			rootElement.setAttribute(APP_VERSION_ATTRIBUTE_NAME, appVersion);
		if (appVersionName != null)
			rootElement.setAttribute(APP_CODE_ATTRIBUTE_NAME, appVersionName);
		if (dbVersion != null)
			rootElement.setAttribute(DB_VERSION_ATTRIBUTE_NAME, dbVersion);
		rootElement.setAttribute(TIMESTAMP_ATTRIBUTE_NAME,
				df.format(new Date()));

		document.appendChild(rootElement);
	}

	public Node marshall(Person person) throws IOException {

		Element personNode = document.createElement(PERSON_ELEMENT_NAME);
		personNode.setAttribute(ID_ATTRIBUTE_NAME,
				Integer.toString(person.getId()));
		if (person.getGender() != null)
			personNode.setAttribute(GENDER_ATTRIBUTE_NAME, person.getGender()
					.toString());

		personNode.setAttribute(NAME_ATTRIBUTE_NAME, person.getName());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (person.getBirthDate() != null)
			personNode.setAttribute(BIRTHDAY_ATTRIBUTE_NAME,
					df.format(person.getBirthDate().getTime()));

		if (person.getPicture() != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream objectWriter = new ObjectOutputStream(baos);
			objectWriter.writeObject(person.getPicture());
			Element pictureNode = document.createElement(PICTURE_ELEMENT_NAME);
			pictureNode.appendChild(document.createTextNode(Base64
					.encodeToString(baos.toByteArray(), Base64.DEFAULT)));
			personNode.appendChild(pictureNode);
		}
		RecordMarshaller rmarsh = new RecordMarshaller(personNode);
		for (Record r : person.getRecords()) {
			rmarsh.marshall(r);
		}

		person.getOtherInsurances();
		person.getStatutoryInsurance();
		person.getAlergies();

		rootElement.appendChild(personNode);
		return personNode;
	}

	public Person unmarshall(Node personNode) {
		return null;
	}

}
