package hw8.spp;

import exceptions.InsertionException;
import hw8.graph.Edge;
import hw8.graph.Graph;
import hw8.graph.Vertex;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Search for the shortest path between two endpoints.
 */
public abstract class StreetSearcher {

  /*
   * Notes:
   *  - Vertex data is the coordinates, stored as a String.
   *  - Vertex label is the Edge into it on the path found.
   *  - Edge data is the road name, stored as a String.
   *  - Edge label is the road length, stored as a Double.
   */

  public static boolean VERBOSE = true;

  // useful for marking distance to nodes, or use Double.POSITIVE_INFINITY
  protected static final double MAX_DISTANCE = 1e18;

  public int numLoaded;

  /* We use a HashMap to store all the vertices so we can
   * find them by name (i.e. their coordinates) when inserting
   * for a fast duplicates check. */
  protected Map<String, Vertex<String>> vertices;
  protected Graph<String, String> graph;


  /**
   * Creates a StreetSearcher object.
   *
   * @param graph and implementation of Graph ADT.
   */
  public StreetSearcher(Graph<String, String> graph) {
    vertices = new HashMap<>();
    this.graph = graph;
    this.numLoaded = 0;
  }

  // Get the path by tracing labels back from end to start.
  protected List<Edge<String>> getPath(Vertex<String> end,
                                       Vertex<String> start) {
    if (graph.label(end) != null) {
      List<Edge<String>> path = new ArrayList<>();

      Vertex<String> cur = end;
      Edge<String> road;
      while (cur != start) {
        road = (Edge<String>) graph.label(cur);  // unchecked cast ok
        path.add(road);
        cur = graph.from(road);
      }
      return path;
    }
    return null;
  }

  // Print the path found.
  protected void printPath(List<Edge<String>> path, double totalDistance) {
    if (path == null) {
      System.out.println("No path found");
      return;
    }

    System.out.printf("Total Distance: %.4f\n", totalDistance);
    for (int i = path.size() - 1; i >= 0; i--) {
      System.out.printf("\t%6.2f \t%s\n", graph.label(path.get(i)), path.get(i).get());
    }
  }


  /**
   * Find the shortest path.
   *
   * @param startName starting vertex name
   * @param endName   ending vertex name
   */
  public abstract void findShortestPath(String startName, String endName);

  // Add an endpoint to the network if it is a new endpoint
  private Vertex<String> addLocation(String name) {
    if (!vertices.containsKey(name)) {
      Vertex<String> v = graph.insert(name);
      vertices.put(name, v);
      return v;
    }
    return vertices.get(name);
  }

  /**
   * Load network from a data file.
   *
   * @param data File must be a list of edges
   *             with distances, in the format
   *             specified in the homework instructions.
   * @throws FileNotFoundException thrown if invalid file provided
   */
  public void loadNetwork(File data) throws FileNotFoundException {
    int numRoads = 0;

    // Read in from file fileName
    Scanner input = new Scanner(new FileInputStream(data));
    while (input.hasNext()) {

      // Parse the line in to <end1> <end2> <road-distance> <road-name>
      String[] tokens = input.nextLine().split(" ");
      String fromName = tokens[0];
      String toName = tokens[1];
      double roadDistance = Double.parseDouble(tokens[2]);
      String roadName = tokens[3];

      boolean roadAdded = addRoad(fromName, toName, roadDistance, roadName);
      if (roadAdded) {
        numRoads += 2;
      }
    }
    input.close();
    numLoaded = numRoads;
    if (VERBOSE) {
      System.out.printf("Network Loaded: %d roads, %d endpoints\n", numRoads, vertices.size());
    }
  }

  private boolean addRoad(String from, String to, double distance, String road) {
    // Get the fromVertex and toVertex endpoints, adding if necessary
    Vertex<String> fromVertex = addLocation(from);
    Vertex<String> toVertex = addLocation(to);

    // Add the road toVertex the network - We assume all roads are two-way and
    // ignore if we've already added the road as a reverse of another
    try {
      Edge<String> roadEdge = graph.insert(fromVertex, toVertex, road);
      Edge<String> backwardsRoad = graph.insert(toVertex, fromVertex, road);

      // Label each road with its weight
      graph.label(roadEdge, distance);
      graph.label(backwardsRoad, distance);

    } catch (InsertionException ignored) {
      return false;
    }

    return true;
  }

  protected void checkValidEndpoint(String endpointName) {
    if (!vertices.containsKey(endpointName)) {
      throw new IllegalArgumentException("Invalid Endpoint: " + endpointName);
    }
  }
}
