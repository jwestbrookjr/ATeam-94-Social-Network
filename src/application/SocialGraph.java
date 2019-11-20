/**
 * Filename:   SocialGraph.java
 * Project:    ATeam 94 Group Project
 * Authors:    Jon Westbrook (jwestbrook@wisc.edu)
 * 
 * Semester:   Fall 2019
 * Course:     CS400
 * 
 * Due Date:   Before 11:59pm on 12/03/19.
 * Version:    1.0
 * 
 * Credits: 
 * 
 * Bugs:       
 */

package application;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Graph class which implements a undirected, unweighted graph.
 * 
 * @author Jon Westbrook
 *
 */
public class SocialGraph implements GraphADT {
	// Number of edges in this graph.
	private int size;
	// Number of vertices in this graph.
	private int order;
	// List of vertices in this graph.
	private ArrayList<VertexNode> vertexList;

	private class VertexNode {
		// Name of this vertex.
		private String name;
		// List of successors (directed edges).
		private ArrayList<VertexNode> successors;

		/**
		 * Constructor for a vertex.
		 * 
		 * @param name - name of this vertex.
		 */
		private VertexNode(String name) {
			this.name = name;
			successors = new ArrayList<>();
		}

		/**
		 * Method which returns the name of this vertex.
		 * 
		 * @return - String name of this vertex.
		 */
		private String getName() {
			return name;
		}

		/**
		 * Method which confirms a vertex exists in the successors list.
		 * 
		 * @param name - name of the vertex to search for in the successors
		 *             list.
		 * @return - true if vertex is in successors list, false otherwise.
		 */
		private boolean isSuccessor(String name) {
			if (successors.contains(getVertexByName(name))) {
				return true;
			} else {
				return false;
			}
		}

		/**
		 * Method which returns vertex that is a successor of this vertex,
		 * searched by its name.
		 * 
		 * @param name - name of the vertex to search for in the successors
		 *             ArrayList.
		 * @return - VertexNode with matching name, or null if it is not found.
		 */
		private VertexNode getSuccessor(String name) {
			// If name is null, return null.
			if (name == null) {
				return null;
			}

			// If vertex is not a successor, return null, else return the vertex
			// with the matching name.
			if (!isSuccessor(name)) {
				return null;
			} else if (isSuccessor(name)) {
				return getVertexByName(name);
			}

			return null;
		}

		/**
		 * Method which adds a successor (and therefore a directed edge) from
		 * this vertex to another vertex.
		 */
		private void addSuccessor(String name) {
			// If name is null, return without doing anything.
			if (name == null) {
				return;
			}

			// If vertex does not exist, create and add a new vertex, then
			// return without doing anything further.
			if (getVertexByName(name) == null) {
				successors.add(new VertexNode(name));
				return;
			}

			// Add a new already existing successor.
			successors.add(getVertexByName(name));
		}

		/**
		 * Method which removes a specified vertex from this vertex's successors
		 * list.
		 * 
		 * @param name - name of vertex to remove from this vertex's successors
		 *             list.
		 * @return - true if vertex is removed, false otherwise.
		 */
		private boolean deleteSuccessor(String name) {
			// If name of vertex is null, return false.
			if (name == null) {
				return false;
			}

			// If vertex does not exist, return false.
			if (getVertexByName(name) == null) {
				return false;
			}

			// If vertex is a successor of this vertex, remove it and return
			// true.
			if (successors.contains(getVertexByName(name))) {
				successors.remove(getVertexByName(name));
				return true;
			}

			return false;
		}

		/**
		 * Method which deletes all successors (and therefore edges) from this
		 * vertex.
		 */
		private void clearSuccessors() {
			successors = null;
		}
	}

	/**
	 * Method which finds a vertex in the graph by its name.
	 * 
	 * @param name - name of the vertex for which to search in the graph.
	 * @return - vertex with the matching name.
	 */
	private VertexNode getVertexByName(String name) {
		// Iterate through the vertex list and return the node with the matching
		// name.
		for (int i = 0; i < vertexList.size(); ++i) {
			if (vertexList.get(i).getName().equals(name)) {
				return vertexList.get(i);
			}
		}
		// Return null if no such vertex is found.
		return null;
	}

	/*
	 * Default no-argument constructor.
	 */
	public SocialGraph() {
		this.size = 0;
		this.order = 0;
		vertexList = new ArrayList<>();
	}

	/**
	 * Method which adds a new vertex to the graph. If vertex is null or already
	 * exists in the graph, method returns without doing anything. Otherwise, a
	 * new vertex is created and added.
	 * 
	 * @param vertex - vertex to add to the graph.
	 */
	public void addVertex(String vertex) {
		// If vertex is null, return without doing anything.
		if (vertex == null) {
			return;
		}

		// Checks the vertex list for the vertex to be added, and returns
		// without doing anything if it exists in the list.
		else if (vertexList.contains(getVertexByName(vertex))) {
			return;
		}

		// Else adds the vertex to the list of vertices, then increments order
		// count.
		else {
			vertexList.add(new VertexNode(vertex));
			this.order++;
		}
	}

