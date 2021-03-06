package application;

import java.util.ArrayList;

/**
 * Filename: GraphADT.java 
 * Project: ATeam 94 Social Network 
 * Authors: Deppeler
 * 
 * A simple graph interface (DO NOT edit this file).
 */
public interface GraphADT {

	/**
	 * Add new vertex to the graph.
	 *
	 * If vertex is null or already exists, method ends without adding a vertex
	 * or throwing an exception.
	 * 
	 * Valid argument conditions: 1. vertex is non-null 2. vertex is not already
	 * in the graph.
	 * 
	 * @param vertex - the vertex to be added.
	 */
	public void addVertex(String vertex);

	/**
	 * Remove a vertex and all associated edges from the graph.
	 * 
	 * If vertex is null or does not exist, method ends without removing a
	 * vertex, edges, or throwing an exception.
	 * 
	 * Valid argument conditions: 1. vertex is non-null 2. vertex is not already
	 * in the graph.
	 * 
	 * @param vertex - the vertex to be removed
	 */
	public void removeVertex(String vertex);

	/**
	 * Add the edge from vertex1 to vertex2 to this graph. (edge is directed and
	 * unweighted)
	 * 
	 * If either vertex does not exist, VERTEX IS ADDED and then edge is
	 * created. No exception is thrown.
	 *
	 * If the edge exists in the graph, no edge is added and no exception is
	 * thrown.
	 * 
	 * Valid argument conditions: 1. neither vertex is null 2. both vertices are
	 * in the graph 3. the edge is not in the graph
	 * 
	 * @param vertex1 the first vertex (src)
	 * @param vertex2 the second vertex (dst)
	 */
	public void addEdge(String vertex1, String vertex2);

	/**
	 * Remove the edge from vertex1 to vertex2 from this graph. (edge is
	 * directed and unweighted) If either vertex does not exist, or if an edge
	 * from vertex1 to vertex2 does not exist, no edge is removed and no
	 * exception is thrown.
	 * 
	 * Valid argument conditions: 1. neither vertex is null 2. both vertices are
	 * in the graph 3. the edge from vertex1 to vertex2 is in the graph
	 * 
	 * @param vertex1 the first vertex
	 * @param vertex2 the second vertex
	 */
	public void removeEdge(String vertex1, String vertex2);

	/**
	 * Searches for a specified vertex in the graph and returns the vertex if
	 * it's in the graph; otherwise return null.
	 * 
	 * @param data - data contained in a vertex of the graph.
	 * @return - a GraphNode with the associated data, or null.
	 */
	public GraphNode search(String data);

	/**
	 * This method uses Breadth First Search to traverse a graph starting at one
	 * vertex and looking for another vertex. If the second vertex is found the
	 * search stops. While traversing, the predecessor for each visited vertex is
	 * set to the vertex that it is visited from. If there is a path between the
	 * two vertices, a list of vertices is created using the predecessors set in
	 * the Breadth First Search. This list is returned by the method.
	 * 
	 * If a vertex is not found in the graph or if there is no path between the
	 * two vertices, then null is returned.
	 * 
	 * We used the below website as a reference:
	 * https://www.geeksforgeeks.org/shortest-path-unweighted-graph/
	 * 
	 * @param vertex1 - start vertex of the edge.
	 * @param vertex2 - terminal vertex of the edge.
	 * @return - ArrayList containing vertices in the shortest path, or null.
	 */
	public ArrayList<String> getShortestPath(String vertex1, String vertex2);

	/**
	 * Returns the number of edges in this graph.
	 * 
	 * @return - number of edges in the graph.
	 */
	public int size();

	/**
	 * Returns the number of vertices in this graph.
	 * 
	 * @return - number of vertices in graph.
	 */
	public int order();

}