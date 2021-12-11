package material.BinarySearchTree;

import material.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RBTree<E> implements BinarySearchTree<E> {
    private class RBInfo<T> implements Comparable<RBInfo<T>>, Position<T>{
        private boolean isRed; // we add a color field to a BTNode
        private final T element;
        private Position<RBInfo<T>> rbInfoPosition;
        RBInfo(T element) {
            this.element = element;
        }

        public boolean isRed() {
            return isRed;
        }

        public void setRed(boolean red) {
            isRed = red;
        }

        public Position<RBInfo<T>> getRbInfoPosition() {
            return rbInfoPosition;
        }

        public void setRbInfoPosition(Position<RBInfo<T>> rbInfoPosition) {
            this.rbInfoPosition = rbInfoPosition;
        }

        /***
         * Just compare the key in ordered dictionaries
         */
        @Override
        public int compareTo(RBInfo<T> trbInfo) {
            if (element instanceof Comparable && trbInfo.element instanceof Comparable) {
                Comparable<T> c1 = (Comparable<T>) element;
                return c1.compareTo(trbInfo.element);

            } else {
                throw new ClassCastException("Element is not comparable");
            }
        }

        @Override
        public T getElement() {
            return null;
        }
    }

    private class RBTreeIterator<T> implements Iterator<Position<T>> {

        private final Iterator<Position<RBInfo<T>>> it;

        public RBTreeIterator(Iterator<Position<RBInfo<T>>> iterator) {
            this.it = iterator;
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Position<T> next() {
            Position<RBInfo<T>> aux = it.next();
            return aux.getElement();
        }

        @Override
        public void remove() {
            it.remove();
        }
    }

    private final LinkedBinarySearchTree<RBInfo<E>> binarySearchTree = new LinkedBinarySearchTree<>();
    private final ReestructurableBinaryTree<RBInfo<E>> reestructurableBinaryTree = new ReestructurableBinaryTree<>();

    public RBTree() {
        binarySearchTree.binaryTree = reestructurableBinaryTree;
    }

    @Override
    public Position<E> find(E value) {
        RBInfo<E> searchedValue = new RBInfo<>(value);
        Position<RBInfo<E>> output = binarySearchTree.find(searchedValue);

        if (output == null) {
            return null;
        }
        return output.getElement();
    }

    @Override
    public Iterable<? extends Position<E>> findAll(E value) {
        RBInfo<E> searchedValue = new RBInfo<>(value);
        List<Position<E>> aux = new ArrayList<>();
        for (Position<RBInfo<E>> n : binarySearchTree.findAll(searchedValue)) {
            aux.add(n.getElement());
        }
        return aux;
    }

    @Override
    public Position<E> insert(E value) {
        RBInfo<E> aux = new RBInfo<>(value);

        Position<RBInfo<E>> posZ = binarySearchTree.insert(aux);
        aux.setRbInfoPosition(posZ);
        aux.setRed(true);

        if (this.binarySearchTree.binaryTree.isRoot(posZ)) {
            aux.setRed(false);
        } else {
            remedyDoubleRed(aux); // fix a double-red color violation
        }
        return aux;
    }

    @Override
    public boolean isEmpty() {
        return binarySearchTree.isEmpty();
    }

    @Override
    public int size() {
        return binarySearchTree.size();
    }

    /**
     * Remedies a double red violation at a given node caused by insertion.
     * @param nodeZ
     */
    protected void remedyDoubleRed(RBInfo<E> nodeZ) {
        Position<RBInfo<E>> posV = this.binarySearchTree.binaryTree.parent(nodeZ.getRbInfoPosition());
        RBInfo<E> nodeV = posV.getElement();

        if (!nodeV.isRed()) {
            return;
        }
        // we have a double red: posZ and posV
        Position<RBInfo<E>> grandParent = this.binarySearchTree.binaryTree.parent(posV);
        boolean hasSibling = this.binarySearchTree.binaryTree.hasLeft(grandParent) && this.binarySearchTree.binaryTree.hasRight(grandParent);
        boolean blackUncle = true;
        if (hasSibling) {
            Position<RBInfo<E>> uncleZ = this.binarySearchTree.binaryTree.sibling(posV);
            blackUncle = !uncleZ.getElement().isRed;
        }
        if (blackUncle) { // Case 1: trinode restructuring
            posV = this.reestructurableBinaryTree.restructure(nodeZ.getRbInfoPosition(), this.binarySearchTree);
            posV.getElement().setRed(false);
            this.binarySearchTree.binaryTree.left(posV).getElement().setRed(true);
            this.binarySearchTree.binaryTree.right(posV).getElement().setRed(true);
        } else { // Case 2: recoloring
            nodeV.setRed(false);
            if (hasSibling) {
                this.binarySearchTree.binaryTree.sibling(posV).getElement().setRed(false);
            }
            Position<RBInfo<E>> posU = this.binarySearchTree.binaryTree.parent(posV);
            if (this.binarySearchTree.binaryTree.isRoot(posU)) {
                return;
            }
            RBInfo<E> nodeU = posU.getElement();
            nodeU.setRed(true);
            remedyDoubleRed(nodeU);
        }
    }

    @Override
    public void remove(Position<E> pos) throws IllegalStateException {
        RBInfo<E> rbInfo = (RBInfo<E>) pos;
        Position<RBInfo<E>> treePos = rbInfo.getRbInfoPosition();

        Position<RBInfo<E>> parent = null;

        if (reestructurableBinaryTree.isLeaf(treePos) || !reestructurableBinaryTree.hasLeft(treePos) || !reestructurableBinaryTree.hasRight(treePos)) {
            if (reestructurableBinaryTree.root() != treePos) {
                parent = reestructurableBinaryTree.parent(treePos);
            }
            reestructurableBinaryTree.remove(treePos);
        } else {
            Position<RBInfo<E>> sucessor = binarySearchTree.sucessor(treePos);
            reestructurableBinaryTree.swap(sucessor, treePos);
            final boolean colorSuccesor = sucessor.getElement().isRed;
            sucessor.getElement().setRed(treePos.getElement().isRed());
            treePos.getElement().setRed(colorSuccesor);
            if (reestructurableBinaryTree.root() != treePos) {
                parent = reestructurableBinaryTree.parent(treePos);
            }
            reestructurableBinaryTree.remove(treePos);
        }

        Position<RBInfo<E>> nodeR = null;
        if (reestructurableBinaryTree.hasLeft(treePos))
            nodeR = reestructurableBinaryTree.left(treePos);
        else if (reestructurableBinaryTree.hasRight(treePos))
            nodeR = reestructurableBinaryTree.right(treePos);

        if ((nodeR != null) && (nodeR.getElement().isRed)) {
            nodeR.getElement().setRed(false);
            return;
        }

        if (treePos.getElement().isRed) {
            return;
        }

        if (reestructurableBinaryTree.isEmpty()) {
            return;
        }

        remedyDoubleBlack(parent, nodeR);
    }

    /**
     * Remedies a double black violation at a given node caused by removal.
     */
    protected void remedyDoubleBlack(Position<RBInfo<E>> doubleBlackParent, Position<RBInfo<E>> doubleBlack) {
        Position<RBInfo<E>> posX = doubleBlackParent;
        Position<RBInfo<E>> posZ = null;
        Position<RBInfo<E>> posY = null;
        boolean YisLeftChildOfX;

        if (this.binarySearchTree.binaryTree.hasLeft(posX) && this.binarySearchTree.binaryTree.left(posX) != doubleBlack) {
            posY = this.binarySearchTree.binaryTree.left(posX);
            YisLeftChildOfX = true;
        } else {
            posY = this.binarySearchTree.binaryTree.right(posX);
            YisLeftChildOfX = false;
        }

        RBInfo<E> nodeX = doubleBlackParent.getElement();
        RBInfo<E> nodeY = posY.getElement();

        if (!nodeY.isRed()) {
            posZ = hasRedChild(posY); //posZ != null means that it at least one red childpren
            if (posZ != null) { // Case 1: trinode restructuring
                boolean oldColor = nodeX.isRed();
                Position<RBInfo<E>> posB = this.reestructurableBinaryTree.restructure(posZ, this.binarySearchTree); //After restrusturing posZ gets the value of midKey
                posB.getElement().setRed(oldColor);
                this.binarySearchTree.binaryTree.left(posB).getElement().setRed(false);
                this.binarySearchTree.binaryTree.right(posB).getElement().setRed(false);
                return;
            } else { // Case 2: recoloring
                nodeY.setRed(true);
                if (!nodeX.isRed()) { //If X is black we propagate the doble black problem to the root
                    if (!this.binarySearchTree.binaryTree.isRoot(posX)) { //If X is root the problem is solved
                        remedyDoubleBlack(this.binarySearchTree.binaryTree.parent(posX),posX);  //In other case propagate
                    }
                    return;
                }
                nodeX.setRed(false);
                return;
            }
        } // Case 3: adjustment

        if (YisLeftChildOfX && this.binarySearchTree.binaryTree.hasLeft(posY)) {
            posZ = this.binarySearchTree.binaryTree.left(posY);
        } else if (this.binarySearchTree.binaryTree.hasRight(posY)) {
            posZ = this.binarySearchTree.binaryTree.right(posY);
        }
        this.reestructurableBinaryTree.restructure(posZ, this.binarySearchTree);
        nodeY.setRed(false);
        nodeX.setRed(true);
        remedyDoubleBlack(doubleBlackParent, doubleBlack);
    }

    /**
     * Returns a red child of a node.
     * @param pos
     * @return
     */
    protected Position<RBInfo<E>> hasRedChild(Position<RBInfo<E>> pos) {
        Position<RBInfo<E>> child;
        if (this.binarySearchTree.binaryTree.hasLeft(pos)) {
            child = this.binarySearchTree.binaryTree.left(pos);
            final boolean redLeftChild = child.getElement() != null && child.getElement().isRed();
            if (redLeftChild) {
                return child;
            }
        }

        if (this.binarySearchTree.binaryTree.hasRight(pos)) {
            child = this.binarySearchTree.binaryTree.right(pos);
            final boolean redRightChild = child.getElement() != null && child.getElement().isRed();
            if (redRightChild) {
                return child;
            }
        }
        return null;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        Iterator<Position<RBInfo<E>>> bstIt = binarySearchTree.iterator();
        RBTreeIterator<E> it = new RBTreeIterator<E>(bstIt);
        return it;
    }

    /**
     * Returns the position with the smallest value in the tree.
     * @return
     */
    public Position<E> first() {

        //Se guarda en el position
        Position<E> nodo = binarySearchTree.first().getElement();
        //Se devuelve
        return nodo;
    }

    /**
     *
     * @return
     */
    public Position<E> last() {
        Position<E> nodo = binarySearchTree.last().getElement();
        //Se devuelve
        return nodo;
    }
}
