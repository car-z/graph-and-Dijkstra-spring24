package hw8.graph;

public class GraphPrinter<V, E> {

  private Graph<V, E> graph;

  /**
   * Create a Graph Printer.
   * @param graph an instance of Graph.
   */
  public GraphPrinter(Graph<V, E> graph) {
    this.graph = graph;
  }

  private String vertexString(Vertex<V> v) {
    return "\"" + v.get() + "\"";
  }

  private String verticesToString() {
    StringBuilder sb = new StringBuilder();
    for (Vertex<V> v : graph.vertices()) {
      sb.append("  ").append(vertexString(v)).append("\n");
    }
    return sb.toString();
  }

  private String edgeString(Edge<E> e) {
    return String.format("%s -> %s [label=\"%s\"]",
        this.vertexString(graph.from(e)),
        this.vertexString(graph.to(e)),
        e.get());
  }

  private String edgesToString() {
    StringBuilder edgs = new StringBuilder();
    for (Edge<E> e : graph.edges()) {
      edgs.append("  ").append(this.edgeString(e)).append(";\n");
    }
    return edgs.toString();
  }

  @Override
  public String toString() {
    return "digraph {\n"
        + this.verticesToString()
        + this.edgesToString()
        + "}";
  }
}
