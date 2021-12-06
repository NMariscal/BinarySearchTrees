package material.BinarySearchTree;

import material.BinaryTree.LinkedBinaryTree;
import material.Position;
import material.iterators.BFSIterator;
import material.iterators.InorderBinaryTreeIterator;

import java.util.ArrayList;
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
        boolean appear = false;
        while((comparator.compare(value, position.getElement()) == 0)&& (!appear)){
            list.add(position);
            if (this.binaryTree.hasRight(position)){
                position = treeSearch(value, this.binaryTree.right(position));
            }else{
                // if do not has right , break
                appear = true;
            }
        }
    }

    /***
     * Find the position of the value that we pass
     * @return the Position where is the value
     */
    @Override
    public Position<E> find(E value) {
        if (this.binaryTree.size() == 0){
            return null;
        }
        Position<E> currentPosition = treeSearch(value, this.binaryTree.root());
        if (comparator.compare(value, currentPosition.getElement()) == 0){
            return currentPosition;
        }else{
            return null;
        }
    }

    @Override
    public Iterable<? extends Position<E>> findAll(E value) {
        // Add to a list all the equal values (position.getElement() == value)
       List<Position<E>> positionList= new ArrayList<>();
       if (!this.binaryTree.isEmpty()){
           addAll(positionList, this.binaryTree.root(), value);
       }
        return positionList;
    }


    @Override
    public Position<E> insert(E value) {
        if(this.binaryTree.isEmpty()){
            return this.binaryTree.addRoot(value);
        }
        Position<E> insertPosition = treeSearch(value, this.binaryTree.root());
        if (comparator.compare(value, insertPosition.getElement()) == 0){ // if they are equals
            // To consider nodes already in the tree with the same key
            while(!this.binaryTree.isLeaf(insertPosition) && this.binaryTree.hasRight(insertPosition)){
                /// iterative search for insertion position
                insertPosition = treeSearch(value, this.binaryTree.right(insertPosition));
            }
        }
        Position<E> returnPosition;
        if (comparator.compare(value, insertPosition.getElement()) < 0){
            returnPosition = this.binaryTree.insertLeft(insertPosition, value);
        }else{
            returnPosition = this.binaryTree.insertRight(insertPosition, value);
        }
        return returnPosition;
    }

    @Override
    public boolean isEmpty() {
        return this.binaryTree.size() == 0;
    }

    @Override
    public void remove(Position<E> pos) throws IllegalStateException {
        if (this.binaryTree.isLeaf(pos) || !this.binaryTree.hasLeft(pos) || !this.binaryTree.hasRight(pos)){
            this.binaryTree.remove(pos);
        }else{
            Position<E> sucessor = sucessor(pos);
            this.binaryTree.swap(sucessor, pos);
            this.binaryTree.remove(pos);
        }
    }

    /***
     * We have to find in all tree the next node that have like element the NEXT number.
     */
    private Position<E> sucessor(Position<E> pos) {
        // If we have right , the sucessor is in the right branch. Because we are finding t
        // he next number , greater than the given position
       if(this.binaryTree.hasRight(pos)){
           return minium(this.binaryTree.right(pos));
       }
       // If the given position is a root we return null.
       if (this.binaryTree.root() == pos){
           return null;
       }
       // Else, we have to find the sucessor in the tree
        // If aux is not a root, aux is the parent of position
       //Position<E> aux = this.binaryTree.root() == pos ? null : this.binaryTree.parent(pos);
        Position<E> aux = this.binaryTree.parent(pos);
       // if the given position parent has right we store it in the variable rightChild
       //Position<E> rightChild = this.binaryTree.hasRight(aux) ? this.binaryTree.right(pos) : null;
       // While the node has parent and
       /*while(aux != null && pos == rightChild){
           pos = aux;
           aux = this.binaryTree.root() == pos ? null : this.binaryTree.right(pos);
           if(aux != null){
               rightChild = this.binaryTree.hasRight(aux) ? this.binaryTree.right(aux) : null;
           }
       }*/
        while(aux != null && this.comparator.compare(aux.getElement(), pos.getElement()) < 0){
            if (!this.binaryTree.isRoot(aux)){
                aux = this.binaryTree.parent(aux);
            }else{
                aux = null;
            }
        }
        return aux;
    }

    private Position<E> minium(Position<E> position) {
        while(this.binaryTree.hasRight(position)){
            position = this.binaryTree.left(position);
        }
        return position;
    }

    @Override
    public int size() {
        return this.binaryTree.size();
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new InorderBinaryTreeIterator<>(this.binaryTree);
    }

    // Aditional functions :

    /***
     * Return the first element of a tree. The first element of a tree is the one that is located farthest to the left. The smallest
     */
    public Position<E> first(){
        // Start in the root
        Position<E> root = this.binaryTree.root();
        while (binaryTree.hasLeft(root)){
            root = binaryTree.left(root);
        }
        return root;
    }

    /***
     * Return the last element of a tree. The last element of a tree is the one that is located farthest to the right. The greatest
     */
    public Position<E> last(){
        // Start in the root
        Position<E> root = this.binaryTree.root();
        while (binaryTree.hasRight(root)){
            root = binaryTree.right(root);
        }
        return root;
    }


}
