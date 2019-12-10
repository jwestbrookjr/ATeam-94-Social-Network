package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Interface which outlines implementation of an abstract social network.
 * 
 * @author Sam Peaslee, Cole Christophel, Alex Bush, Jon Westbrook, Grant
 *         Hellenbrand
 *
 */
public interface SocialNetworkADT {

	/**
	 * Parses the contents of the input file to create a SocialNetWork. Stores
	 * users in a graph depending on the contents of the input file.
	 * 
	 * @throws InvalidInputFileFormatException - if input file is not a valid
	 *                                         format.
	 * @throws FileNotFoundException           - if file is not found.
	 */
	public void createSocialNetWork(File file)
			throws InvalidInputFileFormatException, FileNotFoundException;

	/**
	 * Update the contents of the SocialNetwork depending on the command given
	 * from the file used to initially create the SocialNetwork, or when a
	 * change is made via the GUI.
	 * 
	 * @param command - add, remove, or update central user.
	 * @param words   - array of strings containing the name(s) that come after
	 *                the command.
	 * @throws InvalidInputFileFormatException - if input file is not a valid
	 *                                         format.
	 */
	public void updateSocialNetwork(String command, String[] words)
			throws InvalidInputFileFormatException;

	/**
	 * When the SocialNetwork is updated by someone using the GUI, add the
	 * command they used to update the SocialNetwork to the log file. Example:
	 * new user SAM was added to the SocialNetwork the line "a sam" should be
	 * added to the log file.
	 * 
	 * @param update - String array containing line to add to log file.
	 * @param log    - FileWriter to write to the log file.
	 */
	public void updateLogFile(String[] update, FileWriter log)
			throws IOException;

	/**
	 * Updates the log file from a valid input file.
	 * 
	 * @param inputFile - valid input text file.
	 * @param log       - valid log text file.
	 * @throws IOException - if any input file is invalid.
	 */
	public void updateLogFile(File inputFile, FileWriter log)
			throws IOException;

	/**
	 * Get the information stored about the current central user of the
	 * SocialNetwork (name, friends).
	 * 
	 * @return - the GraphNode containing central user's information.
	 */
	public GraphNode getCentralUser();

	/**
	 * Get the graph that holds all info about users in the SocialNetwork.
	 * 
	 * @return graph - that contains user info.
	 */
	public SocialGraph getGraph();
}