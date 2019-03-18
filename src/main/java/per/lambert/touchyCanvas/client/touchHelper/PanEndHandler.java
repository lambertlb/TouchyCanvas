package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for pan end.
 * @author LLambert
 *
 */
public interface PanEndHandler extends EventHandler {
	/**
	 * Called when panning ends.
	 * 
	 * @param event with data.
	 */
	void onPanEnd(PanEndEvent event);

}
