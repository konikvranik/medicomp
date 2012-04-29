package net.suteren.medicomp.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.suteren.medicomp.domain.Alergie;
import net.suteren.medicomp.domain.Insurance;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.Person.Gender;
import net.suteren.medicomp.domain.record.Record;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.util.Base64;
import android.util.Log;

public class PersonMarshaller {

	public static final String PERSON_ELEMENT_NAME = "Person";
	private static final String ID_ATTRIBUTE_NAME = "id";
	private static final String GENDER_ATTRIBUTE_NAME = "gender";
	private static final String NAME_ATTRIBUTE_NAME = "name";
	private static final String BIRTHDAY_ATTRIBUTE_NAME = "birthday";
	private static final String PICTURE_ELEMENT_NAME = "Picture";
	private static final Object INSURANCES_ELEMENT_NAME = "insurances";
	private static final Object ALERGIES_ELEMENT_NAME = "alergies";
	private static final String RECORDS_ELEMENT_NAME = "records";
	private static final Object STATUTORY_INSURANCE_ELEMENT_NAME = "StatutoryInsurance";
	private Document document;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddZ");

	public PersonMarshaller(Document doc) throws ParserConfigurationException {
		document = doc;
		if (doc == null)
			document = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
	}

	public Node marshall(Node parent, Person person) throws IOException {

		Element personNode = document.createElement(PERSON_ELEMENT_NAME);
		personNode.setAttribute(ID_ATTRIBUTE_NAME,
				Integer.toString(person.getId()));
		if (person.getGender() != null)
			personNode.setAttribute(GENDER_ATTRIBUTE_NAME, person.getGender()
					.name());

		personNode.setAttribute(NAME_ATTRIBUTE_NAME, person.getName());
		if (person.getBirthDate() != null)
			personNode.setAttribute(BIRTHDAY_ATTRIBUTE_NAME,
					dateFormat.format(person.getBirthDate().getTime()));

		if (person.getPicture() != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream objectWriter = new ObjectOutputStream(baos);
			objectWriter.writeObject(person.getPicture());
			Element pictureNode = document.createElement(PICTURE_ELEMENT_NAME);
			pictureNode.appendChild(document.createTextNode(Base64
					.encodeToString(baos.toByteArray(), Base64.DEFAULT)));
			personNode.appendChild(pictureNode);
		}

		Element records = document.createElement(RECORDS_ELEMENT_NAME);
		personNode.appendChild(records);
		RecordMarshaller rmarsh = new RecordMarshaller(records);
		for (Record r : person.getRecords()) {
			rmarsh.marshall(r);
		}

		person.getOtherInsurances();
		person.getStatutoryInsurance();
		person.getAlergies();

		parent.appendChild(personNode);
		return personNode;
	}

	public Person unmarshall(Element personElement) throws XMLParseException {

		if (PERSON_ELEMENT_NAME.equals(personElement.getNodeName()))
			throw new XMLParseException("Not a Person element");

		Person person = new Person();

		person.setId(new Integer(personElement.getAttribute(ID_ATTRIBUTE_NAME)));
		try {
			person.setBirthDate(dateFormat.parse(personElement
					.getAttribute(BIRTHDAY_ATTRIBUTE_NAME)));
		} catch (ParseException e) {
			Log.e(getClass().getCanonicalName(), e.getMessage(), e);
		}
		person.setGender(Gender.valueOf(personElement
				.getAttribute(GENDER_ATTRIBUTE_NAME)));
		person.setName(personElement.getAttribute(NAME_ATTRIBUTE_NAME));

		InsuranceMarshaller imsh = new InsuranceMarshaller(personElement);

		Node child = personElement.getFirstChild();
		for (; child != null; child = child.getNextSibling()) {

			if (child.getNodeType() != Node.ELEMENT_NODE)
				continue;

			if (RECORDS_ELEMENT_NAME.equals(child.getNodeName()))
				person.setRecords(marshallRecords((Element) child));
			else if (INSURANCES_ELEMENT_NAME.equals(child.getNodeName()))
				person.setOtherInsurances(marshallInsurances((Element) child));
			else if (ALERGIES_ELEMENT_NAME.equals(child.getNodeName()))
				person.setAlergies(marshallAlergies((Element) child));
			else if (STATUTORY_INSURANCE_ELEMENT_NAME.equals(child
					.getNodeName()))
				person.setStatutoryInsurance(imsh.unmarshall((Element) child));

		}

		// TODO person.setPicture(picture);

		return person;
	}

	private Collection<Alergie> marshallAlergies(Element child) {
		// TODO Auto-generated method stub
		return null;
	}

	private Collection<Insurance> marshallInsurances(Element child) {
		// TODO Auto-generated method stub
		return null;
	}

	private Collection<Record> marshallRecords(Element parent) {

		ArrayList<Record> records = new ArrayList<Record>();

		RecordMarshaller rmsh = new RecordMarshaller(parent);

		Node child = parent.getFirstChild();
		for (; child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE)
				continue;
			try {
				Record r = rmsh.unmarshall((Element) child);
				records.add(r);
			} catch (Exception e) {
				Log.e(getClass().getCanonicalName(), e.getMessage(), e);
			}
		}

		return records;
	}
}
