package darkest_utility;

/**
 * Meters is a static helper class for handling meters in a pixel world
 * 
 * @author Mikko Hilpinen
 * @since 16.10.2014
 */
public class Util
{
	// ATTRIBUTES	------------------------------------
	
	private final static int PIXELS_IN_METER = 100;
	
	/**
	 * How fast the sound travels (in meters per second)
	 */
	public final static int SOUND_SPEED_METERS_PER_SECOND = 343;
	
	/**
	 * How many 'steps' there are in a single second
	 */
	public final static int STEPS_PER_SECOND = 30;
	
	/**
	 * How fast the sound travels (in pixels per step)
	 */
	public final static double SOUND_SPEED_PIXELS_PER_STEP = 
			metersToPixels(SOUND_SPEED_METERS_PER_SECOND) / secondsToSteps(1);
	
	/**
	 * How small sound will still be played
	 */
	public final static int MIN_SOUND_VOLUME = 5;
	
	/**
	 * How loud a sound should be at maximum
	 */
	public final static int MAX_SOUND_VOLUME = 120;
	
	
	// CONSTRUCTOR	------------------------------------
	
	private Util()
	{
		// The constructor is hidden since the interface is static
	}
	
	
	// OTHER METHODS	-------------------------------

	/**
	 * Transforms meters into pixels
	 * @param meters How many meters there are
	 * @return How many pixels the given amount of meters equal
	 */
	public static double metersToPixels(double meters)
	{
		return meters * PIXELS_IN_METER;
	}
	
	/**
	 * Transforms  pixels into meters
	 * @param pixels How many pixels there are
	 * @return How many meters the given amount of pixels equal
	 */
	public static double pixelsToMeters(double pixels)
	{
		return pixels / PIXELS_IN_METER;
	}
	
	/**
	 * Transforms steps into seconds
	 * @param steps How many steps there are
	 * @return How many seconds there are in the given amount of steps
	 */
	public static double stepsToSeconds(double steps)
	{
		return steps / STEPS_PER_SECOND;
	}
	
	/**
	 * Transforms seconds to steps
	 * @param seconds How many seconds there are
	 * @return How many steps there are in the given amount of seconds
	 */
	public static double secondsToSteps(double seconds)
	{
		return seconds * STEPS_PER_SECOND;
	}
	
	/**
	 * Calculates how many desibels of volume is lost during a distance the sound travels 
	 * in pixels
	 * @param pixels How many pixels the sound has traveled
	 * @return How many desibels of volume are lost during that transition
	 */
	public static double getVolumeLostInDistance(double pixels)
	{
		return 6 * (Math.log1p(pixelsToMeters(pixels)) / Math.log(2));
	}
	
	/**
	 * Calculates, how much the distance the sound traveled affects the volume level. 
	 * The volume stays at default (adjustment = 0) at the distance of 1 meter
	 * @param pixelsSoundTraveled How many pixels the sound traveled
	 * @return How much the volume of the sound is increased by the distance
	 */
	public static double getVolumeAdjustment(double pixelsSoundTraveled)
	{
		return -6 * (Math.log(pixelsToMeters(pixelsSoundTraveled) / Math.log(2)));
	}
}
