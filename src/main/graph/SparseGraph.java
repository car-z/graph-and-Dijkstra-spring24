package hw8.graph;

import static java.util.Objects.hash;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * An implementation of Graph ADT using incidence lists
 * for sparse graphs where most nodes aren't connected.
 *
 * @param <V> Vertex element type.
 * @param <E> Edge element type.
 */
public class SparseGraph<V, E> implements Graph<V, E> {

  //HashMap which associates the vertices (keys) to its incident edges
  //which themselves are contained within individual HashSets
  private HashMap<VertexNode<V>, HashSet<EdgeNode<E>>> incidenceList;

  /**
   * default constructor for SparseGraph Object.
   */
  public SparseGraph() {
    this.incidenceList = new HashMap<>();
  }


  // Converts the vertex back to a VertexNode to use internally
  private VertexNode<V> convert(Vertex<V> v) throws PositionException {
    try {
      VertexNode<V> gv = (VertexNode<V>) v;
      if (gv.owner != this) {
        throw new PositionException();
      }
      return gv;
    } catch (NullPointerException | ClassCastException ex) {
      throw new PositionException();
    }
  }

  // Converts and edge back to a EdgeNode to use internally
  private EdgeNode<E> convert(Edge<E> e) throws PositionException {
    try {
      EdgeNode<E> ge = (EdgeNode<E>) e;
      if (ge.owner != this) {
        throw new PositionException();
      }
      return ge;
    } catch (NullPointerException | ClassCastException ex) {
      throw new PositionException();
    }
  }

  //INSERT VERTEX
  @Override
  public Vertex<V> insert(V v) throws InsertionException {
    VertexNode<V> newVertex = new VertexNode<>(v,this);
    if (v == null || incidenceList.containsKey(newVertex)) {
      throw new InsertionException();
    }
    //initializing empty HashSet to store edges which are incident to this new vertex
    HashSet<EdgeNode<E>> newEdgeSet = new HashSet<>(5);
    incidenceList.put(newVertex,newEdgeSet);
    return newVertex;
  }

  //INSERT EDGE
  @Override
  public Edge<E> insert(Vertex<V> from, Vertex<V> to, E e)
      throws PositionException, InsertionException {
    VertexNode<V> fromNode = convert(from);
    VertexNode<V> toNode = convert(to);
    EdgeNode<E> newEdge = new EdgeNode<>(fromNode,toNode,e,this);
    //call private helper method to confirm if given vertices are valid
    //and protect against self-loop and duplicate edges
    insertEdgePossible(fromNode,toNode,newEdge);
    //add newly created edge to list of incident edges for the origin and destination matrix
    incidenceList.get(fromNode).add(newEdge);
    fromNode.degree++;
    incidenceList.get(toNode).add(newEdge);
    toNode.degree++;
    return newEdge;
  }

  /**
   * private helper function to throw exception if inserting a new edge is not possible.
   * @param from origin vertex for new edge
   * @param to destination vertex for new edge.
   * @throws PositionException if either vertex position (from or to) is invalid
   * @throws InsertionException if insertion would create a self-loop or duplicate edge
   */
  private void insertEdgePossible(VertexNode<V> from, VertexNode<V> to, EdgeNode<E> newEdge)
          throws PositionException, InsertionException {
    if (!incidenceList.containsKey(from) || !incidenceList.containsKey(to)) {
      throw new PositionException();
    }
    if (from.equals(to) || incidenceList.get(from).contains(newEdge)) {
      throw new InsertionException();
    }
  }

  /**
   * private helper function to check if given vertex is valid and in graph.
   * @param v vertex to check for
   * @return vertex v after conversion to VertexNode
   * @throws PositionException if v is invalid or not in the graph
   */
  private VertexNode<V> validVertex(Vertex<V> v) throws PositionException {
    VertexNode<V> vertexNode = convert(v);
    if (!incidenceList.containsKey(vertexNode)) {
      throw new PositionException();
    }
    return vertexNode;
  }

