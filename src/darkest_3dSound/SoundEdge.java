package darkest_3dSound;

import darkest_utility.Util;

/**
 * SoundEdges are SoundParticle paths that lead from an SoundSource to an Ear.
 * 
 * @author Mikko Hilpinen
 * @since 16.10.2014
 */
public class SoundEdge
{
	// ATTRIBUTES	--------------------------------------
	
	private int volumeAdjustment;
	private double delaySteps;
	private Ear target;
	private boolean isValid;
	
	
	// CONSTRUCTOR	--------------------------------------
	
	/**
	 * Creates a new SoundEdge between the given target and the given source.
	 * @param startVolume How strong the volume is by default (in desibels, an approximate)
	 * @param volumeLostInCollisions How many desibels of volume were lost in SoundParticle 
	 * collisions
	 * @param pixelsTraveled How many pixels the SoundParticle traveled while creating this 
	 * edge
	 * @param target Which ear the SoundParticle reached 
	 */
	public SoundEdge(int startVolume, int volumeLostInCollisions, double pixelsTraveled, Ear target)
	{
		System.out.println("Sound Edge created");
		
		// Initializes attributes
		this.isValid = true;
		this.delaySteps = pixelsTraveled / Util.SOUND_SPEED_PIXELS_PER_STEP;
		this.target = target;
		this.volumeAdjustment = (int) Util.getVolumeAdjustment(pixelsTraveled) - 
				volumeLostInCollisions;
		
		// Checks if the volume is too large
		if (startVolume + this.volumeAdjustment > Util.MAX_SOUND_VOLUME)
			this.volumeAdjustment = Util.MAX_SOUND_VOLUME - startVolume;
		
		// Checks if the volume is too small
		if (startVolume + this.volumeAdjustment < Util.MIN_SOUND_VOLUME)
			this.isValid = false;
		
		System.out.println("Edge -------");
		System.out.println("Delay: " + Util.stepsToSeconds(this.delaySteps) * 1000 + " ms");
		System.out.println("Delay: " + this.delaySteps + " steps");
		System.out.println("VolumeAdj: " + this.volumeAdjustment + " dB");
		System.out.println("Target side: " + target.getSide());
	}

	
	// GETTERS & SETTERS	--------------------------------------
	
	/**
	 * @return How much the volume of the sound is adjusted by the environment
	 */
	public int getVolumeAdjustment()
	{
		return this.volumeAdjustment;
	}
	
	/**
	 * @return How long it takes for the sound to travel to its destination
	 */
	public double getDelaySteps()
	{
		return this.delaySteps;
	}
	
	/**
	 * @return Which ear the sound reached
	 */
	public Ear getTarget()
	{
		return this.target;
	}
	
	/**
	 * @return Is there any point in using this edge
	 */
	public boolean isValid()
	{
		return this.isValid;
	}
	
	
	// OTHER METHODS	---------------------------------------------
	
	/**
	 * Tells whether this soundEdge is basically identical to another soundEdge
	 * @param another The soundEdge this edge is compared with
	 * @return Are the two edges similar
	 */
	protected boolean isDuplicateWith(SoundEdge another)
	{
		return (this.volumeAdjustment == another.volumeAdjustment && 
				this.target.getSide() == another.target.getSide() &&
				(int) (this.delaySteps) == (int) (another.delaySteps));
	}
}
