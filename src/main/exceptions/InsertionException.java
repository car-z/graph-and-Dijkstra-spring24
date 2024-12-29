package exceptions;

/**
 * Exception for bad insertions.
 * <p>
 * Some data structures don't like certain insertions. For example, the Tree
 * interface doesn't allow insertRoot() if there's already a root, the Graph
 * interface doesn't allow insertEdge() if the edge would create a self-loop,
 * etc.</p>
 */
public class InsertionException extends RuntimeException {
  /**
   * Constructs a new InsertionException.
   */
  public InsertionException() {
  }

  /**
   * Constructs a new InsertionException with the specified detail message.
   *
   * @param message the detail message. The detail message is saved for
   *                later retrieval by the getMessage() method.
   */
  public InsertionException(String message) {
    super(message);
  }
}
