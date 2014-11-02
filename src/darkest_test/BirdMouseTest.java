package darkest_test;

import darkest_3dSound.Ear;
import darkest_3dSound.Ear.Side;
import darkest_utility.Util;
import omega_world.Area;

/**
 * BirdMouseTest tests the 3d sound functionality
 * 
 * @author Mikko Hilpinen
 * @since 17.10.2014
 */
public class BirdMouseTest
{
	// CONSTRUCTOR	-----------------------------------------------
	
	/**
	 * Creates a new BirdMouseTest to the given area
	 * @param area The area the test will be held at
	 */
	public BirdMouseTest(Area area)
	{
		// Creates stuff to the given area
		new Ear(310, 240, Side.LEFT, area);
		new Ear(310 + (int) Util.metersToPixels(0.2), 240, Side.RIGHT, area);
		new TestSoundSource(area);
	}
}
