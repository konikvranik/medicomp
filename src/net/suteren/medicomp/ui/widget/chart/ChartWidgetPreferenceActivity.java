package net.suteren.medicomp.ui.widget.chart;

import net.suteren.medicomp.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ChartWidgetPreferenceActivity extends PreferenceActivity {
	
	 @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);  
 
         addPreferencesFromResource(R.xml.chart_widget_preferences);
     }

}
