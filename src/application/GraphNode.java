package application;

/**
 * Filename: GraphNode.java 
 * Project: ATeam 94 Social Network
 * Authors: Sam Peaslee, Jon Westbrook, Grant Hellenbrand, Cole Christophel, Alex Bush
 * 
 * Class to store a vertex in a graph.
 */

import java.util.ArrayList;

/**
 * Class which represents a node in a graph.
 * 
 * @author Sam Peaslee
 *
 */
public class GraphNode {
	// User Name associated with a GraphNode.
	private String userName;
	// Successors of GraphNode.
	private ArrayList<String> friends;
	// Flag to track whether a Graphnode has been visited.
	private boolean visited;
	// This field will be used to store the predecessor of a vertex when
	// the shortest path algorithm is implemented in the SocialGraph
	// class. A GraphNode can have multiple predecessors, but for the shortest
	// path algorithm, one needs to specify one predecessor for each vertex
	// visited, which will be the vertex that is visited right before it.
	// See the method getShortestPath() in the SocialGraph class for details.
	private GraphNode shortestPathPred;

	/**
	 * Constructor to create new GraphNode.
	 * 
	 * @param data - data to store in this GraphNode.
	 */
	GraphNode(String data) {
		this.userName = data;
		friends = new ArrayList<String>();
		visited = false;
	}

	/**
	 * Retrieve user name associated with a GraphNode.
	 * 
	 * @return - userName stored in this GraphNode.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Retrieves the ArrayList of friends for the GraphNode.
	 * 
	 * @return - ArrayList of friends stored in this GraphNode.
	 */
	public ArrayList<String> getFriends() {
		return friends;
	}

	/**
	 * Adds a friend to the GraphNode's friends ArrayList.
	 * 
	 * @param friend - friend to add to this GraphNode's friends list.
	 */
	public void addFriend(String friend) {
		friends.add(friend);
	}

	/**
	 * Retrieves GraphNode that was visited before the current GraphNode.
	 * 
	 * @return - GraphNode that is the predecessor to this GraphNode.
	 */
	public GraphNode getPredecessor() {
		return shortestPathPred;
	}

	/**
	 * Set the GraphNode visited before the current GraphNode.
	 * 
	 * @param pred - GraphNode visited before this GraphNode.
	 */
	public void setPredecessor(GraphNode pred) {
		shortestPathPred = pred;
	}

	/**
	 * Checks whether a GraphNode has been visited.
	 * 
	 * @return - true if this GraphNode has been visited, false otherwise.
	 */
	public boolean isVisited() {
		return visited;
	}

	/**
	 * Set a GraphNode's visited field to true.
	 */
	public void setVisited() {
		visited = true;
	}

	/**
	 * Set a GraphNode's visited field to false.
	 */
	public void setUnvisited() {
		visited = false;
	}
}