package StructuredTree;


import java.util.*;

public class BTree<E extends Comparable<E>> {

    private BTreeNode<E> root;

    public BTree(int order) {
        root = new BTreeNode<>(order);
    }

    public void setRoot(BTreeNode<E> root) {
        this.root = root;
    }

    public BTreeNode<E> getRoot() {
        return root;
    }

    /**
     * Metodo de insercion general del arbol, controla todo el crecimiento
     * <li>value: es la nueva key que ingresamos al arbol</li>
     */
    public void insert(E value) {
        BTreeNode<E> r = root;
        if (r.getKeys().size() == r.getOrder() - 1 && r.isLeaf()) {
            BTreeNode<E> s = new BTreeNode<>(r.getOrder());
            root = s;
            insertNonFull(r, value);
            s.getChildrens().add(r);
            splitChild(s, 0, r);
        } else {
            insertNonFull(r, value);
        }
    }


    /**
     * Seperar nodos hijos
     * <li>Parametros:</li>
     * <ul>x: es el nodo padre donde se encuentra el hijo<ul/>
     * <ul>y: nodo hijo que deseamos dividir
     */
    private void splitChild(BTreeNode<E> x/*Nodo padre*/, int i, BTreeNode<E> y/*Nodo hijo que se va a dividir*/) {
        BTreeNode<E> z = new BTreeNode<>(root.getOrder());

        z.setLeaf(y.isLeaf());
        x.getChildrens().add(i + 1, z);
        x.getKeys().add(i, y.getKeys().get(root.getOrder() / 2));
        //Eliminar el pivote de x
        z.getKeys().addAll(y.getKeys().subList((root.getOrder() / 2), root.getOrder()));
        y.getKeys().subList((root.getOrder() / 2), root.getOrder()).clear();
        z.getKeys().remove(0);

        x.setLeaf(x.getChildrens().isEmpty());
        if (x.getKeys().size() == x.getOrder()) {
            BTreeNode<E> newRoot = new BTreeNode<>(root.getOrder());
            newRoot.getChildrens().add(0, x);
            splitRoot(newRoot, x);
        }
    }

    /**
     * Crear nuevos nodos raices si todos estan llenos
     * <li>Parametros:</li>
     * <ul>x: es el nodo padre donde se encuentra el hijo (nueva raiz)<ul/>
     * <ul>y: nodo hijo que deseamos dividir (raiz que se va a dividir)
     */
    private void splitRoot(BTreeNode<E> x, BTreeNode<E> y) {
        int sizeY = y.getKeys().size();
        int pivote = findPivote(y.getKeys());
        BTreeNode<E> z = new BTreeNode<>(x.getOrder()); //nuevo hijo derecho de x

        x.getChildrens().add(z);
        x.getKeys().add(y.getKeys().get(pivote));//Agregando la antigua raiz a la raiz nueva

        z.getKeys().addAll(y.getKeys().subList(pivote + 1, sizeY));//Agregando a z los keys derechos de la antigua raiz
        z.getChildrens().addAll(y.getChildrens().subList(pivote + 1, y.getChildrens().size()));//Agrega hijo izquierdo a z
        z.setLeaf(z.getChildrens().isEmpty());
        y.getKeys().subList(pivote, y.getKeys().size()).clear();
        y.getChildrens().subList(pivote + 1, y.getChildrens().size()).clear();
        x.setLeaf(x.getChildrens().isEmpty());

        if ((root.getKeys().size() < root.getOrder()) && !searchInBTree(x, root.getKeys().get(0))) {
            root.getKeys().addAll(x.getKeys());
            x.getChildrens().remove(0);
            root.getChildrens().addAll(x.getChildrens());
        } else {
            setRoot(x);
        }
        if (root.getKeys().size() == root.getOrder()) {
            BTreeNode<E> newRoot = new BTreeNode<>(root.getOrder());
            newRoot.getChildrens().add(0, root);
            splitRoot(newRoot, root);
        }
    }


