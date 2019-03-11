package per.lambert.touchyCanvas;

import per.lambert.touchyCanvas.client.TouchyCanvasTest;
import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TouchyCanvasSuite extends GWTTestSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite("Tests for TouchyCanvas");
		suite.addTestSuite(TouchyCanvasTest.class);
		return suite;
	}
}
