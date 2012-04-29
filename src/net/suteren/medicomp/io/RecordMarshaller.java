package net.suteren.medicomp.io;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import javax.management.modelmbean.XMLParseException;

import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.record.Field;
import net.suteren.medicomp.domain.record.Record;
import net.suteren.medicomp.enums.Category;
import net.suteren.medicomp.enums.Type;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.util.Log;

public class RecordMarshaller {

	public static final String RECORD_ELEMENT_NAME = "Record";
	private static final String ID_ATTRIBUTE_NAME = "id";
	private static final String TITLE_ATTRIBUTE_NAME = "title";
	private static final String TYPE_ATTIRBUTE_NAME = "type";
	private static final String TIMESTAMP_ATTRIBUTE_NAME = "timestamp";
	private static final String PARENT_ID_ATTRIBUTE_NAME = "parentId";
	private static final String CATEGORY_ATTRIBUTE_NAME = "category";
	private static final String PERSON_ID_ATTRIBUTE_NAME = "personId";

	private Node recordNode;
	private DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSSZ");

	RecordMarshaller(Node parent) {
		this.recordNode = parent;
	}

	public void setParent(Node parent) {
		this.recordNode = parent;
	}

	public Node marshall(Record record) {
		Document doc = recordNode.getOwnerDocument();

		Element recordNode = doc.createElement(RECORD_ELEMENT_NAME);
		recordNode.setAttribute(ID_ATTRIBUTE_NAME,
				Integer.toString(record.getId()));
		recordNode.setAttribute(TITLE_ATTRIBUTE_NAME, record.getTitle());
		recordNode.setAttribute(TYPE_ATTIRBUTE_NAME, record.getType().name());

		if (record.getTimestamp() != null)
			recordNode.setAttribute(TIMESTAMP_ATTRIBUTE_NAME,
					dateFormat.format(record.getTimestamp().getTime()));

		if (record.getParent() != null)
			recordNode.setAttribute(PARENT_ID_ATTRIBUTE_NAME,
					Integer.toString(record.getParent().getId()));

		if (record.getCategory() != null)
			recordNode.setAttribute(CATEGORY_ATTRIBUTE_NAME, record
					.getCategory().name());

		if (record.getPerson() != null)
			recordNode.setAttribute(PERSON_ID_ATTRIBUTE_NAME,
					Integer.toString(record.getPerson().getId()));

		FieldMarshaller fldmrs = new FieldMarshaller(recordNode);
		for (@SuppressWarnings("rawtypes")
		Field f : record.getFields()) {
			fldmrs.marshall(f);
		}

		recordNode.appendChild(recordNode);
		return recordNode;
	}

	public Record unmarshall(Element recordElement) throws XMLParseException {

		if (recordElement == null)
			return null;

		if (RECORD_ELEMENT_NAME.equals(recordElement.getNodeName()))
			throw new XMLParseException("Not a Record element");

		Record record = new Record();

		record.setId(new Integer(recordElement.getAttribute(ID_ATTRIBUTE_NAME)));
		record.setTitle(recordElement.getAttribute(TITLE_ATTRIBUTE_NAME));
		try {
			record.setTimestamp(dateFormat.parse(recordElement
					.getAttribute(TIMESTAMP_ATTRIBUTE_NAME)));
		} catch (ParseException e) {
			Log.e(getClass().getCanonicalName(), e.getMessage(), e);
		}
		record.setCategory(Category.valueOf(recordElement
				.getAttribute(CATEGORY_ATTRIBUTE_NAME)));
		record.setType(Type.valueOf(recordElement
				.getAttribute(TYPE_ATTIRBUTE_NAME)));

		Integer parentId = new Integer(
				recordElement.getAttribute(PARENT_ID_ATTRIBUTE_NAME));
		if (parentId != null) {
			Record parentRecord = new Record();
			parentRecord.setId(parentId);
			record.setParent(parentRecord);
		}

		Integer personId = new Integer(
				recordElement.getAttribute(PERSON_ID_ATTRIBUTE_NAME));
		if (personId != null) {
			Person person = new Person();
			person.setId(personId);
			record.setPerson(person);
		}

		@SuppressWarnings("rawtypes")
		Collection<Field> fields = new ArrayList<Field>();

		FieldMarshaller fldmrs = new FieldMarshaller(recordNode);

		Node child = recordNode.getFirstChild();
		for (; child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE)
				continue;
			try {
				fields.add(fldmrs.unmarshall((Element) child));
			} catch (SQLException e) {
				Log.e(getClass().getCanonicalName(), e.getMessage(), e);
			}
		}
		record.setFields(fields);

		return record;
	}
}
