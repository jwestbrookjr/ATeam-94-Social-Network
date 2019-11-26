package application;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Filename:   UndirectedGraph.java
 * Project:    
 * Authors:    
 * 
 * Undirected and unweighted graph implementation
 */

public class SocialGraph implements GraphADT{

    
    //List of GraphNodes in the graph
    ArrayList<GraphNode> vertices  =
        new ArrayList<GraphNode>(0);
    // Set to hold all users names  in the graph
    Set<String> allUserNames =  new HashSet<>();
    int numVertices = 0;//Number of Vertices
    int numEdges = 0;// Number of edges 

    /*
     * Default no-argument constructor
     */ 
    public SocialGraph() {

    }
///////////////////////////////////////////////////////////////////////////////

    /**
     * Add new vertex to the graph.
     *
     * If vertex is null or already exists,
     * method ends without adding a vertex or 
     * throwing an exception.
     * 
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph 
     */
    public void addVertex(String vertex) {
        //Vertex is null do not add
        if(vertex == null) {
            return;}
        //Vertex is already in the graph do no add
        if(search(vertex) != null) return;
        //Vertex is not in graph so add it
        GraphNode v = new GraphNode(vertex);
        vertices.add(v);
        allUserNames.add(vertex);
        numVertices++;
        
    }

    /**
     * Remove a vertex and all associated 
     * edges from the graph.
     * 
     * If vertex is null or does not exist,
     * method ends without removing a vertex, edges, 
     * or throwing an exception.
     * 
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph 
     */
    public void removeVertex(String vertex) {
        //Vertex is null do not add
        if(vertex == null) return;
        GraphNode vRemoved = search(vertex);
        //Vertex is not in the graph do not remove anything
        if(vRemoved == null) return;
        //Go through list of edges for each vertex in graph
        //If vertex has a edge with vRemoved, removed the edge from 
        //the graph
        List<String> n;
        for(GraphNode v: vertices) {
            n = v.getFriends();
            if(n.contains(vertex)) {
                n.remove(vertex);
                numEdges--;
            }
        }
        //Remove vertex from graph 
        vertices.remove(vRemoved);
        allUserNames.remove(vertex);
        numVertices--;
  
    }

    /**
     * Add the edge from vertex1 to vertex2
     * to this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * add vertex, and add edge, no exception is thrown.
     * If the edge exists in the graph,
     * no edge is added and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge is not in the graph
     */
    public void addEdge(String vertex1, String vertex2) {
        //Do not add edge if either vertex is null
        if(vertex1 == null | vertex2 == null) return;
        GraphNode v1 = search(vertex1);
        GraphNode v2 = search(vertex2);
        if(v1 == null) {      
            addVertex(vertex1);
            v1 = search(vertex1);
        }
        if(v2 == null) {
            addVertex(vertex2);
            v2 = search(vertex2);
        }
        //Iterate through adjacent nodes for v1
        //If v2 is already an adjacent node the do not add new edge
       for(String n: v1.getFriends()){ 
           if(n.equals(vertex2)) {
               return;
           }
       }
       // No edge exist between v1 and v2 so new edge can be created
       // Undirected Graph add v2 to adjacency list of v1
       // Undirected Graph add v1 to adjacency list of v2
       v1.addFriend(vertex2);
       v2.addFriend(vertex1);
       numEdges++;
       
    }
    /**
     * Remove the edge from vertex1 to vertex2
     * from this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * or if an edge from vertex1 to vertex2 does not exist,
     * no edge is removed and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge from vertex1 to vertex2 is in the graph
     */
    public void removeEdge(String vertex1, String vertex2) {
        GraphNode v1 = search(vertex1);
        GraphNode v2 = search(vertex2);
        //IF either vertexx is not in Graph do not remove anything 
        if(v1 == null | v2 == null) return;
        // Get list of adjacent vertices of vertex1
        // If vertex2 is in the list remove it and 
        // decrease the number of edges in the graph
        List<String> neighborsV1 = v1.getFriends();
        List<String> neighborsV2 = v2.getFriends();
        if(neighborsV1.contains(vertex2) & neighborsV1.contains(vertex2)) {
            neighborsV1.remove(vertex2);
            neighborsV2.remove(vertex1);
            numEdges--;
        }
        
    }   

    /**
     * Returns a Set that contains all the vertices
     * 
     */
    public Set<String> getAllUsers() {
        return allUserNames;
    }

    /**
     * Returns the number of edges in this graph.
     */
    public int size() {
        return numEdges;
    }

    /**
     * Returns the number of vertices in this graph.
     */
    public int order() {
        return numVertices;
    }
    
////////////////////////////////////////////////////////////////////////////////
   // CODE TO GET THE SHORTED PATH BETWEEN TO VERTICES IN  AN UNDIRECTED GRAPH
    
