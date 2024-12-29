package hw8.graph;

/**
 * Generic position interface.
 *
 * @param <T> the element type.
 */
public interface Position<T> {

  /**
   * Read element from this position.
   *
   * @return element at this position.
   */
  T get();
}