	/**
	 * Method to remove a vertex and all associated edges from the graph.
	 * 
	 * If vertex is null or does not exist, method ends without removing a
	 * vertex, edges, or throwing an exception.
	 * 
	 * Valid argument conditions: 1. vertex is non-null 2. vertex is already in
	 * the graph.
	 * 
	 * @param vertex - vertex to remove from the graph.
	 */
	public void removeVertex(String vertex) {
		// If vertex is null, return without doing anything.
		if (vertex == null) {
			return;
		}

		// Checks through the vertex list and returns nothing if the vertex
		// is not found.
		if (!vertexList.contains(getVertexByName(vertex))) {
			return;
		}

		// Removes all edges from this vertex prior to its removal.
		getVertexByName(vertex).clearSuccessors();
		// Removes vertex from the list of vertices, then decrements order
		// count.
		vertexList.remove(getVertexByName(vertex));
		this.order--;
	}

	/**
	 * Method to add the edge from vertex1 to vertex2 to this graph (edge is
	 * directed and unweighted). If either vertex does not exist, add vertex,
	 * and add edge, and no exception is thrown. If the edge exists in the
	 * graph, no edge is added and no exception is thrown.
	 * 
	 * Valid argument conditions: 1. neither vertex is null 2. both vertices are
	 * in the graph 3. the edge is not in the graph
	 */
	public void addEdge(String vertex1, String vertex2) {
		// If either vertex is null, return without doing anything.
		if (vertex1 == null || vertex2 == null) {
			return;
		}

		// Confirms vertex1 exists in the vertexList; if not, adds it.
		if (!vertexList.contains(getVertexByName(vertex1))) {
			addVertex(vertex1);
		}
		// Confirms vertex2 exists in the vertexList; if not, adds it.
		if (!vertexList.contains(getVertexByName(vertex2))) {
			addVertex(vertex2);
		}

		// If edge exists in this graph, return without doing anything.
		if (getVertexByName(vertex1).getSuccessor(vertex2) != null) {
			return;
		}

		// Adds an edge to this graph, then checks to confirm no cycle has been
		// created by doing so.
		getVertexByName(vertex1).addSuccessor(vertex2);
		this.size++;
	}

	/**
	 * Method which removes the edge from vertex1 to vertex2 from this graph
	 * (edge is directed and unweighted). If either vertex does not exist, or if
	 * an edge from vertex1 to vertex2 does not exist, no edge is removed and no
	 * exception is thrown.
	 * 
	 * Valid argument conditions: 1. neither vertex is null 2. both vertices are
	 * in the graph 3. the edge from vertex1 to vertex2 is in the graph.
	 * 
	 * @param vertex1 - starting vertex of an edge.
	 * @param vertex2 - terminating vertex of an edge.
	 */
	public void removeEdge(String vertex1, String vertex2) {
		// If either vertex is null, return without doing anything.
		if (vertex1 == null || vertex2 == null) {
			return;
		}

		// If either vertex does not exist in the vertex list, return without
		// doing anything.
		if (!vertexList.contains(getVertexByName(vertex1))
				|| !vertexList.contains(getVertexByName(vertex2))) {
			return;
		}

		// If edge from vertex1 to vertex2 does not exist, return without doing
		// anything.
		if (getVertexByName(vertex1).getSuccessor(vertex2) == null) {
			return;
		}

		// If vertex2 is a successor of vertex1 (and therefore has an edge to it
		// from vertex1), delete this edge, then decrements size.
		if (getVertexByName(vertex1).successors
				.contains(getVertexByName(vertex2))) {
			getVertexByName(vertex1).deleteSuccessor(vertex2);
			this.size--;
		}
	}

	/**
	 * Method which returns a set of all vertices in the graph.
	 */
	public Set<String> getAllVertices() {
		// Create a new set to return with all vertices added in an order.
		Set<String> allVertices = new LinkedHashSet<String>();
		// Iterate through the graph, adding all vertices to the set.
		for (int i = 0; i < vertexList.size(); ++i) {
			allVertices.add(vertexList.get(i).getName());
		}

		return allVertices;
	}

	/**
	 * Method which returns a set of all adjacent vertices of a single vertex.
	 */
	public List<String> getAdjacentVerticesOf(String vertex) {
		List<String> adjacentVertices = new ArrayList<String>();

		// Iterates through the successor list stored in a vertex and adds the
		// name of the vertex to the list of adjacent vertices.
		for (int i = 0; i < getVertexByName(vertex).successors.size(); ++i) {
			adjacentVertices
					.add(getVertexByName(vertex).successors.get(i).getName());
		}

		return adjacentVertices;
	}

	/**
	 * Method which returns the number of edges in this graph.
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Method which returns the number of vertices in this graph.
	 */
	public int order() {
		return this.order;
	}
}
