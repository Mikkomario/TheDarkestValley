package darkest_3dSound;

import genesis_graphic.DepthConstants;

import java.awt.Graphics2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import darkest_utility.Util;
import omega_gameplay.BasicPhysicDrawnObject;
import omega_gameplay.Collidable;
import omega_gameplay.CollisionType;
import omega_world.Area;

/**
 * SoundParticles travel around the area and try to find an ear
 * 
 * @author Mikko Hilpinen
 * @since 16.10.2014
 */
public class SoundParticle extends BasicPhysicDrawnObject
{
	// TODO: Change the collision detection to ear instead
	// TODO: SoundWalls should be in their own collision handler
	
	// ATTRIBUTES	----------------------------------
	
	private int startVolume;
	private double pixelsTraveled, volumeLostOnCollisions;
	private SoundSource source;
	private Ear firstEar;
	
	// TODO: Find a better value here?
	private static final int MAX_DISTANCE = (int) Util.metersToPixels(150);
	
	private static final Class<?>[] COLLISIONCLASSES = {Ear.class};
	
	
	// CONSTRUCTOR	----------------------------------
	
	/**
	 * Creates a new soundParticle to the given position
	 * @param x The x-coordinate of the particle (in pixels)
	 * @param y The y-coordinate of the particle (in pixels)
	 * @param volume How strong is the sound creating the particle? (in desibels)
	 * @param direction The direction the particle will travel to
	 * @param source The source that created this particle
	 * @param area The area where the particle is located at
	 */
	public SoundParticle(int x, int y, int volume, double direction, SoundSource source, 
			Area area)
	{
		super(x, y, DepthConstants.NORMAL, true, CollisionType.CIRCLE, area);
		
		// Initializes attributes
		this.startVolume = volume;
		this.pixelsTraveled = 0;
		this.volumeLostOnCollisions = 0;
		this.source = source;
		this.firstEar = null;
		
		// TODO: The times 2 may not be a good idea, who knows
		setMotion(direction, Util.SOUND_SPEED_PIXELS_PER_STEP * 0.01);
		
		setRadius(getHeight());
		setCircleCollisionPrecision(1, 1, 1);
		//Point2D.Double[] colPoints = {new Point2D.Double(getOriginX(), getOriginY())};
		//setRelativeCollisionPoints(colPoints);
		
		// Adds the particle to the handler(s)
		area.getCollisionHandler().addCollisionListener(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------

	@Override
	public void act(double steps)
	{
		super.act(steps);
		
		// Calculates the traveled distance
		this.pixelsTraveled += getMovement().getSpeed() * steps;
		
		// Increases in size as the distance gets larger
		double scale = this.pixelsTraveled * SoundSource.degreesForParticle / 360.0;
		setScale(scale, scale);
		
		// Checks if there is any point in living anymore
		checkIfShouldDie();
	}
	
	@Override
	public void onCollision(ArrayList<Double> colPoints, Collidable collided, double steps)
	{
		System.out.println("Collides with something");
		
		// On wall collisions, some of the power is lost and the direction changes
		/*
		else if (collided instanceof PhysicalCollidable)
		{
			// TODO: Lose some power and change direction. Change to soundWall
		}
		*/
	}

	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		return COLLISIONCLASSES;
	}

	@Override
	public int getHeight()
	{
		return 6;
	}

	@Override
	public int getWidth()
	{
		return getHeight();
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		drawCollisionArea(g2d);
		drawCollisionPoints(g2d);
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
	
	
	// OTHER METHODS	----------------------------------
	
	/**
	 * This method should be called when the particle collides with an ear
	 * @param ear The ear the particle collided with
	 */
	protected void onEarCollision(Ear ear)
	{
		// If the particle has already collided with this ear, doesn't react
		if (ear.equals(this.firstEar) || isDead())
			return;
		
		//System.out.println("Particle: " + this);
		this.source.addEdge(new SoundEdge(this.startVolume, 
				(int) this.volumeLostOnCollisions, this.pixelsTraveled, ear));
		
		// If the particle collided with both ears, dies
		if (this.firstEar != null)
			kill();
		else
			this.firstEar = ear;
	}
	
	private void checkIfShouldDie()
	{
		if (this.startVolume - this.volumeLostOnCollisions < Util.MIN_SOUND_VOLUME || 
				this.pixelsTraveled > MAX_DISTANCE)
		{
			//System.out.println("Particle died without finding an ear");
			kill();
		}
	}
}
