package net.suteren.medicomp.format;

import java.text.ParseException;

import net.suteren.medicomp.domain.record.Record;

public interface RecordFormatter {

	Record parse(String input) throws ParseException;

	Record parse(String input, boolean strict) throws ParseException;

}