    /**
     * Insertar key (value) en un nodo con capacidad
     */
    private void insertNonFull(BTreeNode<E> x, E value) {
        int i = x.getKeys().size() - 1;

        if ((!searchInBTree(x, value) && !searchInBTree(root, value)) || x.getKeys().isEmpty()) {
            if (x.isLeaf()) {
                x.getKeys().add(value);
                Collections.sort(x.getKeys());
            } else {

                while (i >= 0 && value.compareTo(x.getKeys().get(i)) < 0) {
                    i--;
                }
                i++;
                if (x.getChildrens().get(i).getKeys().size() == root.getOrder() - 1 && (x.getChildrens().get(i).isLeaf())) {
                    x.getChildrens().get(i).getKeys().add(value);
                    Collections.sort(x.getChildrens().get(i).getKeys());
                    //Verificación de si el hijo izq. Tiene al menos 1 espacio disponible para agregarle una key

                    if (i != 0 && x.getKeys().size() >= i && x.getChildrens().get(i - 1).getKeys().size() < root.getOrder() - 1 && x.getChildrens().get(i - 1) != null) {
                        x.getChildrens().get(i - 1).getKeys().add(x.getKeys().get(i - 1));
                        x.getKeys().remove(i - 1);
                        x.getKeys().add(x.getChildrens().get(i).getKeys().get(0));
                        x.getChildrens().get(i).getKeys().remove(0);
                    }
                    //Verificación de si el hijo derecho tiene al menos 1 espacio disponible para agregarle una key
                    else if (x.getKeys().size() > i + 1) {
                        if (x.getChildrens().get(i + 1) != null && x.getChildrens().get(i + 1).getKeys().size() < root.getOrder() - 1) {
                            x.getChildrens().get(i + 1).getKeys().add(0, x.getKeys().get(i));
                            x.getKeys().remove(i);
                            E pos = x.getChildrens().get(i).getKeys().get(x.getChildrens().get(i).getKeys().size() - 1);
                            x.getKeys().add(pos);
                            x.getChildrens().get(i).getKeys().remove(pos);
                        }
                    }
                    //Dividimos nodo
                    else {
                        splitChild(x, i, x.getChildrens().get(i));
                    }
                    x = root;
                    i = x.getKeys().size() - 1;

                    if (value.compareTo(x.getKeys().get(i)) >= 0) {
                        i++;
                    }
                }

                insertNonFull(x.getChildrens().get(i), value);
            }
        }
    }


    /**
     * Metodo para buscar una key en un BTree
     * <li>root: raiz del arbol donde se busca</li>
     * <li>value: valor de la key que queremos buscar</li>
     */
    public boolean searchInBTree(BTreeNode<E> root, E value) {
        int i = 0;//indice de busqueda

        if (!(root == null)) {
            while (i < root.getKeys().size() && value.compareTo(root.getKeys().get(i)) > 0) {
                i++;
            }

            if (i < root.getKeys().size() && value == root.getKeys().get(i)) {
                return true;
            }

            if (root.isLeaf()) {
                return false;
            } else {
                return searchInBTree(root.getChildrens().get(i), value);
            }
        }
        return false;
    }


    /**
     * Metodo para buscar un key en el arbol, retorna el nodo donde se encuentra
     */
    public BTreeNode<E> searchInBTree1(BTreeNode<E> root, E value) {
        int i = 0;//indice de busqueda

        if (root != null) {
            while (i < root.getKeys().size() && value.compareTo(root.getKeys().get(i)) > 0) {
                i++;
            }

            if (i < root.getKeys().size() && value == root.getKeys().get(i)) {
                return root;
            }

            if (root.isLeaf()) {
                return null;
            } else {
                return searchInBTree1(root.getChildrens().get(i), value);
            }
        }

        return searchInBTree1(root.getChildrens().get(i), value);
    }


    /**
     * Metodos para imprimir el albol, uno imprime las claves con cada nivel y el otro todo el arbol
     */
    public void printBTree(BTreeNode<E> root) {
        printBTree(root, 0);
    }


    private void printBTree(BTreeNode<E> node, int level) {
        if (node != null) {
            for (int i = 0; i < node.getKeys().size(); i++) {
                if (!node.isLeaf()) {
                    printBTree(node.getChildrens().get(i), level + 1);
                }
                System.out.println("Level: " + level + " - Key: " + node.getKeys().get(i));

                if (!node.isLeaf() && i == node.getKeys().size() - 1) {
                    printBTree(node.getChildrens().get(i + 1), level + 1);
                }
            }
        }
    }

    public void printTree() {
        List<BTreeNode<E>> currLevel = new ArrayList<>();
        currLevel.add(this.root);

        while (!currLevel.isEmpty()) {
            List<BTreeNode<E>> nextLevel = new ArrayList<>();

            for (BTreeNode<E> node : currLevel) {
                System.out.print("[" + String.join(", ", node.getKeys().stream().map(String::valueOf).toArray(String[]::new)) + "] ");

                if (!node.isLeaf()) {
                    nextLevel.addAll(node.getChildrens());
                }
            }

            System.out.println();
            currLevel = nextLevel;
        }
    }


