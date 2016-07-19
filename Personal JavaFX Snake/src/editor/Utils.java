package editor;



import javafx.stage.Screen;

public class Utils {

	public static final int WIDTH = (int) (Screen.getPrimary().getBounds().getWidth());
	public static final int HEIGHT = (int) (Screen.getPrimary().getBounds().getHeight());
	public static final double SCALE_DELTA = 1.1;

	private static double ResolutionScaleX = 1.0;
	private static double ResolutionScaleY = 1.0;

	public static void scaleResolution() {
		double resolutionX = Screen.getPrimary().getBounds().getWidth();
		double resolutionY = Screen.getPrimary().getBounds().getHeight();

		double baseResolutionX = 1920;
		double baseResolutionY = 1080;

		ResolutionScaleX = baseResolutionX / resolutionX;
		ResolutionScaleY = baseResolutionY / resolutionY;

		System.out.println("width scale = " + ResolutionScaleX);
		System.out.println("height scale = " + ResolutionScaleY);

	}
	 public static double ScaleX(double value) {
	        double newSize = value/ResolutionScaleX;

	        return newSize;
	    }

	    public static double ScaleY(double value) {
	        double newSize = value/ResolutionScaleY;

	        return newSize;
	    }

	    public static double ScaleX_Y(double value){
	    	double newSize = value/((ResolutionScaleX+ResolutionScaleY)/2);

	    	return newSize;
	    }
}