  /**
   * private helper function to check if given edge is valid and in graph.
   * @param e edge to check for
   * @return edge v after conversion to EdgeNode
   * @throws PositionException if e is invalid or not in the graph
   */
  private EdgeNode<E> validEdge(Edge<E> e) throws PositionException {
    EdgeNode<E> edgeNode = convert(e);
    VertexNode<V> from = edgeNode.from;
    VertexNode<V> to = edgeNode.to;
    HashSet<EdgeNode<E>> edgeSet;
    //search for edge in HashSet of edges which has fewer edges (for efficiency)
    if (from.degree >= to.degree) {
      edgeSet = incidenceList.get(to);
    } else {
      edgeSet = incidenceList.get(from);
    }
    if (!edgeSet.contains(edgeNode)) {
      throw new PositionException();
    }
    return edgeNode;
  }

  //REMOVE VERTEX
  @Override
  public V remove(Vertex<V> v) throws PositionException, RemovalException {
    VertexNode<V> vertexNode = validVertex(v);
    //not allowed to remove vertex if it still has incident edges (the associated HashSet contains EdgeNode objects)
    if (!(incidenceList.get(vertexNode).isEmpty())) {
      throw new RemovalException();
    }
    incidenceList.remove(vertexNode);
    return vertexNode.data;
  }

  //REMOVE EDGE
  @Override
  public E remove(Edge<E> e) throws PositionException {
    EdgeNode<E> edgeNode = validEdge(e);
    VertexNode<V> from = edgeNode.from;
    VertexNode<V> to = edgeNode.to;
    //remove the edge from the incident edges set of both its origin and destination vertex
    incidenceList.get(from).remove(edgeNode);
    from.degree--;
    incidenceList.get(to).remove(edgeNode);
    to.degree--;
    return edgeNode.data;
  }

  @Override
  public Iterable<Vertex<V>> vertices() {
    Set<VertexNode<V>> vertexSet = incidenceList.keySet();
    return Collections.unmodifiableSet(vertexSet);
  }

  @Override
  public Iterable<Edge<E>> edges() {
    //creating new Set to hold all the edges in This SparseGraph
    Set<EdgeNode<E>> allEdges = new HashSet<>();
    //extracting all edges from the SparseGraph and consolidating each individual set into one set
    Collection<HashSet<EdgeNode<E>>> hashEdgeSets = incidenceList.values();
    for (HashSet<EdgeNode<E>> edgeSet : hashEdgeSets) {
      allEdges.addAll(edgeSet);
    }
    return Collections.unmodifiableSet(allEdges);
  }

  @Override
  public Iterable<Edge<E>> outgoing(Vertex<V> v) throws PositionException {
    VertexNode<V> vertexNode = validVertex(v);
    //extract Set of all incident edges associated with vertex v
    HashSet<EdgeNode<E>> incidentEdges = incidenceList.get(vertexNode);
    //create new Set to hold the outgoing edges
    Set<EdgeNode<E>> outgoingEdges = new HashSet<>();
    //add only the outgoing edges to the new Set to be returned as outgoing iterable
    for (EdgeNode<E> edge : incidentEdges) {
      if (edge.from.equals(v)) {
        outgoingEdges.add(edge);
      }
    }
    return Collections.unmodifiableSet(outgoingEdges);
  }

  @Override
  public Iterable<Edge<E>> incoming(Vertex<V> v) throws PositionException {
    VertexNode<V> vertexNode = validVertex(v);
    //extract set of all incident edges associated with vertex v
    HashSet<EdgeNode<E>> incidentEdges = incidenceList.get(vertexNode);
    //create new set to hold incoming edges
    Set<EdgeNode<E>> incomingEdges = new HashSet<>();
    //add only the incoming edges to the new Set to be returned as incoming iterable
    for (EdgeNode<E> edge : incidentEdges) {
      if (edge.to.equals(v)) {
        incomingEdges.add(edge);
      }
    }
    return Collections.unmodifiableSet(incomingEdges);
  }

