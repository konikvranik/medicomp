package net.suteren.medicomp.format;

import net.suteren.medicomp.domain.record.Record;

public interface RecordFormatter {

	Record format(String input);

}
