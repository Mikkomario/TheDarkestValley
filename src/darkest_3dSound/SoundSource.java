package darkest_3dSound;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import flux_wav.WavSound;
import genesis_logic.ActorHandler;
import omega_gameplay.HelpMath;
import omega_world.Area;
import omega_world.GameObject;
import timers.SingularTimer;
import timers.TimerEventListener;

/**
 * SoundSources are able to create SoundParticles and play wav sounds.
 * 
 * @author Mikko Hilpinen
 * @since 16.10.2014
 */
public class SoundSource extends GameObject implements TimerEventListener
{
	// ATTRIBUTES	--------------------------------------
	
	private Point2D.Double position;
	private int fanWidth, defaultSoundVolume;
	private boolean needsNewEdges, mayContainDuplicates;
	private ArrayList<SoundEdge> edges;
	private double direction;
	private Area area;
	private WavSound lastSound;
	
	/**
	 * How many degrees each particle covers
	 */
	protected static int degreesForParticle = 10;
	
	private static int soundPreparationDuration = 100;
	
	
	// CONSTRUCTOR	--------------------------------------
	
	/**
	 * Creates a soundSource to the given position. The Source sends the sound to a given 
	 * direction. If you want the sound to be heard from any direction, fanWidthDegrees should 
	 * be 360.
	 * 
	 * @param position The position the soundSource resides at (in pixels)
	 * @param fanWidthDegrees How wide the fan is (in degrees). The fan determines from which 
	 * direction the sound can be heard from. [1, 360]
	 * @param direction The direction the soundSource faces
	 * @param defaultSoundVolume How loud sounds are played through this source
	 * @param area The area where the soundSource resides
	 */
	public SoundSource(Point2D.Double position, int fanWidthDegrees, double direction, 
			int defaultSoundVolume, Area area)
	{
		super(area);
		
		// Initializes attributes
		this.position = position;
		this.fanWidth = fanWidthDegrees;
		this.direction = HelpMath.checkDirection(direction);
		this.needsNewEdges = true;
		this.edges = new ArrayList<SoundEdge>();
		this.defaultSoundVolume = defaultSoundVolume;
		this.area = area;
		this.lastSound = null;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------
	
	@Override
	public void kill()
	{
		this.edges.clear();
		super.kill();
	}
	
	@Override
	public void activate()
	{
		// Can't be activated or deactivated
	}

	@Override
	public void inactivate()
	{
		// Can't be activated or deactivated
	}

	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public void onTimerEvent(int timerid)
	{
		// Starts playing a sound after preparation
		playSoundWithDelay(this.lastSound);
		this.lastSound = null;
	}
	
	
	// GETTERS & SETTERS	---------------------------------------
	
	/**
	 * @return The position the sound originates from
	 */
	public Point2D.Double getPosition()
	{
		return this.position;
	}
	
	/**
	 * Changes the position the sound originates from
	 * @param newPosition The new position the sound originates from
	 */
	public void setPosition(Point2D.Double newPosition)
	{
		if (getPosition().equals(newPosition))
			return;
		
		this.position = newPosition;
		this.needsNewEdges = true;
	}
	
	/**
	 * Changes the direction the source is facing
	 * @param newDirection The new direction the source is facing
	 */
	public void setDirection(double newDirection)
	{
		if (this.direction == newDirection)
			return;
		
		this.direction = newDirection;
		this.needsNewEdges = true;
	}
	
	/**
	 * Changes the direction the source is facing
	 * @param degrees How much the source's direction is changed
	 */
	public void rotate(double degrees)
	{
		setDirection(this.direction + degrees);
	}
	
	
	// OTHER METHODS	------------------------------------------
	
	/**
	 * Prepares the source to play a sound. This will allow the sound to be played immediately 
	 * after playSound is called, unless there are some changes in between, of course.
	 */
	public void prepareForSound()
	{
		// If no preparation is required, no preparation will be get
		if (!this.needsNewEdges)
			return;
		
		System.out.println("Preparing");
		
		// Removes the old edges
		this.edges.clear();
		
		// Creates new edges using soundParticles
		for (double angle = this.direction - this.fanWidth / 2; 
				angle < this.direction + this.fanWidth / 2; angle += degreesForParticle)
		{
			new SoundParticle((int) getPosition().getX(), (int) getPosition().getY(), 
					this.defaultSoundVolume, angle, this, this.area);
		}
		
		this.needsNewEdges = false;
	}
	
	/**
	 * Plays a wavSound from the source. There may be a slight delay if the source hasn't 
	 * been allowed to prepare for the sound beforehand.
	 * @param sound The sound that will be played.
	 */
	public void playSound(WavSound sound)
	{
		// If the source is not ready yet, has to take some time to prepare
		if (this.needsNewEdges)
		{
			this.lastSound = sound;
			prepareForSound();
			new SingularTimer(this, soundPreparationDuration, 0, this.area.getActorHandler());
		}
		
		// Otherwise playes the sound after a short delay
		else
			playSoundWithDelay(sound);
	}
	
	/**
	 * Adds a new edge to the list of used edges (only valid edges are accepted)
	 * @param edge The edge that will be added
	 */
	protected void addEdge(SoundEdge edge)
	{
		if (edge.isValid())
		{
			this.edges.add(edge);
			this.mayContainDuplicates = true;
		}
	}
	
	private void playSoundWithDelay(WavSound sound)
	{
		// Removes duplicates if necessary
		removeDuplicateEdges();
		
		System.out.println("Plays " + this.edges.size() + " sounds");
		for (SoundEdge edge : this.edges)
		{
			// TODO: The delay is nonexistant
			new DelaySoundPlayer((int) (edge.getDelaySteps() * 100), this.area.getActorHandler(), 
					sound, edge.getVolumeAdjustment(), edge.getTarget().getSide().getPan());
		}
	}
	
	private void removeDuplicateEdges()
	{
		if (!this.mayContainDuplicates)
			return;
			
		ArrayList<SoundEdge> duplicates = new ArrayList<SoundEdge>();
		
		for (int i = 0; i < this.edges.size(); i++)
		{
			SoundEdge possiblyDuplicate = this.edges.get(i);
			for (int comparedIndex = 0; comparedIndex < i; comparedIndex++)
			{
				if (possiblyDuplicate.isDuplicateWith(this.edges.get(comparedIndex)))
				{
					duplicates.add(possiblyDuplicate);
					break;
				}
			}
		}
		
		System.out.println("Found " + duplicates.size() + " duplicates");
		this.edges.removeAll(duplicates);
		this.mayContainDuplicates = false;
	}
	
	
	// SUBCLASSES	----------------------------------------
	
	private class DelaySoundPlayer implements TimerEventListener
	{
		// ATTRIBUTES	------------------------------------
		
		private WavSound sound;
		private double volumeAdjustment, pan;
		
		
		// CONSTRUCTOR	------------------------------------
		
		/**
		 * Creates a new soundPlayer that will play the sound after a slight delay
		 * 
		 * @param delay How many steps are waited before the sound is played
		 * @param actorhandler The actorHandler that will inform the object about 
		 * passing of time
		 * @param sound The sound that will be played
		 * @param volumeAdjustment How much the sound's volume is adjusted
		 * @param pan How much the sound is panned
		 */
		public DelaySoundPlayer(int delay, ActorHandler actorhandler, WavSound sound, 
				double volumeAdjustment, double pan)
		{
			// Initializes methods
			this.sound = sound;
			this.volumeAdjustment = volumeAdjustment;
			this.pan = pan;
			
			new SingularTimer(this, delay, 0, actorhandler);
		}

		
		// IMPLEMENTED METHODS	------------------------------
		
		@Override
		public void onTimerEvent(int timerid)
		{
			//System.out.println("Sound playing");
			
			// Plays the sound
			this.sound.play((float) this.volumeAdjustment, (float) this.pan, null);
		}

		@Override
		public void activate()
		{
			// Can't be activated
		}

		@Override
		public void inactivate()
		{
			// Can't be deactivated
		}

		@Override
		public boolean isActive()
		{
			return true;
		}

		@Override
		public boolean isDead()
		{
			return SoundSource.this.isDead();
		}

		@Override
		public void kill()
		{
			// Can't be killed by itself
		}
	}
}