  @Override
  public Vertex<V> from(Edge<E> e) throws PositionException {
    EdgeNode<E> edgeNode = validEdge(e);
    return edgeNode.from;
  }

  @Override
  public Vertex<V> to(Edge<E> e) throws PositionException {
    EdgeNode<E> edgeNode = validEdge(e);
    return edgeNode.to;
  }

  //LABEL A VERTEX
  @Override
  public void label(Vertex<V> v, Object l) throws PositionException {
    VertexNode<V> vertexNode = validVertex(v);
    vertexNode.label = l;
  }

  //LABEL AN EDGE
  @Override
  public void label(Edge<E> e, Object l) throws PositionException {
    EdgeNode<E> edgeNode = validEdge(e);
    edgeNode.label = l;
  }

  //RETURN VERTEX LABEL
  @Override
  public Object label(Vertex<V> v) throws PositionException {
    VertexNode<V> vertexNode = validVertex(v);
    return vertexNode.label;
  }

  //RETURN EDGE LABEL
  @Override
  public Object label(Edge<E> e) throws PositionException {
    EdgeNode<E> edgeNode = validEdge(e);
    return edgeNode.label;
  }

  @Override
  public void clearLabels() {
    for (Vertex<V> v : this.vertices()) {
      VertexNode<V> vertexNode = convert(v);
      vertexNode.label = null;
    }
    for (Edge<E> e : this.edges()) {
      EdgeNode<E> edgeNode = convert(e);
      edgeNode.label = null;
    }
  }

  @Override
  public String toString() {
    GraphPrinter<V, E> gp = new GraphPrinter<>(this);
    return gp.toString();
  }

  //Private class for a vertex of type V
  private final class VertexNode<V> implements Vertex<V> {
    V data;
    Graph<V, E> owner;
    Object label;
    int degree;

    /**
     * Constructor to create new VertexNode object.
     * @param v the data to be held at this vertex
     * @param owner the Graph object that this vertex belongs to
     */
    VertexNode(V v, Graph<V,E> owner) {
      this.data = v;
      this.label = null;
      this.owner = owner;
      this.degree = 0;
    }

    @Override
    public V get() {
      return this.data;
    }

    @Override
    public int hashCode() {
      return hash(data, owner);
    }

    @Override
    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }
      if (other == null || this.getClass() != other.getClass()) {
        return false;
      }

      VertexNode<V> otherCast = (VertexNode<V>) other;
      //two vertex objects are equal so long as they belong to the same Graph and contain the same data
      if (this.owner != otherCast.owner) {
        return false;
      }
      return this.data == otherCast.data;
    }
  }

  //Private class for an edge of type E
  private final class EdgeNode<E> implements Edge<E> {
    E data;
    Graph<V, E> owner;
    VertexNode<V> from;
    VertexNode<V> to;
    Object label;

    /**
     * Constructor to create a new EdgeNode object.
      * @param f origin vertex for this edge
     * @param t destination vertex for this edge
     * @param e data to be stored at this edge
     * @param owner the Graph object which this edge belongs to
     */
    EdgeNode(VertexNode<V> f, VertexNode<V> t, E e, Graph<V,E> owner) {
      this.from = f;
      this.to = t;
      this.data = e;
      this.label = null;
      this.owner = owner;
    }

    @Override
    public E get() {
      return this.data;
    }

    @Override
    public int hashCode() {
      return hash(from) - hash(to);
    }

    @Override
    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }
      if (other == null || this.getClass() != other.getClass()) {
        return false;
      }

      EdgeNode<E> otherCast = (EdgeNode<E>) other;
      //two edges are equal so long as they belong to the same Graph and have the same origin and destination vertices
      if (this.owner != otherCast.owner) {
        return false;
      }
      return this.from == otherCast.from && this.to == otherCast.to;
    }
  }
}
