package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Class which represents a social network.
 * 
 * @author Sam Peaslee, Grant Hellenbrand, Alex Bush, Cole Christophel, Jon
 *         Westbrook
 *
 */
public class SocialNetwork implements SocialNetworkADT {

	// Graph that hold users in the SocialNetwork.
	private SocialGraph graph;

	// Central user of the SocialNetwork (user to be displayed).
	private GraphNode centralUser;

	/**
	 * Constructor to create a SocialNetwork object.
	 */
	public SocialNetwork() {
		graph = new SocialGraph();
	}

	/**
	 * Get the graph that holds all info about users in the SocialNetwork.
	 * 
	 * @return graph - that contains user info.
	 */
	public SocialGraph getGraph() {
		return graph;
	}

	/**
	 * Set the graph.
	 * 
	 * @return graph - that holds user information stored in SocialNetwork.
	 */
	public void setGraph(SocialGraph graph) {
		this.graph = graph;
	}

	/**
	 * Get the information stored about the current central user of the
	 * SocialNetwork (name, friends).
	 * 
	 * @return - GraphNode containing central user's information.
	 */
	public GraphNode getCentralUser() {
		return centralUser;
	}

	/**
	 * Set central user.
	 */
	public void setCentralUser(GraphNode cu) {
		centralUser = cu;
	}

	/**
	 * Parses the contents of the input file to create a SocialNetWork. Stores
	 * users in a graph depending on the contents of the input file.
	 * 
	 * @throws InvalidInputFileFormatException - if input file does not valid
	 *                                         format.
	 * @throws FileNotFoundException           - if file is not found.
	 */
	public void createSocialNetWork(File inputFile)
			throws InvalidInputFileFormatException, FileNotFoundException {
		Scanner scn = new Scanner(inputFile);
		// Read the contents of the input file that will create the initial
		// social network.
		while (scn.hasNextLine()) {
			// Get current line of file.
			String currentLine = scn.nextLine();
			// Split into String array containing the words in the line.
			String[] words = currentLine.split(" ");
			// Get the first word in the line which is the command.
			String command = words[0];
			// Creates the social network.
			updateSocialNetwork(command, words);
		}
		scn.close();
	}

	/**
	 * Update the contents of the SocialNetwork depending on the command given
	 * from the file used to initially create the SocialNetwork or when a change
	 * is made via the GUI.
	 * 
	 * @param command - add, remove, or set central user.
	 * @param words   - array of strings containing the name(s) that come after
	 *                the command.
	 * @throws InvalidInputFileFormatException - if input file is not valid
	 *                                         format.
	 */
	public void updateSocialNetwork(String command, String[] cmdLine)
			throws InvalidInputFileFormatException {
		if (command.equals("a")) {
			// Add a user or create a new friendship
			// between users.
			if (cmdLine.length == 2) {
				graph.addVertex(cmdLine[1]);
			} else if (cmdLine.length == 3) {
				graph.addEdge(cmdLine[1], cmdLine[2]);
			} else {
				throw new InvalidInputFileFormatException();
			}
		} else if (command.equals("r")) {
			// Remove a user and all users' friendships,
			// or remove a friendship between two users.
			if (cmdLine.length == 2) {
				graph.removeVertex(cmdLine[1]);
			} else if (cmdLine.length == 3) {
				graph.removeEdge(cmdLine[1], cmdLine[2]);
			} else {
				throw new InvalidInputFileFormatException();
			}
		} else if (command.equals("s")) {
			if (cmdLine.length == 2) {
				// System.out.println(cmdLine[1]);
				setCentralUser(graph.search(cmdLine[1]));
			} else {
				throw new InvalidInputFileFormatException();
			}
		} else {
			throw new InvalidInputFileFormatException();
		}
	}

	/**
	 * Copies contents of a text file line by line to the log file. Used when a
	 * file is used to update the Social Network.
	 * 
	 * @param inputFile - input file to parse.
	 * @param log       - new log file created.
	 * @throws IOException - if any error occurs retrieving a file.
	 */
	public void updateLogFile(File inputFile, FileWriter log)
			throws IOException {
		// Copy contents of input file to log file
		Scanner scn = new Scanner(inputFile);
		while (scn.hasNextLine()) {
			log.write(scn.nextLine() + "\n");
		}
		scn.close();

	}

	/**
	 * When the SocialNetwork is updated by someone using the GUI, add the
	 * command they used to update the SocialNetwork to the log file. Example:
	 * new user SAM was added to the SocialNetwork. The line "a sam" should be
	 * added to the log file.
	 * 
	 * @param update - updates to add to the log.
	 * @param log    - log to write log information in.
	 * @throws IOException - if any error occurs retrieving a file.
	 */
	public void updateLogFile(String[] update, FileWriter log)
			throws IOException {
		String line = "";
		for (int i = 0; i < update.length; i++) {
			if (i == 0) {
				line = update[i];
			} else if (i == update.length - 1) {
				line = line + " " + update[i] + "\n";
			} else {
				line = line + " " + update[i];
			}
		}
		log.write(line);
	}

	/**
	 * Main method for testing and debugging only.
	 * 
	 * @param args - unused.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

	}
}
