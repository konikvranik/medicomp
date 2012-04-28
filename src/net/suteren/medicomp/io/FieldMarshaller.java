package net.suteren.medicomp.io;

import net.suteren.medicomp.domain.record.Field;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FieldMarshaller {

	private static final String FIELD_ELEMENT_NAME = "Field";
	private static final String ID_ATTRIBUTE_NAME = "id";
	private static final String NAME_ATTIRBUTE_NAME = "name";
	private static final String RECORD_ID_ATTRIBUTE_NAME = "recordId";
	private static final String TYPE_ATTRIBUTE_NAME = "type";
	private static final String UNIT_ATTRIBUTE_NAME = "unit";
	private static final String VALUE_ATTIRBUTE_NAME = "value";
	private Node parent;

	public FieldMarshaller(Node parent) {
		this.parent = parent;
	}

	public Node marshall(@SuppressWarnings("rawtypes") Field field) {
		Document doc = parent.getOwnerDocument();

		Element fieldNode = doc.createElement(FIELD_ELEMENT_NAME);

		fieldNode.setAttribute(ID_ATTRIBUTE_NAME,
				Integer.toString(field.getId()));

		fieldNode.setAttribute(NAME_ATTIRBUTE_NAME, field.getName());
		if (field.getRecord() != null)
			fieldNode.setAttribute(RECORD_ID_ATTRIBUTE_NAME,
					Integer.toString(field.getRecord().getId()));
		if (field.getType() != null)
			fieldNode.setAttribute(TYPE_ATTRIBUTE_NAME, field.getType()
					.toString());
		if (field.getUnit() != null)
			fieldNode.setAttribute(UNIT_ATTRIBUTE_NAME, field.getUnit()
					.toString());
		if (field.getValue() != null)
			fieldNode.setAttribute(VALUE_ATTIRBUTE_NAME, field.getValue()
					.toString());

		parent.appendChild(fieldNode);
		return fieldNode;
	}

	@SuppressWarnings("rawtypes")
	public Field unmarshall(Node fieldNode) {
		return null;
	}

}
