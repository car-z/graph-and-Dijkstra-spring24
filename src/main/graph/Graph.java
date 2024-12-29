package hw8.graph;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;

/**
 * Graph ADT to represent directed graphs.
 *
 * @param <V> Vertex element type.
 * @param <E> Edge element type.
 */
public interface Graph<V, E> {

  /**
   * Insert a new vertex.
   *
   * @param v Element to insert.
   * @return Vertex position created to hold element.
   * @throws InsertionException If v is null or already in this Graph.
   */
  Vertex<V> insert(V v) throws InsertionException;

  /**
   * Insert a new edge.
   *
   * @param from Vertex position where edge starts.
   * @param to   Vertex position where edge ends.
   * @param e    Element to insert.
   * @return Edge position created to hold element.
   * @throws PositionException  If either vertex position is invalid.
   * @throws InsertionException If insertion would create a self-loop or
   *                            duplicate edge.
   */
  Edge<E> insert(Vertex<V> from, Vertex<V> to, E e)
      throws PositionException, InsertionException;

  /**
   * Remove a vertex.
   *
   * @param v Vertex position to remove.
   * @return Element from removed vertex position.
   * @throws PositionException If vertex position is invalid.
   * @throws RemovalException  If vertex still has incident edges.
   */
  V remove(Vertex<V> v) throws PositionException, RemovalException;

  /**
   * Remove an edge.
   *
   * @param e Edge position to remove.
   * @return Element from removed edge position.
   * @throws PositionException If edge position is invalid.
   */
  E remove(Edge<E> e) throws PositionException;

  /**
   * Vertices of graph.
   *
   * @return Iterable over all vertices of the graph (in no specific order).
   */
  Iterable<Vertex<V>> vertices();

  /**
   * Edges of graph.
   *
   * @return Iterable over all edges of the graph (in no specific order).
   */
  Iterable<Edge<E>> edges();

  /**
   * Outgoing edges of vertex.
   *
   * @param v Vertex position to explore.
   * @return Iterable over all outgoing edges of the given vertex
   *         (in no specific order).
   * @throws PositionException If vertex position is invalid.
   */
  Iterable<Edge<E>> outgoing(Vertex<V> v) throws PositionException;

  /**
   * Incoming edges of vertex.
   *
   * @param v Vertex position to explore.
   * @return Iterable over all incoming edges of the given vertex
   *         (in no specific order).
   * @throws PositionException If vertex position is invalid.
   */
  Iterable<Edge<E>> incoming(Vertex<V> v) throws PositionException;

  /**
   * Start vertex of edge.
   *
   * @param e Edge position to explore.
   * @return Vertex position edge starts from.
   * @throws PositionException If edge position is invalid.
   */
  Vertex<V> from(Edge<E> e) throws PositionException;

  /**
   * End vertex of edge.
   *
   * @param e Edge position to explore.
   * @return Vertex position edge leads to.
   * @throws PositionException If edge position is invalid.
   */
  Vertex<V> to(Edge<E> e) throws PositionException;

  /**
   * Label vertex with object.
   *
   * @param v Vertex position to label.
   * @param l Label object.
   * @throws PositionException If vertex position is invalid.
   */
  void label(Vertex<V> v, Object l) throws PositionException;

  /**
   * Label edge with object.
   *
   * @param e Edge position to label.
   * @param l Label object.
   * @throws PositionException If edge position is invalid.
   */
  void label(Edge<E> e, Object l) throws PositionException;

  /**
   * Vertex label.
   *
   * @param v Vertex position to query.
   * @return Label object (or null if none).
   * @throws PositionException If vertex position is invalid.
   */
  Object label(Vertex<V> v) throws PositionException;

  /**
   * Edge label.
   *
   * @param e Edge position to query.
   * @return Label object (or null if none).
   * @throws PositionException If edge position is invalid.
   */
  Object label(Edge<E> e) throws PositionException;

  /**
   * Clear all labels.
   * All labels are null after this.
   */
  void clearLabels();
}
