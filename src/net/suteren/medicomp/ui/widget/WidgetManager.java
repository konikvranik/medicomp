package net.suteren.medicomp.ui.widget;

public interface WidgetManager {

	/**
	 * 
	 * @param widget
	 *            new widget instance
	 * @return widgetId
	 */
	boolean registerWidget(Widget widget, Integer position);

	/**
	 * 
	 * @param widgetId
	 *            widgetId
	 * @return success
	 */
	boolean unRegisterWidget(int widgetId);

	/**
	 * 
	 * @param widgetId
	 * @return
	 */
	Widget getItemById(int widgetId) throws Exception;

	/**
	 * 
	 * @param position
	 *            desired position
	 * @return real new position
	 */
	int move(int from, int to);
}