    /*
     * Mark all nodes as unvisited and predecessor for 
     * shortest path algorithm to null.
     */
    private void initailizeFields() {
        for(GraphNode vertex: vertices) {
           vertex.setUnvisited();
           vertex.setPredecessor(null);
        }
    }
    
    /**
     * This method uses Breadth First Search to traverse a graph starting at one
     * vertex and looking for another vertex. If the second vertex is
     * found the search stops. While traversing the predecessor for each visited 
     * vertex is set to the vertex that it is visited from.
     * If there is a path between the two vertices a list of vertices is created
     * using the predecessors set in the Breadth First Search 
     * This list is returned by the method
     * 
     * If a vertex is not found in the graph or if there is no path between the 
     * two vertices then null is returned 
     * 
     * I used the below website as a reference 
     * https://www.geeksforgeeks.org/shortest-path-unweighted-graph/
     * 
     * @param vertex1 
     * @param vertex2
     * @return ArrayList containing vertices in the shortest path or null
     */
    public ArrayList<String> getShortestPath(String vertex1, String vertex2){
        
        boolean isPath = false;
        GraphNode v1 = search(vertex1);
        GraphNode v2 = search(vertex2);
        if(v1 == null || v2 == null) {
            //If a vertex is not in the graph return null
            return null;
        }
                
        // Mark all vertices unvisited and set their predecessor to null 
        initailizeFields();
        // Queue to implement Breadth First Search
        Queue<GraphNode> q = new LinkedList<GraphNode>();
        // Mark v2 visited and add to queue 
        v2.setVisited();
        q.add(v2);
        // Normal Breadth First Search, with a little extra code to properly
        // set the correct predecessor for each vertex and stop searching if
        // v1 is reached
        while(!q.isEmpty()) {
            GraphNode current = q.poll();
            for(String k: current.getFriends()) {
                GraphNode f = search(k);
                if(!f.isVisited()) {
                    //Set the vertex as visited
                    f.setVisited();
                    // Set it's predecessor to the vertex that its visited from 
                    f.setPredecessor(current);
                    // Add vertex to queue 
                    q.add(f);
                    // Exit the while loop if v1 is reached 
                    // There is a path so set isPath to TRUE
                    if(f == v1) {
                        isPath = true;
                        break;
                    }
                }
            }
        }
           
        if (isPath) {
            // List to store the names of users in the shortest path 
            // from v1 to v2
            ArrayList<String> shortestPath = new ArrayList<>();
            // Add name at v1 to list 
            shortestPath.add(v1.getUserName());
            // Add name at v1's predecessor to list 
            shortestPath.add(v1.getPredecessor().getUserName());
            // New queue to get correct order of the shortest path 
            Queue<GraphNode> q2 = new LinkedList<>();
            // Add v1's predecessor to the queue
            q2.add(v1.getPredecessor());
            while (!q2.isEmpty()) {
                // Grab and remove the front of the queue 
                GraphNode n = q2.poll();
                // If the vertex has a predecessor
                // Add the predecessor to the shortestPath list 
                // and to the queue
                if (n.getPredecessor() != null) {
                    shortestPath.add(n.getPredecessor().getUserName());
                    q2.add(n.getPredecessor());
                }
            }
            return shortestPath;
        }else {
            // If there's no path between the two vertexes return null
            return null;
        }
    }

///////////////////////////////////////////////////////////////////////////////    
    /**
     * Searches for a specified vertex in the graph and returns the 
     * vertex if its in the graph, otherwise returns null
     * @param data
     * @return GraphNode or null
     */
    public GraphNode search(String data) {
        for(int i = 0; i < vertices.size(); i++) {
            if(vertices.get(i).getUserName().equals(data)) {
                return vertices.get(i);
            }
        } 
        return null;
    }
    
///////////////////////////////////////////////////////////////////////////////
    /**
     * Prints Graph
     */
    public void printGraph() {
        System.out.println("Printing Graph......");
        System.out.println("List of All Vertexes: ");
        for (int i = 0; i < numVertices; i++) {
            System.out.print(vertices.get(i).getUserName() + ", ");
        }
        System.out.println();
        for (int i = 0; i < numVertices; i++) {
            GraphNode currV = vertices.get(i);
            ArrayList<String> neighbors = currV.getFriends();
            System.out.print(
                "User: " + currV.getUserName()+ 
                " Friends: ");
            if (neighbors.size() == 0) {
                System.out.println("None");
            }
            for (int j = 0; j < neighbors.size(); j++) {
                if (j != neighbors.size() - 1) {
                    System.out.print("" + neighbors.get(j) + ", ");
                } else {
                    System.out.println(neighbors.get(j));
                }
            }
        }
    }
    
       public static void main(String[] args) {   
           SocialGraph g = new SocialGraph();
           g.addEdge("D","E");
           g.addEdge("G","F");
           g.addEdge("D", "G");
           g.addEdge("D", "A");
           System.out.println(g.getShortestPath("D","E"));
           System.out.println(g.getShortestPath("A","G"));

       }
    
}