package darkest_3dSound;

import genesis_graphic.DepthConstants;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import darkest_utility.Util;
import omega_gameplay.Collidable;
import omega_gameplay.CollidingDrawnObject;
import omega_gameplay.CollisionType;
import omega_world.Area;

/**
 * Ears receive sounds. Only those sounds can be heard that find an ear.
 * 
 * @author Mikko Hilpinen
 * @since 16.10.2014
 */
public class Ear extends CollidingDrawnObject
{
	// ATTRIBUTES	--------------------------------------------------------
	
	private Side side;
	
	private static final Class<?>[] COLLISIONCLASSES = {SoundParticle.class};
	
	
	// CONSTRUCTOR	-----------------------------------------
	
	/**
	 * Creates a new ear to the given position
	 * 
	 * @param x The new x-coordinate of the ear (in pixels)
	 * @param y The new y-coordinate of the ear (in pixels)
	 * @param side On which side of the head this ear is on
	 * @param area The area where the ear resides
	 */
	public Ear(int x, int y, Side side, Area area)
	{
		super(x, y, DepthConstants.NORMAL, true, CollisionType.CIRCLE, area);
		
		// Initializes attributes
		this.side = side;
		
		setCircleCollisionPrecision(1, 1, 1);
		
		// Adds the object to the handler(s)
		area.getCollisionHandler().addCollisionListener(this);
	}
	
	
	// IMPLEMENTED METHODS	-------------------------------

	@Override
	public void onCollision(ArrayList<Double> colpoints, Collidable collided,
			double steps)
	{
		// When the ear collides with a soundParticle, informs it
		if (collided instanceof SoundParticle)
			((SoundParticle) collided).onEarCollision(this);
	}
	
	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		return COLLISIONCLASSES;
	}

	@Override
	public int getHeight()
	{
		return (int) Util.metersToPixels(0.05);
	}

	@Override
	public int getWidth()
	{
		return (int) Util.metersToPixels(0.06);
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws a circle
		g2d.drawOval(0, 0, getWidth(), getHeight());
	}

	@Override
	public int getOriginX()
	{
		return getWidth() / 2;
	}

	@Override
	public int getOriginY()
	{
		return getHeight() / 2;
	}
	
	@Override
	public boolean pointCollides(Point2D absolutepoint)
	{
		if (getSide() == Side.LEFT)
			return super.pointCollides(absolutepoint) && 
					negateTransformations(absolutepoint).getX() < getOriginX();
		
		return super.pointCollides(absolutepoint) && 
				negateTransformations(absolutepoint).getX() > getOriginX();
	}
	
	
	// GETTERS & SETTERS	-------------------------
	
	/**
	 * @return The side of the head the ear is on
	 */
	public Side getSide()
	{
		return this.side;
	}
	
	
	// ENUMERATIONS	---------------------------------
	
	/**
	 * Side tells, which side of the head the ear is on
	 */
	public enum Side
	{
		/**
		 * The left side
		 */
		LEFT, 
		/**
		 * The right side
		 */
		RIGHT;
		
		
		// OTHER METHODS	----------------------------
		
		/**
		 * @return The pan used for playing a sound on this side
		 */
		public double getPan()
		{
			if (this == LEFT)
				return -1;
			else
				return 1;
		}
	}
}
