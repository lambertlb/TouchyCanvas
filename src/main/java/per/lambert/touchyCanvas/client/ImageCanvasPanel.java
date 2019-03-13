package per.lambert.touchyCanvas.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.googlecode.mgwt.dom.client.event.tap.HasTapHandlers;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.dom.client.event.touch.HasTouchHandlers;
import com.googlecode.mgwt.dom.client.event.touch.TouchHandler;
import com.googlecode.mgwt.dom.client.recognizer.longtap.HasLongTapHandlers;
import com.googlecode.mgwt.dom.client.recognizer.longtap.LongTapEvent;
import com.googlecode.mgwt.dom.client.recognizer.longtap.LongTapHandler;
import com.googlecode.mgwt.dom.client.recognizer.pinch.HasPinchHandlers;
import com.googlecode.mgwt.dom.client.recognizer.pinch.PinchEvent;
import com.googlecode.mgwt.dom.client.recognizer.pinch.PinchHandler;
import com.googlecode.mgwt.dom.client.recognizer.swipe.HasSwipeHandlers;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeEndEvent;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeEndHandler;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeMoveEvent;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeMoveHandler;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeStartEvent;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeStartHandler;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeEvent.DIRECTION;
import com.googlecode.mgwt.ui.client.widget.touch.GestureUtility;
import com.googlecode.mgwt.ui.client.widget.touch.TouchWidgetImpl;

public class ImageCanvasPanel extends AbsolutePanel implements MouseWheelHandler, MouseDownHandler, MouseMoveHandler, MouseUpHandler {
	/**
	 * Offset for clearing rectangle.
	 */
	private static final int CLEAR_OFFEST = -10;
	/**
	 * Default zoom constant.
	 */
	private static final double DEFAULT_ZOOM = 1.1;
	/**
	 * Image with picture.
	 */
	private Image image;
	/**
	 * image context for drawing.
	 */
	private ImageElement imageElement;
	/**
	 * Main canvas that is drawn.
	 */
	private ImageCanvas canvas;
	/**
	 * background canvas for temporary drawing.
	 */
	private Canvas backCanvas;
	/**
	 * Hidden panel to handle loading the image.
	 */
	private LayoutPanel hiddenPanel;
	/**
	 * Width of actual image.
	 */
	private int imageWidth = 0;
	/**
	 * Height of actual image.
	 */
	private int imageHeight = 0;
	/**
	 * Width of parent window.
	 */
	private int parentWidth = 0;
	/**
	 * Height of parent window.
	 */
	private int parentHeight = 0;
	/**
	 * current zoom factor for image.
	 */
	private double totalZoom = 1;
	/**
	 * Maximum zoom factor. We do not allow zooming out farther than the initial calculated zoom that fills the parent.
	 */
	private double maxZoom = .05;
	/**
	 * Offset of image in the horizontal direction.
	 */
	private double offsetX = 0;
	/**
	 * Offset of image in the vertical direction.
	 */
	private double offsetY = 0;
	/**
	 * Used by pan control.
	 */
	private boolean mouseDown = false;
	/**
	 * X position of mouse down.
	 */
	private double mouseDownXPos = 0;
	/**
	 * Y position of mouse down.
	 */
	private double mouseDownYPos = 0;

	public ImageCanvasPanel() {
		image = new Image();
		image.addLoadHandler(new LoadHandler() {
			public void onLoad(final LoadEvent event) {
				setupImage();
			}
		});
		canvas = ImageCanvas.createIfSupported();
		backCanvas = Canvas.createIfSupported();
		hiddenPanel = new LayoutPanel();
		hiddenPanel.add(image);
		hiddenPanel.setVisible(false);

		canvas.addMouseWheelHandler(this);
		canvas.addMouseMoveHandler(this);
		canvas.addMouseDownHandler(this);
		canvas.addMouseUpHandler(this);
		canvas.addSwipeStartHandler(new SwipeStartHandler() {

			@Override
			public void onSwipeStart(SwipeStartEvent event) {
				doSwiperStart(event);
			}
		});
		canvas.addSwipeEndHandler(new SwipeEndHandler() {

			@Override
			public void onSwipeEnd(SwipeEndEvent event) {
				doSwipeEnd(event);
			}
		});
		canvas.addSwipeMoveHandler(new SwipeMoveHandler() {

			@Override
			public void onSwipeMove(SwipeMoveEvent event) {
				doSwipeMove(event);
			}
		});

		super.add(canvas, 0, 0);
		super.add(hiddenPanel, -1, -1);
	}

	/**
	 * Tell image where picture is.
	 * 
	 * @param imageURL URL to picture
	 */
	public void setImage(final String imageURL) {
		image.setUrl(imageURL);
	}

	/**
	 * Called when image is loaded
	 */
	private void setupImage() {
		totalZoom = 1;
		offsetX = 0;
		offsetY = 0;
		this.imageElement = (ImageElement) image.getElement().cast();
		parentWidthChanged(getParent().getOffsetWidth(), getParent().getOffsetHeight());
	}

