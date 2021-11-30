package material.BinaryTree;

import material.Position;

import java.util.ArrayList;
import java.util.Iterator;

public class LinkedBinaryTree<E>  implements BinaryTree<E>{

    public LinkedBinaryTree() {

    }

    protected class BTreeNode<E> implements Position<E>{
        private E element;
        private BTreeNode<E> left , right , parent;

        public BTreeNode(E element, BTreeNode<E> left, BTreeNode<E> right, BTreeNode<E> parent) {
            this.element = element;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        public E getElement() {
            return element;
        }

        public BTreeNode<E> getLeft() {
            return left;
        }

        public BTreeNode<E> getRight() {
            return right;
        }

        public BTreeNode<E> getParent() {
            return parent;
        }

        public void setElement(E element) {
            this.element = element;
        }

        public void setLeft(BTreeNode<E> left) {
            this.left = left;
        }

        public void setRight(BTreeNode<E> right) {
            this.right = right;
        }

        public void setParent(BTreeNode<E> parent) {
            this.parent = parent;
        }

    }
    public Position<E> root;

    public LinkedBinaryTree(Position<E> root) {
        this.root = root;
    }

    public Position<E> getRoot() {
        return root;
    }

    public void setRoot(Position<E> root) {
        this.root = root;
    }

    @Override
    public Position<E> left(Position<E> p) throws RuntimeException {
        BTreeNode<E> treeNode = checkPosition(p);
        BTreeNode<E> leftPosition = treeNode.getLeft();
        if (leftPosition == null){
            throw new RuntimeException("This node has not left child.");
        }
        return leftPosition;
    }

    @Override
    public Position<E> right(Position<E> p) throws RuntimeException {
        BTreeNode<E> treeNode = checkPosition(p);
        BTreeNode<E> rightPosition = treeNode.getLeft();
        if (rightPosition == null){
            throw new RuntimeException("This node has not right child.");
        }
        return rightPosition;
    }

    @Override
    public boolean hasLeft(Position<E> p) {
        BTreeNode<E> treeNode = checkPosition(p);
        return (treeNode.left != null);
    }

    @Override
    public boolean hasRight(Position<E> p) {
        BTreeNode<E> treeNode = checkPosition(p);
        return (treeNode.right != null);
    }

    @Override
    public E replace(Position<E> p, E e) {
        BTreeNode<E> node = checkPosition(p);
        node.setElement(e);
        return p.getElement();
    }


    // Devuelve el hermano del nodo dado. LLama a su padre y le pide el hermano que no es el.
    @Override
    public Position<E> sibling(Position<E> p) throws RuntimeException {
        BTreeNode<E> treeNode = checkPosition(p);
        BTreeNode<E> parent = treeNode.parent;
        if(parent != null){
            BTreeNode<E> result;
            BTreeNode<E> left = parent.left;
            if(left == treeNode){
                result = parent.right;
            }else{
                result = parent.left;
            }
            if (result != null) {
                return result;
            }
        }
        throw new RuntimeException("No sibling.");
    }

    @Override
    public Position<E> insertLeft(Position<E> p, E e) throws RuntimeException {
        BTreeNode<E> treeNode = checkPosition(p);
        if (hasLeft(p)){
            throw new RuntimeException("This node has already a left child.");
        }
        BTreeNode<E> newElement = new BTreeNode<>(e, null,null, treeNode);
        treeNode.setLeft(newElement);
        return newElement;
    }

