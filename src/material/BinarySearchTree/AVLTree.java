package material.BinarySearchTree;

import material.Position;
import  material.BinarySearchTree.LinkedBinarySearchTree.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// This class is necesary for save the height value of the AVL tree (BTnodes) nodes.
public class AVLTree<E> implements BinarySearchTree<E>{
    private class AVLInfo<T> implements Comparable<AVLInfo<T>>, Position<T>{
        private int height;
        private T element;
        private Position<AVLInfo<T>> avlInfoPosition;

        public AVLInfo(T element) {
            this.element = element;
            this.avlInfoPosition = null;
            this.height = 1;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setElement(T element) {
            this.element = element;
        }

        public Position<AVLInfo<T>> getAvlInfoPosition() {
            return avlInfoPosition;
        }

        public void setAvlInfoPosition(Position<AVLInfo<T>> avlInfoPosition) {
            this.avlInfoPosition = avlInfoPosition;
        }

        @Override
        public int compareTo(AVLInfo<T> tavlInfo) {
            if(element instanceof Comparable && tavlInfo.element instanceof Comparable){
                Comparable<T> c1 = (Comparable<T>) element;
                return c1.compareTo(tavlInfo.element);
            }else{
                throw new ClassCastException("Element is not comparable.");
            }
        }

        @Override
        public boolean equals(Object o) {
            AVLInfo<T> info = (AVLInfo<T>) o;
            return element.equals(info.getElement());
        }

        @Override
        public String toString() {
            return element.toString();
        }

        @Override
        public T getElement() {
            return null;
        }
    }
    private class AVLTreeIterator<T> implements Iterator<Position<T>> {

        private Iterator<Position<AVLInfo<T>>> it;

        public AVLTreeIterator(Iterator<Position<AVLInfo<T>>> iterator) {
            this.it = iterator;
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Position<T> next() {
            Position<AVLInfo<T>> aux = it.next();
            return aux.getElement();
        }

        @Override
        public void remove() {
            it.remove();
        }
    }

    private LinkedBinarySearchTree<AVLInfo<E>> binarySearchTree;
    private ReestructurableBinaryTree<AVLInfo<E>> reestructurableBinaryTree;

    public AVLTree() {
        this.reestructurableBinaryTree = new ReestructurableBinaryTree<>();
        this.binarySearchTree = new LinkedBinarySearchTree<>();
        binarySearchTree.binaryTree = reestructurableBinaryTree;
    }


    @Override
    public Position<E> find(E value) {
        AVLInfo<E> searchedValue = new AVLInfo<>(value);
        Position<AVLInfo<E>> output = binarySearchTree.find(searchedValue);
        return (output == null) ? null : output.getElement();
    }

    @Override
    public Iterable<Position<E>> findAll(E value) {
        AVLInfo<E> searchedValue = new AVLInfo<>(value);
        List<Position<E>> aux = new ArrayList<>();
        for (Position<AVLInfo<E>> n : binarySearchTree.findAll(searchedValue)) {
            aux.add(n.getElement());
        }
        return aux;
    }

    /**
     * Returns whether a node has balance factor between -1 and 1.
     */
    private boolean isBalanced(Position<AVLInfo<E>> p) {
        int leftHeight = (binarySearchTree.binaryTree.hasLeft(p)) ? binarySearchTree.binaryTree.left(p).getElement().getHeight() : 0;
        int rightHeight = (binarySearchTree.binaryTree.hasRight(p)) ? binarySearchTree.binaryTree.right(p).getElement().getHeight() : 0;
        final int bf = leftHeight - rightHeight;
        return ((-1 <= bf) && (bf <= 1));
    }

    /**
     * Return a child of p with height no smaller than that of the other child.
     */
    private Position<AVLInfo<E>> tallerChild(Position<AVLInfo<E>> p) {

        int leftHeight = (binarySearchTree.binaryTree.hasLeft(p)) ? binarySearchTree.binaryTree.left(p).getElement().getHeight() : 0;
        int rightHeight = (binarySearchTree.binaryTree.hasRight(p)) ? binarySearchTree.binaryTree.right(p).getElement().getHeight() : 0;

        if (leftHeight > rightHeight) {
            return binarySearchTree.binaryTree.left(p);
        } else if (leftHeight < rightHeight) {
            return binarySearchTree.binaryTree.right(p);
        }

        // equal height children - break tie using parent's type
        if (binarySearchTree.binaryTree.isRoot(p)) {
            return binarySearchTree.binaryTree.left(p);
        }

        if (p == binarySearchTree.binaryTree.left(binarySearchTree.binaryTree.parent(p))) {
            return binarySearchTree.binaryTree.left(p);
        } else {
            return binarySearchTree.binaryTree.right(p);
        }
    }

    private void calculateHeight(Position<AVLInfo<E>> p) {
        int leftHeight = (binarySearchTree.binaryTree.hasLeft(p)) ? binarySearchTree.binaryTree.left(p).getElement().getHeight() : 0;
        int rightHeight = (binarySearchTree.binaryTree.hasRight(p)) ? binarySearchTree.binaryTree.right(p).getElement().getHeight() : 0;
        p.getElement().setHeight(1 + Math.max(leftHeight, rightHeight));
    }

