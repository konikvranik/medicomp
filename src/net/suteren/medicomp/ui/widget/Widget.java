package net.suteren.medicomp.ui.widget;

import net.suteren.medicomp.domain.PersistableWithId;
import android.view.View;
import android.view.ViewGroup;

public interface Widget extends PersistableWithId {

	View getView(View convertView, ViewGroup parent);

	boolean onClick(View view, long position, long id);
	
	boolean showPreferencesPane();

}
