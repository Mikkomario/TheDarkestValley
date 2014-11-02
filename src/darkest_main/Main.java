package darkest_main;

import flux_wav.OpenWavSoundBankHolder;
import genesis_graphic.GamePanel;
import genesis_graphic.GameWindow;

import java.awt.BorderLayout;

import darkest_test.BirdMouseTest;
import darkest_utility.Util;
import omega_world.AreaRelay;
import arc_bank.MultiMediaHolder;
import arc_bank.OpenGamePhaseBank;
import arc_bank.OpenGamePhaseBankHolder;
import arc_resource.MetaResource;

/**
 * The main class starts the game
 * 
 * @author Mikko Hilpinen
 * @since 16.10.2014
 */
public class Main
{
	private Main()
	{
		// Initializes resources
		MultiMediaHolder.initializeResourceDatabase(new OpenGamePhaseBankHolder(
				"configuration/gamephaseload.txt"));
		MultiMediaHolder.initializeResourceDatabase(new OpenWavSoundBankHolder(
				"configuration/wavload.txt"));
		MultiMediaHolder.activateBank(MetaResource.GAMEPHASE, "default", true);
		
		// Creates new GameWindow & Panels
		GameWindow window = new GameWindow(640, 480, "The Darkest Valley", true, 
				Util.STEPS_PER_SECOND, 10, false);
		GamePanel panel = new GamePanel(640, 480);
		//panel.disableClear();
		window.addGamePanel(panel, BorderLayout.CENTER);
		
		// Creates areas
		AreaRelay areaRelay = new AreaRelay(window, panel);
		areaRelay.addArea("test", OpenGamePhaseBank.getGamePhaseBank("default").getPhase("test"));
		
		// TODO: Starts the game
		new BirdMouseTest(areaRelay.getArea("test"));
		areaRelay.getArea("test").start();
	}
	
	/**
	 * Starts the game
	 * @param args not used
	 */
	public static void main(String[] args)
	{
		// Starts the program
		new Main();
	}
}
