package material.iterators;

import material.Position;
import material.Tree;


import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class InternalNodeIterator<E> implements Iterator<Position<E>> {
    // Tengo que devolver los nodos internos es decir los que no tienen hojas.
    private final Queue<Position<E>>  nodeQueue;
    private final Tree<E> tree;

    public InternalNodeIterator(Tree<E> tree) {
        nodeQueue = new ArrayDeque<>();
        this.tree = tree;
        if (!tree.isEmpty()) {
            nodeQueue.add(tree.root());
            Position<E> position = tree.root();
            if (tree.isInternal(position)) {
                nodeQueue.add(position);
            }
        }
    }

    @Override
    public boolean hasNext() {
        return  nodeQueue.size() > 0;
    }

    @Override
    public Position<E> next() {
        Position<E> aux = nodeQueue.remove();
        // Añado a la cola los hijos si no son hoja
        for(Position<E>position : tree.children(aux)){
            nodeQueue.add(position);
        }
        return aux;
    }
}