    @Override
    public Position<E> insertRight(Position<E> p, E e) throws RuntimeException {
        BTreeNode<E> treeNode = checkPosition(p);
        Position<E> position = treeNode.getRight();
        if (position != null){
            throw new RuntimeException("This node has already a left child.");
        }
        BTreeNode<E> newElement = new BTreeNode<>(e, null,null, treeNode);
        treeNode.setRight(newElement);
        return newElement;
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public int size() {
        int size = 0;
        for(Position<E> position : this){
            size++;
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (root == null);
    }

    @Override
    public Position<E> root() throws RuntimeException {
        // No podemos devolver en EDA un null NUNCA
        if(root == null){
            throw new RuntimeException("This tree is empty.");

        }
        return root;
    }

    @Override
    public Position<E> parent(Position<E> v) throws RuntimeException {
        BTreeNode<E> treeNode = checkPosition(v);
        // Comprobamos que no es raiz ya que la raz es el único nodo que no tiene padre.
        if(isRoot(v)){
            throw new RuntimeException("This node has not parent.");
        }
        return treeNode.parent;
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        /*BTreeNode<E> treeNode = checkPosition(v);
        ArrayList<Position<E>> arrayList = new ArrayList<>();
        if(hasLeft(v)){
            arrayList.add(treeNode.left);
        }
        if (hasRight(v)){
            arrayList.add(treeNode.right);
        }
        return arrayList;*/

        ArrayList<Position<E>> arrayList = new ArrayList<>();
        if(hasLeft(v)){
            arrayList.add(left(v));
        }
        if (hasRight(v)){
            arrayList.add(right(v));
        }
        return arrayList;
    }

    @Override
    public boolean isInternal(Position<E> v) {
        checkPosition(v);
        return hasLeft(v) || hasRight(v);
    }

    @Override
    public boolean isLeaf(Position<E> v) throws RuntimeException {
        return (!isInternal(v));
    }

    @Override
    public boolean isRoot(Position<E> v) {
        checkPosition(v);
        return v == root();
    }

    @Override
    public Position<E> addRoot(E e) throws RuntimeException {
        return null;
    }


    @Override
    public Iterator<Position<E>> iterator() {
        return null;
    }

    private BTreeNode<E> checkPosition(Position<E> position){
        if (!(position instanceof BTreeNode) || position == null){
                throw new RuntimeException("Tha position is invalid.");
        }
        return     (BTreeNode<E>)position;

    }

    // solo borramos si tiene uno o ningún hijo
    @Override
    public E remove(Position<E> position) throws RuntimeException{
        BTreeNode<E> node = checkPosition(position);
        BTreeNode<E> leftNode = node.left;
        BTreeNode<E> rightNode = node.right;
        if ((leftNode != null ) && (rightNode != null)){
            // si tiene ambos hijos no podemos borrar
            throw new RuntimeException("Cannot remove node with two childen.");
        }
        BTreeNode<E> child = leftNode != null ? leftNode : rightNode;
        if(node == root){
            if (child != null){
                child.setParent(null);
            }
            root = child;
        }else{
            // si no es la raiz conseguimos el padre del node
            BTreeNode<E> parent =  node.parent;
            if(parent.left == node){
                parent.setLeft(child);
            }else {
                parent.setRight(child);
            }
            if (child != null){
                child.setParent(parent);
            }
        }
        return position.getElement();
    }
    @Override
    public void swap(Position<E> p1, Position<E> p2) {
        BTreeNode<E> node1 = checkPosition(p1);
        BTreeNode<E> node2 = checkPosition(p2);

        BTreeNode<E> copyNode1 = new BTreeNode<>(node1.element, node1.parent, node1.left, node1.right);

        node1.parent = node2.parent == node1 ? node2 : node2.parent;
        node1.left = node2.left == node1 ? node2 : node2.left;
        node1.right = node2.right == node1 ? node2 : node2.right;

        node2.parent = copyNode1.parent == node2 ? node1 : copyNode1.parent;
        node2.left = copyNode1.left == node2 ? node1 : copyNode1.left;
        node2.right = copyNode1.right == node2 ? node1 : copyNode1.right;

        if (node1.parent != null) {
            if (node1.parent.left == node2) {
                node1.parent.left = node1;
            } else {
                node1.parent.right = node1;
            }
        } else {
            this.root = node1;
        }

        if (node2.parent != null) {
            if (node2.parent.left == node1) {
                node2.parent.left = node2;
            } else {
                node2.parent.right = node2;
            }
        } else {
            root = node2;
        }

        if (this.hasLeft(node1)) {
            node1.left.parent = node1;
        }
        if (this.hasRight(node1)) {
            node1.right.parent = node1;
        }
        if (this.hasLeft(node2)) {
            node2.left.parent = node2;
        }
        if (this.hasRight(node2)) {
            node2.right.parent = node2;
        }
    }


    @Override
    public BinaryTree<E> subTree(Position<E> v) {
        BTreeNode<E> newRoot = checkPosition(v);

        if (newRoot == root) {
            root = null;
        } else {
            if (newRoot.parent.left == newRoot)
                newRoot.parent.left = null;
            else
                newRoot.parent.right = null;
        }

        newRoot.parent = null;

        LinkedBinaryTree<E> tree = new LinkedBinaryTree<>();
        tree.root = newRoot;
        return tree;
    }

    @Override
    public void attachLeft(Position<E> p, BinaryTree<E> tree) throws RuntimeException {
        //TODO: Este metodo depende de que tree sea del mismo tipo que this
        BTreeNode<E> node = checkPosition(p);

        if (tree == this) {
            throw new RuntimeException("Cannot attach a tree over himself");
        }
        if (this.hasLeft(p)) {
            throw new RuntimeException("Node already has a left child");
        }


        if (tree != null && !tree.isEmpty()) {
            //Check position will fail if tree is not an instance of LinkedBinaryTree
            BTreeNode<E> r = checkPosition(tree.root());
            node.setLeft(r);
            r.setParent(node);

            //The source tree will be left empty
            LinkedBinaryTree<E> lbt = (LinkedBinaryTree<E>) tree; //safe cast, checkPosition would fail first
            lbt.root = null;
        }
    }

    @Override
    public void attachRight(Position<E> p, BinaryTree<E> tree) throws RuntimeException {
        //TODO: Este metodo depende de que tree sea del mismo tipo que this
        BTreeNode<E> node = checkPosition(p);

        if (tree == this) {
            throw new RuntimeException("Cannot attach a tree over himself");
        }
        if (this.hasRight(p)) {
            throw new RuntimeException("Node already has a right child");
        }

        if (tree != null && !tree.isEmpty()) {
            //Check position will fail if tree is not an instance of LinkedBinaryTree
            BTreeNode<E> r = checkPosition(tree.root());
            node.setRight(r);
            r.setParent(node);

            //The source tree will be left empty
            LinkedBinaryTree<E> lbt = (LinkedBinaryTree<E>) tree; //safe cast, checkPosition would fail first
            lbt.root = null;
        }
    }
}
