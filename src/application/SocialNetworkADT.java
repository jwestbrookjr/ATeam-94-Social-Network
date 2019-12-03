package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Interface which outlines implementation of an abstract social network.
 * 
 * @author Sam Peaslee
 *
 */
public interface SocialNetworkADT {

    /**
     * Parses the contents of the input file to create a SocialNetWork.
     * Stores users in a graph depending on the contents of the input file 
     * @throws InvalidInputFileFormatException - if input file does not valid format
     * @throws FileNotFoundException - if file is not found
     */
    public void createSocialNetWork(File file )
        throws InvalidInputFileFormatException, FileNotFoundException;
  
    /**
     * Update the contents of the SocialNetwork depending on the command given 
     * from the file used to initially create the SocialNetwork or when a change 
     * is made via the GUI
     *  if: command == a
     *      if: 1 name after 
     *          add user to graph
     *      else if: 2 names after 
     *          add edge(friendship) between two users
     *      else throw InvalidInputFileFormatException
     *  else if: command == 'r'
     *      if: 1 name after 
     *          remove user(and his friendships) from graph
     *      else if: 2 names after
     *          remove edge(friendship) between the two users 
     *      else throw InvalidInputFileFormatException
     *  else if: command == 's'
     *      if: 1 name 
     *          set user to central user in the SocialNetwork
     *          else throw InvalidInputFileFormatException          
     *  else throw InvalidInputFileFormatException    
     * 
     * @param command -  add, remove or central user
     * @param words - array of strings containing then name(s) that come
     * after the command 
     * @throws InvalidInputFileFormatException - if input file does not valid format
     */
    public void updateSocialNetwork(String command, String[] words)
        throws InvalidInputFileFormatException; 
    
    /**
     * Create a new file to store the log for the SocialNetwork and copy
     * the contents of the input file used to create the SocialNetwork to the
     * new log file 
     */
    public void createLogfile(String inputFileName) throws IOException;
    
    
    /**
     * When the SocialNetwork is updated by someone using the GUI add the 
     * command they used to update the SocialNetwork to the log file 
     * Example:
     *      new user SAM was added to the SocialNetwork
     *      the line "a sam" should be added to the log file 
     * @param logFileName - name of log file 
     */
    public void updateLogFile(String logFileName);
    
    /**
     * Get the information stored about the current central user of the 
     * SocialNetwork(name, friends)
     * @return GraphNode contain central user's information 
     */
    public GraphNode getCentralUser();
    
    /**
     * Get the graph that holds all info about users in the SocialNetwork 
     * @return graph- that contains user info 
     */
    public SocialGraph getGraph();
}