    /**
     * Metodo para calcular el pivote (valor central) en una lista
     * dependiendo de si el size es par o impar
     */
    private int findPivote(ArrayList<E> list) {
        int pivote;

        if (list.size() % 2 == 0) {
            pivote = (list.size() / 2) - 1;
        } else {
            pivote = (list.size()) / 2;
        }
        return pivote;
    }


    /**
     * Metodo para guardar la posicion de un key en un nodo
     * <li>Parametro node: Nodo en el que queremos buscar el key</li>
     * <li>Parametro key: Valor que queremos buscar</li>
     */
    private int getPosition(BTreeNode<E> node, E key) {
        int i = 0;
        int pos = -1;

        while (i < node.getKeys().size()) {
            if (key == node.getKeys().get(i)) {
                pos = i;
                break;
            } else if (key.compareTo(node.getKeys().get(i)) < 0) {
                break;
            }
            i += 1;
        }
        return pos;
    }


    /**
     * Metodo para encontrar el valor inmediato inferior para sustituir en el proceso de eliminar
     */
    private E getMinKey(BTreeNode<E> node) {
        while (!node.isLeaf()) {
            node = node.getChildrens().get(0);
        }
        return node.getKeys().get(0);
    }

    /**
     * Metodos de eliminar
     */
    public void delete(E key) {
        if (!(root == null))
            remove(root, key);
    }

    private void remove(BTreeNode<E> node, E key) {

        if (node != null) {
            int index = getPosition(node, key);
            if (index > -1 && node.isLeaf())
                node.getKeys().remove(index);
            else {
                BTreeNode<E> children;


                if (index > -1) {
                    children = node.getChildrens().get(index + 1);
                    E valor = getMinKey(children);

                    node.getKeys().set(index, valor);
                    remove(children, valor);
                } else {

                    index = node.findWhere(key);
                    children = !node.isLeaf() ? node.getChildrens().get(index) : null;
                    remove(children, key);
                }

                if (children != null && !nodeInRangeSize(children.getKeys().size(), children.getOrder())) {
                    staleKey(node, children, index);
                }
                /*Si el nodo se queda sin llaves, xq fusiono a sus hijos, establece a los hijos
                 como nueva raiz*/
                if (root.getKeys().isEmpty()) {
                    node.getChildrens().get(0).setRoot(true);
                    setRoot(node.getChildrens().get(0));
                }
            }
        }
    }

    public void staleKey(BTreeNode<E> node, BTreeNode<E> firstChild, int pos) {
        BTreeNode<E> secondChild = pos != 0 ? node.getChildrens().get(pos - 1) : node.getChildrens().get(pos + 1);

        if (pos != 0 && nodeInRangeSize(secondChild.getKeys().size() - 1, secondChild.getOrder())) {
            firstChild.addKey(node.getKeys().remove(pos - 1));
            node.addKey(secondChild.getKeys().remove(secondChild.getKeys().size() - 1));
        } else {
            if (pos != node.getChildrens().size() - 1 && nodeInRangeSize(secondChild.getKeys().size() - 1, secondChild.getOrder())) {
                firstChild.addKey(node.getKeys().remove(pos));
                node.addKey(secondChild.getKeys().remove(0));
            } else
                mergeSons(node, pos);
        }
    }


    /**
     * Metodo para unir los hijos de un nodo
     * <li>Parametro node: nodo padre</li>
     * <li>Paramtro pos: Posicion del nodo hijo que necesitamos rellenar</li>
     */
    public void mergeSons(BTreeNode<E> node, int pos) {
        BTreeNode<E> firstChild = node.getChildrens().get(pos);
        BTreeNode<E> secondChild;

        if (pos < node.getChildrens().size() - 1) {
            secondChild = node.getChildrens().remove(pos + 1);
            firstChild.getKeys().addAll(secondChild.getKeys());
            firstChild.getChildrens().addAll(secondChild.getChildrens());
            firstChild.addKey(node.getKeys().remove(pos));
        } else {
            secondChild = node.getChildrens().get(pos - 1);
            secondChild.getKeys().addAll(firstChild.getKeys());
            secondChild.getChildrens().addAll(firstChild.getChildrens());
            secondChild.addKey(node.getKeys().remove(pos - 1));
            node.getChildrens().remove(pos);
        }
    }


    /**
     * Metodo para calcular el rango minimo de un nodo
     * <li>Parametro size: Tamaño del array</li>
     * <li>Parametro order: Orden que debe tener el arbol (cantidad maxima de keys)</li>
     */
    public boolean nodeInRangeSize(int size, int order) {
        return size >= ((order - 1) / 2);
    }

}