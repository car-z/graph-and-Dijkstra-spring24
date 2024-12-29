package hw8;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import hw8.graph.Edge;
import hw8.graph.Graph;
import hw8.graph.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class GraphTest {

  protected Graph<String, String> graph;

  @BeforeEach
  public void setupGraph() {
    this.graph = createGraph();
  }

  protected abstract Graph<String, String> createGraph();

  //INSERT VERTEX TESTS
  @Test
  @DisplayName("insert(v) returns a vertex with given data")
  public void canGetVertexAfterInsert() {
    Vertex<String> v1 = graph.insert("v1");
    assertEquals(v1.get(), "v1");
  }

  @Test
  @DisplayName("insert(null) throws exception")
  public void insertVertexThrowsInsertionExceptionWhenVertexIsNull() {
    try {
      Vertex<String> v1 = graph.insert(null);
      fail("Failed to throw InsertionException");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("inserting existing vertex into graph throws exception")
  public void insertExistingVertexThrowsException() {
    Vertex<String> v1 = graph.insert("v1");
    try {
      Vertex<String> v2 = graph.insert("v1");
      fail("Failed to throw InsertionException");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("inserting multiple vertices into graph works")
  public void insertMultipleVertices() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    assertEquals("v1",v1.get());
    assertEquals("v2",v2.get());
    assertEquals("v3",v3.get());
  }

  //INSERT EDGE TESTS
  //relies on insert vertex working

  @Test
  @DisplayName("insert(U, V, e) returns an edge with given data")
  public void canGetEdgeAfterInsert() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(e1.get(), "v1-v2");
  }

  @Test
  @DisplayName("insert(invalidVertex, V, e) throws exception")
  public void insertEdgeThrowsPositionExceptionWhenFirstVertexIsInvalid() {
    Graph<String,String> otherGraph = createGraph();
    Vertex<String> v1 = otherGraph.insert("v1");
    try {
      Vertex<String> v2 = graph.insert("v2");
      graph.insert(v1, v2, "e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(null, V, e) throws exception")
  public void insertEdgeThrowsPositionExceptionWhenFirstVertexIsNull() {
    try {
      Vertex<String> v2 = graph.insert("v2");
      graph.insert(null, v2, "e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(v,invalidVertex,e) throws exception")
  public void insertEdgeThrowsPositionExceptionWhenSecondVertexIsInvalid() {
    Graph<String,String> otherGraph = createGraph();
    Vertex<String> v2 = otherGraph.insert("v2");
    try {
      Vertex<String> v1 = graph.insert("v1");
      graph.insert(v1,v2,"e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(v,null,e) throws exception")
  public void insertEdgeThrowsPositionExceptionWhenSecondVertexIsNull() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      graph.insert(v1,null,"e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("inserting an edge with two invalid null vertices throws exception")
  public void insertEdgeThrowsPositionExceptionWhenBothVerticesInvalid() {
    try {
      graph.insert(null,null,"e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("inserting an edge throws exception when self-loop is created")
  public void insertEdgeThrowsInsertionExceptionWhenSelfLoopCreated() {
    Vertex<String> v = graph.insert("v");
    try {
      graph.insert(v,v,"e");
      fail("The expected exception was not thrown");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("inserting duplicate edge throws exception")
  public void insertDuplicateEdgeThrowsInsertionException() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    try {
      Edge<String> e2 = graph.insert(v,u,"v-u");
      fail("The expected exception was not thrown");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("inserting edge with same vertices but different data also throws exception")
  public void insertDuplicateEdgeWithDifferentDataThrowsInsertionException() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    try {
      Edge<String> e2 = graph.insert(v,u,"diffData");
      fail("The expected exception was not thrown");
    } catch(InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("inserting edge in opposite direction does not throw exception")
  public void insertOppositeOrientedEdgeDoesNotThrowException() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    Edge<String> e2 = graph.insert(u,v,"u-v");
    assertEquals("v-u",e1.get());
    assertEquals("u-v",e2.get());
  }

  @Test
  @DisplayName("can insert edge with null data")
  public void insertEdgeNullData() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,null);
    assertNull(e1.get());
  }

  @Test
  @DisplayName("inserting multiple edges works")
  public void insertMultipleEdges() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    Vertex<String> z = graph.insert("z");
    Edge<String> e2 = graph.insert(u,z,"u-z");
    Vertex<String> x = graph.insert("x");
    Edge<String> e3 = graph.insert(z,x,"z-x");
    Edge<String> e4 = graph.insert(x,v,"x-v");
    assertEquals("v-u",e1.get());
    assertEquals("u-z",e2.get());
    assertEquals("z-x",e3.get());
    assertEquals("x-v",e4.get());
  }

  //REMOVE VERTEX TESTS

  @Test
  @DisplayName("removing vertex returns element from removed vertex")
  public void removeVertexReturnsElementAtRemovedPosition() {
    Vertex<String> v = graph.insert("v");
    assertEquals("v",graph.remove(v));
  }

  @Test
  @DisplayName("removing invalid vertex throws exception")
  public void removeInvalidVertexThrowsException() {
    Graph<String,String> newGraph = createGraph();
    Vertex<String> v = newGraph.insert("v1");
    Vertex<String> thisV = graph.insert("v1");
    try {
      graph.remove(v);
      fail("failed to throw exception");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("calling remove on a vertex removes the correct vertex")
  public void removeVertexRemovesCorrectVertex() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    assertEquals("v1",graph.remove(v1));
    assertEquals("v2",v2.get());
  }

  @Test
  @DisplayName("removing vertex with incident edges throws exception")
  public void removeVertexWithIncidentEdgesThrowsRemovalException() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    try {
      graph.remove(v);
      fail("Failed to throw expected exception");
    } catch(RemovalException ex) {
      return;
    }
  }

  @Test
  @DisplayName("calling remove multiple times works")
  public void removeMultipleVertices() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    assertEquals("v1",graph.remove(v1));
    assertEquals("v2",graph.remove(v2));
    assertEquals("v3",graph.remove(v3));
  }

  @Test
  @DisplayName("cannot insert edge after vertex has been removed")
  public void cannotInsertEdgesAfterVertexRemoved() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    graph.remove(v);
    try {
      Edge<String> e1 = graph.insert(v,u,"v-u");
      fail("Failed to throw exception");
    } catch(PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("removing a vertex removes it from graph (cannot remove more than once)")
  public void cannotRemoveVertexAfterAlreadyRemoved() {
    Vertex<String> v1 = graph.insert("v1");
    graph.remove(v1);
    try {
      graph.remove(v1);
      fail("Failed to throw exception");
    } catch (PositionException ex) {
      return;
    }
  }

  //REMOVE EDGE TESTS

  @Test
  @DisplayName("removing edge returns element at removed edge")
  public void removeEdgeReturnsElementAtRemovedEdge() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    assertEquals("v-u",graph.remove(e1));
  }

  @Test
  @DisplayName("calling remove on a specific edge removes that edge only")
  public void removeSpecifiedEdgeRemovesCorrectEdge() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    Edge<String> e2 = graph.insert(u,v,"u-v");
    assertEquals("v-u",graph.remove(e1));
    assertEquals("u-v",e2.get());
  }

  @Test
  @DisplayName("removing multiple edges works")
  public void removeMultipleEdges() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    Edge<String> e2 = graph.insert(u,v,"u-v");
    Vertex<String> z = graph.insert("z");
    Edge<String> e3 = graph.insert(v,z,"v-z");
    assertEquals("v-u",graph.remove(e1));
    assertEquals("u-v",graph.remove(e2));
    assertEquals("v-z",graph.remove(e3));
  }

  @Test
  @DisplayName("removing invalid edge throws PositionException")
  public void removeInvalidEdgeThrowsPositionException() {
    Graph<String,String> otherGraph = createGraph();
    Vertex<String> o1 = otherGraph.insert("o1");
    Vertex<String> o2 = otherGraph.insert("o2");
    Edge<String> oE1 = otherGraph.insert(o1,o2,"o1-o2");
    Vertex<String> v1 = graph.insert("o1");
    Vertex<String> v2 = graph.insert("o2");
    Edge<String> vE1 = graph.insert(v1,v2,"o1-o2");
    try {
      graph.remove(oE1);
      fail("Expected expection was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("can remove vertex after all incident edges have been removed")
  public void removeVertexAfterRemovingAllIncidentEdges() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    Edge<String> e2 = graph.insert(u,v,"u-v");
    graph.remove(e1);
    graph.remove(e2);
    assertEquals("v",graph.remove(v));
  }

  @Test
  @DisplayName("Removing an edge removes it from graph (cannot remove twice)")
  public void edgeCannotBeRemovedTwice() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    graph.remove(e1);
    try {
      graph.remove(e1);
      fail("failed to throw exception");
    } catch(PositionException ex) {
      return;
    }
  }

  //VERTICES ITERATOR TEST
  @Test
  @DisplayName("vertices() iterates over all vertices, and only vertices, in a graph")
  public void verticesIteratorIteratesOverAllVertices() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    Vertex<String> z = graph.insert("z");
    Edge<String> e2 = graph.insert(u,z,"u-z");
    Vertex<String> x = graph.insert("x");
    Edge<String> e3 = graph.insert(z,x,"z-x");
    Edge<String> e4 = graph.insert(x,v,"x-v");

    String[] expected = new String[]{"v","u","z","x"};
    int[] counter = new int[4];
    boolean allVertices = true;
    int count = 0;
    for (Vertex<String> vertexIterator : graph.vertices()) {
      count++;
      boolean foundVertex = false;
      for (int i = 0; i < 4; i++) {
        if (vertexIterator.get().equals(expected[i])) {
          counter[i]++;
          foundVertex = true;
          break;
        }
      }
      if (!foundVertex) {
        allVertices = false;
      }
    }
    assertEquals(4,count);
    for (int i = 0; i < 4; i++) {
      assertEquals(1,counter[i]);
    }
    assertTrue(allVertices);
  }

  @Test
  @DisplayName("vertices() works for empty graphs")
  public void iterateVerticesOfEmptyGraph() {
    int count = 0;
    for (Vertex<String> vertexIterator : graph.vertices()) {
      count++;
    }
    assertEquals(0,count);
  }

  //EDGES ITERATOR TEST
  @Test
  @DisplayName("edges() iterates over all edges, and only edges, in a graph")
  public void edgesIteratorIteratesOverAllEdges() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    Vertex<String> z = graph.insert("z");
    Edge<String> e2 = graph.insert(u,z,"u-z");
    Vertex<String> x = graph.insert("x");
    Edge<String> e3 = graph.insert(z,x,"z-x");
    Edge<String> e4 = graph.insert(x,v,"x-v");

    String[] expected = new String[]{"v-u","u-z","z-x","x-v"};
    int[] counter = new int[4];
    boolean allEdges = true;
    int count = 0;
    for (Edge<String> edgeIterator : graph.edges()) {
      count++;
      boolean foundEdge = false;
      for (int i = 0; i < 4; i++) {
        if (edgeIterator.get().equals(expected[i])) {
          counter[i]++;
          foundEdge = true;
          break;
        }
      }
      if (!foundEdge) {
        allEdges = false;
      }
    }
    assertEquals(4,count);
    for (int i = 0; i < 4; i++) {
      assertEquals(1,counter[i]);
    }
    assertTrue(allEdges);
  }

  //OUTGOING EDGES ITERATOR TESTS
  @Test
  @DisplayName("outgoing() iterates over all outgoing edges, and only outgoing edges, of a specified vertex")
  public void outgoingIteratorIteratesOverAllOutgoingEdgesOfGivenVertex() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    Vertex<String> z = graph.insert("z");
    Edge<String> e2 = graph.insert(v,z,"v-z");
    Vertex<String> x = graph.insert("x");
    Edge<String> e3 = graph.insert(v,x,"v-x");

    String[] expected = new String[]{"v-u","v-z","v-x"};
    int[] counter = new int[3];
    boolean allOutgoing = true;
    int count = 0;
    for (Edge<String> outgoingIterator : graph.outgoing(v)) {
      count++;
      boolean foundOutgoing = false;
      for (int i = 0; i < 3; i++) {
        if (outgoingIterator.get().equals(expected[i])) {
          counter[i]++;
          foundOutgoing = true;
          break;
        }
      }
      if (!foundOutgoing) {
        allOutgoing = false;
      }
    }
    assertEquals(3,count);
    for (int i = 0; i < 3; i++) {
      assertEquals(1,counter[i]);
    }
    assertTrue(allOutgoing);
  }

  @Test
  @DisplayName("outgoing() throws PositionException if vertex position is invalid")
  public void outgoingThrowsPositionExceptionWhenGivenVertexInvalid() {
    try {
      for (Edge<String> outgoingIterator : graph.outgoing(null)) {
        fail("Failed to throw exception");
      }
    } catch (PositionException ex) {
      return;
    }
  }

  //INCOMING EDGES ITERATOR TESTS

  @Test
  @DisplayName("incoming() iterates over all incoming edges, and only incoming edges, of a specified vertex")
  public void incomingIteratorIteratesOverAllIncomingEdgesOfGivenVertex() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(u,v,"u-v");
    Vertex<String> z = graph.insert("z");
    Edge<String> e2 = graph.insert(z,v,"z-v");
    Vertex<String> x = graph.insert("x");
    Edge<String> e3 = graph.insert(x,v,"x-v");

    String[] expected = new String[]{"u-v","z-v","x-v"};
    int[] counter = new int[3];
    boolean allIncoming = true;
    int count = 0;
    for (Edge<String> incomingIterator : graph.incoming(v)) {
      count++;
      boolean foundIncoming = false;
      for (int i = 0; i < 3; i++) {
        if (incomingIterator.get().equals(expected[i])) {
          counter[i]++;
          foundIncoming = true;
          break;
        }
      }
      if (!foundIncoming) {
        allIncoming = false;
      }
    }
    assertEquals(3,count);
    for (int i = 0; i < 3; i++) {
      assertEquals(1,counter[i]);
    }
    assertTrue(allIncoming);
  }

  @Test
  @DisplayName("incoming() throws PositionException if vertex position is invalid")
  public void incomingThrowsPositionExceptionWhenGivenVertexInvalid() {
    try {
      for (Edge<String> incomingIterator : graph.incoming(null)) {
        fail("Failed to throw exception");
      }
    } catch (PositionException ex) {
      return;
    }
  }

  //FROM TESTS
  @Test
  @DisplayName("from(e) returns start position of edge e")
  public void fromReturnsStartPositionOfGivenEdge() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    Edge<String> e2 = graph.insert(u,v,"u-v");
    assertEquals(v,graph.from(e1));
    assertEquals(u,graph.from(e2));
  }

  @Test
  @DisplayName("from(null) throws PositionException")
  public void fromThrowsPositionExceptionWhenEdgeInvalid() {
    try {
      Vertex<String> v = graph.from(null);
      fail("failed to throw expected exception");
    } catch(PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("from(EdgeNotInGraph) throws PositionException")
  public void fromThrowsPositionExceptionWhenEdgeIsNotInThisGraph() {
    Graph<String,String> otherGraph = createGraph();
    Vertex<String> v = otherGraph.insert("v");
    Vertex<String> u = otherGraph.insert("u");
    Edge<String> e1 = otherGraph.insert(v,u,"v-u");
    try {
      Vertex<String> from = graph.from(e1);
      fail("failed to throw expected exception");
    } catch (PositionException ex) {
      return;
    }
  }

  //TO TESTS
  @Test
  @DisplayName("to(e) returns end position of edge e")
  public void toReturnsEndPositionOfGivenEdge() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v,u,"v-u");
    Edge<String> e2 = graph.insert(u,v,"u-v");
    assertEquals(u,graph.to(e1));
    assertEquals(v,graph.to(e2));
  }

  @Test
  @DisplayName("to(null) throws PositionException")
  public void toThrowsPositionExceptionWhenEdgeInvalid() {
    try {
      Vertex<String> v = graph.to(null);
      fail("failed to throw expected exception");
    } catch(PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("to(EdgeNotInGraph) throws PositionException")
  public void toThrowsPositionExceptionWhenEdgeIsNotInThisGraph() {
    Graph<String,String> otherGraph = createGraph();
    Vertex<String> v = otherGraph.insert("v");
    Vertex<String> u = otherGraph.insert("u");
    Edge<String> e1 = otherGraph.insert(v,u,"v-u");
    try {
      Vertex<String> from = graph.to(e1);
      fail("failed to throw expected exception");
    } catch (PositionException ex) {
      return;
    }
  }

  //LABEL(VERTEX) and LABEL(VERTEX,LABEL) TESTS

  @Test
  @DisplayName("label(v,l) correctly labels vertex v with object l, as returned by label(v)")
  public void canGetVertexLabelAfterLabelCalled() {
    Vertex<String> v = graph.insert("v");
    String l = "1";
    graph.label(v,l);
    assertEquals("1",graph.label(v));
  }

  @Test
  @DisplayName("label(v) returns null if vertex v has no label")
  public void vertexLabelIsNullWhenNoLabel() {
    Vertex<String> v = graph.insert("v");
    assertNull(graph.label(v));
  }

  @Test
  @DisplayName("label(v,l) throws PositionException for invalid vertex v")
  public void addLabelToInvalidVertexThrowsPositionException() {
    Graph<String,String> otherGraph = createGraph();
    Vertex<String> v = otherGraph.insert("v");
    String l = "1";
    try {
      graph.label(v,l);
      fail("failed to throw exception");
    } catch(PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(v) throws PositionException for invalid vertex v")
  public void returnLabelOfInvalidVertexThrowsPositionException() {
    Graph<String,String> otherGraph = createGraph();
    Vertex<String> v = otherGraph.insert("v");
    try {
      graph.label(v);
      fail("failed to throw exception");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("can successfully update label of vertex")
  public void updateVertexLabel() {
    Vertex<String> v = graph.insert("v");
    String l = "1";
    graph.label(v,l);
    assertEquals("1",graph.label(v));
    String newl = "2";
    graph.label(v,newl);
    assertEquals("2",graph.label(v));
  }

  //LABEL(EDGE) and LABEL(EDGE,LABEL) TESTS

  @Test
  @DisplayName("label(e,l) correctly labels edge e with object l, as returned by label(e)")
  public void canGetEdgeLabelAfterLabelCalled() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e = graph.insert(v, u, "v-u");
    String l = "1";
    graph.label(e,l);
    assertEquals("1",graph.label(e));
  }

  @Test
  @DisplayName("label(e) returns null if edge e has no label")
  public void edgeLabelIsNullWhenNoLabel() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e = graph.insert(v, u, "v-u");
    assertNull(graph.label(e));
  }

  @Test
  @DisplayName("label(e,l) throws PositionException for invalid edge e")
  public void addLabelToInvalidEdgeThrowsPositionException() {
    Graph<String,String> otherGraph = createGraph();
    Vertex<String> v = otherGraph.insert("v");
    Vertex<String> u = otherGraph.insert("u");
    Edge<String> e = otherGraph.insert(v, u, "v-u");
    String l = "1";
    try {
      graph.label(e,l);
      fail("failed to throw exception");
    } catch(PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(e) throws PositionException for invalid edge e")
  public void returnLabelOfInvalidEdgeThrowsPositionException() {
    Graph<String,String> otherGraph = createGraph();
    Vertex<String> v = otherGraph.insert("v");
    Vertex<String> u = otherGraph.insert("u");
    Edge<String> e = otherGraph.insert(v, u, "v-u");
    try {
      graph.label(e);
      fail("failed to throw exception");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("can successfully update label of edge")
  public void updateEdgeLabel() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e = graph.insert(v, u, "v-u");
    String l = "1";
    graph.label(e,l);
    assertEquals("1",graph.label(e));
    String newl = "2";
    graph.label(e,newl);
    assertEquals("2",graph.label(e));
  }

  //CLEAR LABELS TESTS
  @Test
  @DisplayName("all labels are null after clearLabels method call")
  public void clearLabelsSetsAllLabelsToNull() {
    Vertex<String> v = graph.insert("v");
    Vertex<String> u = graph.insert("u");
    Edge<String> e1 = graph.insert(v, u, "v-u");
    Edge<String> e2 = graph.insert(u,v,"u-v");
    String l = "1";
    graph.label(v,l);
    graph.label(u,l);
    graph.label(e1,l);
    graph.label(e2,l);
    graph.clearLabels();
    assertNull(graph.label(v));
    assertNull(graph.label(u));
    assertNull(graph.label(e1));
    assertNull(graph.label(e2));
  }
}
