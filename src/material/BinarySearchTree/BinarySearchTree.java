package material.BinarySearchTree;

import material.Position;

public interface BinarySearchTree<E> extends Iterable<Position<E>> {
    // Dictinoray headers
    Position<E> find(E value);
    Iterable<? extends Position<E>> findAll(E value);
    Position<E> insert(E value);
    boolean isEmpty();
    void remove(Position<E> pos) throws IllegalStateException;
    int size();
}
