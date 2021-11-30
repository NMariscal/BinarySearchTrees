package material.BinarySearchTree;

import material.BinaryTree.LinkedBinaryTree;
import material.Position;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class LinkedBinarySearchTree<E> implements BinarySearchTree<E> {
    protected LinkedBinaryTree<E> binaryTree;
    protected Comparator<E> comparator;

    /***
     * Creates a Binary Search Tree with the default comparator.
     */
    public LinkedBinarySearchTree() {
        // Refers to the constructor below
        this (null);
    }

    /***
     *Creates a Binary Search Tree with the given comparator.
     */
    public LinkedBinarySearchTree(Comparator<E> comparator) {
        if(comparator == null){
            this.comparator = new DefaultComparator<>();
        }else{
            this.comparator = comparator;
        }
        this.binaryTree = new LinkedBinaryTree<>(); // The silly one
    }

    /***
     * Auxiliary method used by find, insert and remove.
     * We pass the element we want to search and from what position.
     * @return the position where value is stored.
     */
    protected Position<E> treeSearch(E value , Position<E> position)throws IllegalStateException, IndexOutOfBoundsException{
        E currentValue = position.getElement();
        int comparator = this.comparator.compare(value, currentValue);
        if ((comparator < 0) && (this.binaryTree.hasLeft(position))){
            return treeSearch(value, this.binaryTree.left(position));
        }else if ((comparator > 0) && (this.binaryTree.hasRight(position))){
            return treeSearch(value, this.binaryTree.right(position));
        }
        return position;
    }

    /***
     * Adds to a list all entries in the subtree rooted at eposition having heys equal to k.
     */
    protected void addAll(List<Position<E>> list, Position<E> ePosition, E value){
        Position<E> position = treeSearch(value, ePosition);
    }

    @Override
    public Position<E> find(E value) {
        return null;
    }

    @Override
    public Iterable<? extends Position<E>> findAll(E value) {
        return null;
    }

    @Override
    public Position<E> insert(E value) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void remove(Position<E> pos) throws IllegalStateException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return null;
    }
}
