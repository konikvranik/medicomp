package net.suteren.medicomp.ui.widget.temperature;

import net.suteren.medicomp.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class TemperatureWidgetPreferenceActivity extends PreferenceActivity {
	
	 @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);  
 
         addPreferencesFromResource(R.xml.temperature_widget_preferences);
     }

}
