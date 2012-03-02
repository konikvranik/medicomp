package net.suteren.medicomp.ui.widget;

public interface WidgetManager {

	/**
	 * 
	 * @param widget
	 *            new widget instance
	 * @return widgetId
	 */
	String registerWidget(Widget widget);

	/**
	 * 
	 * @param widgetId
	 *            widgetId
	 * @return success
	 */
	boolean unRegisterWidget(String widgetId);

	/**
	 * 
	 * @param widgetId
	 * @return
	 */
	Widget getWidgetById(String widgetId);

	/**
	 * 
	 * @param position
	 *            desired position
	 * @return real new position
	 */
	Integer moveWidgetToPosition(Widget widget, int position);
}
