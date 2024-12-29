package hw8;

import hw8.graph.Graph;
import hw8.graph.SparseGraph;

public class SparseGraphTest extends GraphTest {

  @Override
  protected Graph<String, String> createGraph() {
    return new SparseGraph<>();
  }
}
