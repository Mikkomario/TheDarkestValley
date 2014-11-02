package darkest_test;

import flux_wav.OpenWavSoundBank;
import genesis_logic.AdvancedMouseListener;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import omega_world.Area;
import darkest_3dSound.SoundSource;

/**
 * TestSoundSource follows the mouse around and creates a sound when the mouse button is 
 * pressed
 * 
 * @author Mikko Hilpinen
 * @since 16.10.2014
 */
public class TestSoundSource extends SoundSource implements AdvancedMouseListener
{
	// CONSTRUCTOR	-------------------------------------
	
	/**
	 * Creates a new TestSoundSource to the given area
	 * 
	 * @param area
	 */
	public TestSoundSource(Area area)
	{
		super(new Point2D.Double(), 360, 0, 60, area);
		
		// Adds the object to handlers
		area.getMouseHandler().addMouseListener(this);
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------

	@Override
	public MouseButtonEventScale getCurrentButtonScaleOfInterest()
	{
		return MouseButtonEventScale.GLOBAL;
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		return false;
	}

	@Override
	public boolean listensPosition(Double testedPosition)
	{
		return false;
	}

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Double mousePosition,
			double eventStepTime)
	{
		// Plays a sound on button released
		if (eventType == MouseButtonEventType.PRESSED)
			playSound(OpenWavSoundBank.getWavSound("test", "bird"));
	}

	@Override
	public void onMouseMove(Double newMousePosition)
	{
		// Moves along wiht the mouse
		setPosition(newMousePosition);
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Double mousePosition, double eventStepTime)
	{
		// Doesn't react
	}
}
