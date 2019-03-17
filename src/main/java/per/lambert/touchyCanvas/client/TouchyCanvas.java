package per.lambert.touchyCanvas.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TouchyCanvas implements EntryPoint {
	/**
	 * Static instance to allow message handling.
	 */
	private static TouchyCanvas instance;
	/**
	 * Root layout panel.
	 */
	private RootLayoutPanel rootLayoutPanel;
	/**
	 * Main holder of content.
	 */
	private DockLayoutPanel holder;
	/**
	 * Panel for messages.
	 */
	private HorizontalPanel messages;
	/**
	 * Widget for messages.
	 */
	private TextArea messageArea;
	/**
	 * Builder of messages.
	 */
	private StringBuilder messageBuilder;
	/**
	 * Panel to hold canvas.
	 */
	private LayoutPanel imagePanel;
	/**
	 * Cavas with inage.
	 */
	private ImageCanvasPanel imageCanvas;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		rootLayoutPanel = RootLayoutPanel.get();
		holder = new DockLayoutPanel(Unit.PX);
		messages = new HorizontalPanel();
		messages.setSize("100%", "100%");
		messageArea = new TextArea();
		messageArea.setSize("100%", "100%");
		messageArea.setReadOnly(true);
		messages.add(messageArea);
		holder.addSouth(messages, 70);
		imagePanel = new LayoutPanel();
		imagePanel.setSize("100%", "100%");
		imageCanvas = new ImageCanvasPanel();
		imagePanel.add(imageCanvas);
		holder.add(imagePanel);
		rootLayoutPanel.add(holder);
		imageCanvas.setImage(constructPath() + "FallPanorama.jpg");
		messageBuilder = new StringBuilder();
		instance = this;
	}

	/**
	 * Get proper web path.
	 * 
	 * @return proper web path
	 */
	private String constructPath() {
		String path = Location.getPath();
		if (path.toLowerCase().endsWith(".html")) {
			int index = path.lastIndexOf("/");
			path = path.substring(0, index + 1);
		}
		return path;
	}

	/**
	 * Add message to window.
	 * @param message to add
	 */
	public static void addMessage(final String message) {
		instance.messageBuilder.append(message);
		instance.messageBuilder.append(" , ");
		instance.messageArea.setText(instance.messageBuilder.toString());
	}
}
