package application;

/**
 * Filename: GraphNode.java 
 * Project:
 * Authors:
 * 
 * Class to store a vertex in a graph.
 */

import java.util.ArrayList;

public class GraphNode {
    // User Name associated with a GraphNode
    private String userName;
    // Successors of GraphNode
    private ArrayList<String> friends;
    private boolean visited;
    // This field will be used to store the predecessor of a vertex when
    // the shortest path algorithm is implemented in the UndirectedGraph
    // class. A graph can have multiple predecessors, but for the shortest
    // path algorithm you need to specify one predecessor for each vertex
    // visited. Which will be the vertex that its visited right before it
    // See the method getShortestPath() in the UndirectedGraph class for details
    private GraphNode shortestPathPred;


    /**
     * Constructor to create new GraphNode
     * @param data
     */
    GraphNode(String data) {
        this.userName = data;
        friends = new ArrayList<String>();
        visited = false;
    }


    /**
     * Retrieve user name associated with a GraphNode
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Retrieves the ArrayList of friends for the GraphNode
     * @return friends
     */
    public ArrayList<String> getFriends() {
        return friends;
    }

    /**
     * Adds a friend to the GraphNodes friends ArrayList
     * @param friend
     */
    public void addFriend(String friend) {
        friends.add(friend);
    }

    /**
    * Retrieves GraphNode that was visited before the current GraphNode
    * @return shortestPathPred
    */
    public GraphNode getPredecessor() {
        return shortestPathPred;
    }

    /**
     * Set the GraphNode visited before the current GraphNode 
     * @param neighboor
     */
    public void setPredecessor(GraphNode pred) {
        shortestPathPred = pred;
    }

    /**
     * Returns if a GraphNode has been visited or not 
     * @return visited
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Set a GraphNode to visited
     */
    public void setVisited() {
        visited = true;
    }

    /**
     * Set a GraphNode to unvisited 
     */
    public void setUnvisited() {
        visited = false;
    }


}// End of inner GraphNode class