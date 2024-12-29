package hw8.spp;

import static java.util.Objects.hash;

import hw8.graph.Edge;
import hw8.graph.Graph;
import hw8.graph.Vertex;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;


public class DijkstraStreetSearcher extends StreetSearcher {

  /**
   * Creates a StreetSearcher object.
   *
   * @param graph an implementation of Graph ADT.
   */
  public DijkstraStreetSearcher(Graph<String, String> graph) {
    super(graph);
  }

  /**
   * Private Helper Function to check if given start and end points are valid vertices.
   * @param startName String for name of starting vertex
   * @param endName String for name of ending vertex
   * @return true if both the start and end points are valid vertices, false otherwise
   */
  private boolean validVertices(String startName, String endName) {
    Vertex<String> start = vertices.get(startName);
    Vertex<String> end = vertices.get(endName);
    if (start == null && end == null) {
      System.out.println("Invalid Endpoints: " + startName + ", " + endName);
      return false;
    } else if (end == null) {
      System.out.println("Invalid Endpoint: " + endName);
      return false;
    } else if (start == null) {
      System.out.println("Invalid Endpoint: " + startName);
      return false;
    }
    return true;
  }

  @Override
  public void findShortestPath(String startName, String endName) {
    //call private helper function to check that passed in vertices are valid
    if (!validVertices(startName,endName)) {
      return;
    }

    Vertex<String> start = vertices.get(startName);
    Vertex<String> end = vertices.get(endName);
    double totalDist = -1;  // totalDist must be update below

    //PriorityQueue of VertexEntries so that root is always unexplored vertex with minimum distance
    PriorityQueue<VertexEntry> priorityVertex = new PriorityQueue<>(vertices.size(),new ShortestDistance());
    //HashMap to keep track of all vertices where key is the vertex, and value is the corresponding VertexEntry object
    HashMap<Vertex<String>,VertexEntry> allVertexEntries = new HashMap<>(vertices.size());

    //setting default values for all vertices
    setDefaultValues(start,priorityVertex,allVertexEntries);

    boolean found = false;

    //while the shortest path has not been found and there are still more paths to be found
    while (!found && !priorityVertex.isEmpty()) {
      //perform Dijkstra's Algorithm
      found = parseDistances(priorityVertex,allVertexEntries,end);
    }

    totalDist = allVertexEntries.get(end).distance;

    // These method calls will create and print the path for you
    List<Edge<String>> path = getPath(end, start);
    if (VERBOSE) {
      printPath(path, totalDist);
    }
  }

  /**
   * Private helper function to aid in Dijkstra's Algorithm by setting all vertices to initial values needed.
   * @param start the vertex that is origin of shortest path
   * @param vertexPriority the PriorityQueue which places vertices by in a min-heap by their distances
   * @param vertexMap the hashMap which associates a vertex with its vertexEntry
   */
  private void setDefaultValues(Vertex<String> start, PriorityQueue<VertexEntry> vertexPriority,
                                HashMap<Vertex<String>,VertexEntry> vertexMap) {
    for (Vertex<String> v : graph.vertices()) {
      //set label of the vertex to null
      graph.label(v,null);
      VertexEntry newEntry;
      if (v.equals(start)) {
        //initialized with explored marker set to false
        newEntry = new VertexEntry(v,0);
      } else {
        //initialized with explored marker set to false
        newEntry = new VertexEntry(v,MAX_DISTANCE);
      }
      //add vertices to PriorityQueue and HashMap
      vertexPriority.add(newEntry);
      vertexMap.put(v,newEntry);
    }
  }

  /**
   * Private helper function which performs Dijkstra's Algorithm by updating distances of unexplored, incident vertices.
   * @param priorityVertex priorityQueue of unexplored vertices, organized in a min-heap by distance
   *                       Pre-condition: priorityVertex is not empty
   * @param allVertexEntries hashMap which associates the vertex with its vertexEntry
   * @param end Vertex which is destination point of shortest path
   * @return true if destination vertex was just explored, false otherwise
   */
  private boolean parseDistances(PriorityQueue<VertexEntry> priorityVertex,
                              HashMap<Vertex<String>,VertexEntry> allVertexEntries, Vertex<String> end) {
    //extract unexplored vertex with minimum distance
    VertexEntry minDistVertex = priorityVertex.poll();
    //for every unexplored outgoing edge incident to minDistVertex
    for (Edge<String> road : graph.outgoing(minDistVertex.vertex)) {
      //extracting the incident VertexEntry that road leads to
      VertexEntry destination = allVertexEntries.get(graph.to(road));
      //checking if this road has already been explored
      if (destination.explored) {
        continue;
      }
      double d = minDistVertex.distance + (double) graph.label(road);
      if (d < destination.distance) {
        destination.distance = d;
        graph.label(destination.vertex, road);
        //update PriorityQueue to reflect new distances
        priorityVertex.remove(destination);
        priorityVertex.add(destination);
      }
    }
    //set minDistVertex as explored, return true if minDistVertex is the endpoint of the shortest path to be found
    minDistVertex.explored = true;
    return minDistVertex.vertex.equals(end);
  }

  //private class which bundles a vertex with its distance (from another vertex)
  private static class VertexEntry {
    Vertex<String> vertex;
    double distance;
    boolean explored;

    /**
     * Constructor for new VertexEntry object.
     * @param v the vertex
     * @param distance the distance of vertex v from the starting endpoint
     */
    VertexEntry(Vertex<String> v, double distance) {
      this.vertex = v;
      this.distance = distance;
      this.explored = false;
    }

    @Override
    public int hashCode() {
      return hash(vertex);
    }

    @Override
    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }
      if (other == null || this.getClass() != other.getClass()) {
        return false;
      }

      VertexEntry otherCast = (VertexEntry) other;
      //VertexEntry is equal to another VertexEntry as long as they are associated with the same vertex
      return this.vertex.equals(otherCast.vertex);
    }
  }

  //private class to create a custom comparator for VertexEntry to be used in the PriorityQueue
  private static class ShortestDistance implements Comparator<VertexEntry> {

    @Override
    public int compare(VertexEntry v1, VertexEntry v2) {
      return Double.compare(v1.distance,v2.distance);
    }
  }

}