	/**
	 * Parent window size has changed.
	 * 
	 * @param widthOfParent new width of window.
	 * @param heightOfParent new height of window.
	 */
	public final void parentWidthChanged(final int widthOfParent, final int heightOfParent) {
		parentWidth = widthOfParent;
		parentHeight = heightOfParent;
		imageWidth = image.getWidth();
		imageHeight = image.getHeight();

		canvas.setWidth(parentWidth + "px");
		canvas.setCoordinateSpaceWidth(parentWidth);
		canvas.setHeight(parentHeight + "px");
		canvas.setCoordinateSpaceHeight(parentHeight);

		backCanvas.setWidth(parentWidth + "px");
		backCanvas.setCoordinateSpaceWidth(parentWidth);
		backCanvas.setHeight(parentHeight + "px");
		backCanvas.setCoordinateSpaceHeight(parentHeight);

		calculateStartingZoom();
		backCanvas.getContext2d().setTransform(totalZoom, 0, 0, totalZoom, 0, 0);
		drawEverything();
	}

	/**
	 * Calculate the starting zoom factor so that one side of the image exactly fills the parent.
	 */
	private void calculateStartingZoom() {
		if (isScaleByWidth()) {
			totalZoom = (double) parentWidth / (double) imageWidth;
		} else {
			totalZoom = (double) parentHeight / (double) imageHeight;
		}
	}

	/**
	 * Should we scale by width.
	 * 
	 * @return true if we should scale by width
	 */
	private boolean isScaleByWidth() {
		double scaleWidth = (double) parentWidth / (double) imageWidth;
		double scaleHeight = (double) parentHeight / (double) imageHeight;
		return scaleWidth < scaleHeight;
	}

	/**
	 * Main method for drawing image.
	 */
	public final void drawEverything() {
		backCanvas.getContext2d().clearRect(CLEAR_OFFEST, CLEAR_OFFEST, imageWidth + 50, imageHeight + 50);
		backCanvas.getContext2d().setTransform(totalZoom, 0, 0, totalZoom, offsetX, offsetY);
		backCanvas.getContext2d().drawImage(imageElement, 0, 0);
		handleAllDrawing();
	}

	/**
	 * Handle drawing everything.
	 */
	public final void handleAllDrawing() {
		canvas.getContext2d().clearRect(CLEAR_OFFEST, CLEAR_OFFEST, parentWidth +100, parentHeight + 100);
		canvas.getContext2d().drawImage(backCanvas.getCanvasElement(), 0, 0);
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
//		mouseDownXPos = event.getRelativeX(image.getElement());
//		mouseDownYPos = event.getRelativeY(image.getElement());
//		this.mouseDown = true;
	}

	protected void doSwiperStart(SwipeStartEvent event) {
		mouseDownXPos = event.getTouch().getPageX();
		mouseDownYPos = event.getTouch().getPageY();
		this.mouseDown = true;
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
//		this.mouseDown = false;
	}

	protected void doSwipeEnd(SwipeEndEvent event) {
		this.mouseDown = false;
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
//		if (mouseDown) {
//			handleMouseMove(event);
//		}
	}

	/**
	 * Handle mouse move.
	 * 
	 * @param event event data
	 */
	private void handleMouseMove(final MouseMoveEvent event) {
		double xPos = event.getRelativeX(image.getElement());
		double yPos = event.getRelativeY(image.getElement());
		handleCanvasMove(xPos, yPos);
	}

	private void handleCanvasMove(double xPos, double yPos) {
		offsetX += (xPos - mouseDownXPos);
		offsetY += (yPos - mouseDownYPos);
		try {
			drawEverything();
		} catch (Exception ex) {
			mouseDownXPos = xPos;
			mouseDownYPos = yPos;
		}
		mouseDownXPos = xPos;
		mouseDownYPos = yPos;
	}

	protected void doSwipeMove(SwipeMoveEvent event) {
		if (mouseDown) {
			double xPos = event.getTouch().getPageX();
			double yPos = event.getTouch().getPageY();
			handleCanvasMove(xPos, yPos);
		}
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		int move = event.getDeltaY();
		double xPos = (event.getRelativeX(canvas.getElement()));
		double yPos = (event.getRelativeY(canvas.getElement()));

		double zoom = DEFAULT_ZOOM;
		if (move >= 0) {
			zoom = 1 / DEFAULT_ZOOM;
		}

		double newX = (xPos - offsetX) / totalZoom;
		double newY = (yPos - offsetY) / totalZoom;
		double xPosition = (-newX * zoom) + newX;
		double yPosition = (-newY * zoom) + newY;

		zoom = zoom * totalZoom;
		if (zoom < maxZoom) {
			zoom = maxZoom;
		} else {
			offsetX += (xPosition * totalZoom);
			offsetY += (yPosition * totalZoom);
		}
		totalZoom = zoom;
		drawEverything();
	}
}
