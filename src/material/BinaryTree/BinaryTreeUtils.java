package material.BinaryTree;

import material.Position;

public class BinaryTreeUtils<E> {
    private LinkedBinaryTree<E> tree;

    public BinaryTreeUtils(LinkedBinaryTree<E> tree) {
        this.tree = tree;
    }

    // We say that a tree is perfect when all the intern nodes have two child
    public boolean isPerfect(){
        boolean aux = true;
        // recorrer el arbol y ver si es interno y si lo es ver si tiene los dos hijos
        for (Position<E> treeNode : tree){
            if (aux == true){
                if ((tree.isInternal(treeNode)) && (!tree.hasLeft(treeNode) || !tree.hasRight(treeNode))){
                    aux = false;
                }
            }
        }
        return aux;
    }

    // Ejercicio de examen : se dice que eun AB es zurdo si es vacío,
    // es una hoja o más de la mitad de sus descendientes están en el hijo izquierdo.
    public boolean isOdd(){
        boolean resultado = false;
        if (tree.isEmpty()){
            return true;
        }else{
            // sacamos la raiz
            Position<E> root = tree.root;
            return isOddAux(root);
        }
    }
    private boolean isOddAux(Position<E> position){
        boolean resultado = false;
        int derecha = 0;
        int izquierda = 0;
        if (tree.isLeaf(position)){
            return true;
        }else {
            // mas de la mitad de sus hijos estarán a la izda.
            if (tree.hasRight(position) &&(!tree.hasLeft(position)) ) {
               return false;
            }
            else if (tree.hasLeft(position) && (!tree.hasRight(position))) {
                return true;
            }else{
                return (numHijos(tree.left(position)) > numHijos(tree.right(position))) && (isOddAux(tree.left(position ))) && isOddAux(tree.right(position));
            }
        }
}

    private int numHijos(Position<E> position){
        int cont = 1;
        for (Position<E> p :tree){
            cont = cont + numHijos(p);
        }
        return cont;
    }
}
