package net.suteren.medicomp.io;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.suteren.medicomp.domain.record.Field;
import net.suteren.medicomp.domain.record.Record;
import net.suteren.medicomp.enums.Type;
import net.suteren.medicomp.enums.Unit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.util.Log;

public class FieldMarshaller {

	private static final String FIELD_ELEMENT_NAME = "Field";
	private static final String ID_ATTRIBUTE_NAME = "id";
	private static final String NAME_ATTIRBUTE_NAME = "name";
	private static final String RECORD_ID_ATTRIBUTE_NAME = "recordId";
	private static final String TYPE_ATTRIBUTE_NAME = "type";
	private static final String UNIT_ATTRIBUTE_NAME = "unit";
	private static final String VALUE_ATTIRBUTE_NAME = "value";
	private Node parent;
	private DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSSZ");

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
			fieldNode.setAttribute(TYPE_ATTRIBUTE_NAME, field.getType().name());
		if (field.getUnit() != null)
			fieldNode.setAttribute(UNIT_ATTRIBUTE_NAME, field.getUnit().name());
		if (field.getValue() != null)
			if (field.getValue() instanceof Date)
				fieldNode.setAttribute(VALUE_ATTIRBUTE_NAME,
						dateFormat.format(field.getValue()));
			else
				fieldNode.setAttribute(VALUE_ATTIRBUTE_NAME, field.getValue()
						.toString());

		parent.appendChild(fieldNode);
		return fieldNode;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Field unmarshall(Element fieldElement) throws Exception,
			SQLException {

		if (fieldElement == null)
			return null;

		if (!FIELD_ELEMENT_NAME.equals(fieldElement.getNodeName()))
			throw new Exception("Not a Field element");

		Field field = new Field();

		field.setId(new Integer(fieldElement.getAttribute(ID_ATTRIBUTE_NAME)));
		field.setName(fieldElement.getAttribute(NAME_ATTIRBUTE_NAME));

		try {
			Integer recordId = new Integer(
					fieldElement.getAttribute(RECORD_ID_ATTRIBUTE_NAME));
			Record record = new Record();
			record.setId(recordId);
			field.setRecord(record);
		} catch (Exception e) {
			Log.e(getClass().getCanonicalName(), e.getMessage(), e);
		}

		field.setType(Type.valueOf(fieldElement
				.getAttribute(TYPE_ATTRIBUTE_NAME)));
		field.setUnit(Unit.valueOf(fieldElement
				.getAttribute(UNIT_ATTRIBUTE_NAME)));
		Object value = null;
		try {
			value = new Integer(fieldElement.getAttribute(VALUE_ATTIRBUTE_NAME));
		} catch (NumberFormatException e) {
		}
		if (value == null)
			try {
				value = new Double(
						fieldElement.getAttribute(VALUE_ATTIRBUTE_NAME));
			} catch (NumberFormatException e) {
			}
		if (value == null)
			try {
				value = dateFormat.parse(fieldElement
						.getAttribute(VALUE_ATTIRBUTE_NAME));
			} catch (ParseException e) {
			}
		if (value == null)
			value = fieldElement.getAttribute(VALUE_ATTIRBUTE_NAME);
		field.setValue(value);

		return field;

	}

}