    /**
     * Rebalance method called by insert and remove. Traverses the path from p
     * to the root. For each node encountered, we recompute its height and
     * perform a trinode restructuring if it's unbalanced.
     */
    private void rebalance(Position<AVLInfo<E>> zPos) {
        if (binarySearchTree.binaryTree.isInternal(zPos)) {
            calculateHeight(zPos);
        } else {
            zPos.getElement().setHeight(0);
        }
        while (!binarySearchTree.binaryTree.isRoot(zPos)) { // traverse up the tree towards the
            // root
            zPos = binarySearchTree.binaryTree.parent(zPos);
            calculateHeight(zPos);
            if (!isBalanced(zPos)) {
                // perform a trinode restructuring at zPos's tallest grandchild
                Position<AVLInfo<E>> xPos = tallerChild(tallerChild(zPos));
                zPos = this.reestructurableBinaryTree.restructure(xPos, binarySearchTree);
                calculateHeight(binarySearchTree.binaryTree.left(zPos));
                calculateHeight(binarySearchTree.binaryTree.right(zPos));
                calculateHeight(zPos);
            }
        }
    }

    @Override
    public Position<E> insert(E value) {
        AVLInfo<E> aux = new AVLInfo<>(value);
        Position<AVLInfo<E>> internalNode = binarySearchTree.insert(aux);
        aux.setAvlInfoPosition(internalNode); // apunta a si mismo
        rebalance(internalNode);
        return aux;
    }

    @Override
    public boolean isEmpty() {
        return binarySearchTree.isEmpty();
    }

    @Override
    public void remove(Position<E> pos) throws IllegalStateException {

        AVLInfo<E> avlInfo = (AVLInfo<E>) pos;
        Position<AVLInfo<E>> treePos = avlInfo.getAvlInfoPosition();

        Position<AVLInfo<E>> parent = null;

        if (reestructurableBinaryTree.isLeaf(treePos) || !reestructurableBinaryTree.hasLeft(treePos) || !reestructurableBinaryTree.hasRight(treePos)) {
            if (reestructurableBinaryTree.root() != treePos)
                parent = reestructurableBinaryTree.parent(treePos);
            reestructurableBinaryTree.remove(treePos);
        } else {
            Position<AVLInfo<E>> sucessor = binarySearchTree.sucessor(treePos);
            reestructurableBinaryTree.swap(sucessor,treePos);
            if (reestructurableBinaryTree.root() != treePos)
                parent = reestructurableBinaryTree.parent(treePos);
            reestructurableBinaryTree.remove(treePos);
        }

        if (parent != null) {
            rebalance(parent);
        }
    }

    @Override
    public int size() {
        return binarySearchTree.size();
    }

    @Override
    public Iterator<Position<E>> iterator() {
        Iterator<Position<AVLInfo<E>>> bstIt = binarySearchTree.iterator();
        AVLTreeIterator<E> it = new AVLTreeIterator<E>(bstIt);
        return it;
    }

    /**
     * Returns the position with the smallest value in the tree.
     */
    //PRACTICA 5 ejercicio 2, metodo first para el AVLTree
    //*********************************************************************
    //En los AVL, se ponen a la izquierda siempre el mas bajo
    //y a la derecha siempre el mas alto
    //por lo que si nos poden el mas bajo, solo tenemos que hacer un hasLeft, hasta que ya
    //no existan mas nodos a la izquierda y devolver la hoja que este más a la izquierda
    public Position<E> first() {
        //private LinkedBinarySearchTree<AVLInfo<E>> bst;
        //Tenemos bst, que utiliza LinkedBinarySearchTree, por lo que podemos llamar a la funcion
        //first() que hemos creado primero en la clase linkedBinarySearchTree

        //Se guarda en el position
        Position<E> nodo = binarySearchTree.first().getElement();
        //Se devuelve
        return nodo;
    }

    //**Returns the position with the smallest value in the tree. */
    //PRACTICA 5 ejercicio 2, metodo last para el LinkedBinarySearchoTree
    //*********************************************************************
    //En los AVL, se ponen a la izquierda siempre el mas bajo
    //y a la derecha siempre el mas alto
    //por lo que si nos piden el mas alto, solo tenemos que hacer un hasRight, hasta que ya
    //no existan mas nodos a la derecha y devolver la hoja que este más a la derecha
    public Position<E> last() {
        //private LinkedBinarySearchTree<AVLInfo<E>> bst;
        //Tenemos bst, que utiliza LinkedBinarySearchTree, por lo que podemos llamar a la funcion
        //last() que hemos creado primero en la clase linkedBinarySearchTree
        //Se guarda en el position
        Position<E> nodo = binarySearchTree.last().getElement();
        //Se devuelve
        return nodo;
    }
}
