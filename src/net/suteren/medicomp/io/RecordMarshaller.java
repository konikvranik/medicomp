package net.suteren.medicomp.io;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.suteren.medicomp.domain.record.Field;
import net.suteren.medicomp.domain.record.Record;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class RecordMarshaller {

	private static final String RECORD_ELEMENT_NAME = "Record";
	private static final String ID_ATTRIBUTE_NAME = "id";
	private static final String TITLE_ATTRIBUTE_NAME = "title";
	private static final String TYPE_ATTIRBUTE_NAME = "type";
	private static final String TIMESTAMP_ARRTIBUTE_NAME = "timestamp";
	private static final String PARENT_ID_ATTRIBUTE_NAME = "parentId";
	private static final String CATEGORY_ATTRIBUTE_NAME = "category";
	private static final String PERSON_ID_ATTRIBUTE_NAME = "personId";
	private Node parent;

	RecordMarshaller(Node parent) {
		this.parent = parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node marshall(Record record) {
		Document doc = parent.getOwnerDocument();

		Element recordNode = doc.createElement(RECORD_ELEMENT_NAME);
		recordNode.setAttribute(ID_ATTRIBUTE_NAME,
				Integer.toString(record.getId()));
		recordNode.setAttribute(TITLE_ATTRIBUTE_NAME, record.getTitle());
		recordNode.setAttribute(TYPE_ATTIRBUTE_NAME, record.getType()
				.toString());

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		if (record.getTimestamp() != null)
			recordNode.setAttribute(TIMESTAMP_ARRTIBUTE_NAME,
					df.format(record.getTimestamp().getTime()));

		if (record.getParent() != null)
			recordNode.setAttribute(PARENT_ID_ATTRIBUTE_NAME,
					Integer.toString(record.getParent().getId()));

		if (record.getCategory() != null)
			recordNode.setAttribute(CATEGORY_ATTRIBUTE_NAME, record
					.getCategory().toString());

		if (record.getPerson() != null)
			recordNode.setAttribute(PERSON_ID_ATTRIBUTE_NAME,
					Integer.toString(record.getPerson().getId()));

		FieldMarshaller fldmrs = new FieldMarshaller(recordNode);
		for (@SuppressWarnings("rawtypes")
		Field f : record.getFields()) {
			fldmrs.marshall(f);
		}

		parent.appendChild(recordNode);
		return recordNode;
	}
}
