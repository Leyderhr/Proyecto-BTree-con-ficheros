package Interface;

import StructuredTree.BTreeNode;

import java.io.*;

@SuppressWarnings("unchecked")
public class File_Access <E extends Comparable<E>>  {
    static FileOutputStream fichero;
    final static char MARKER = ')';
    final static char MARKER1 = '(';

    public void escribir(BTreeNode root, ObjectOutputStream fichero) {

        //try {
            //fichero = new FileOutputStream("E:/Escuela de Leyder/Carrera Informática/2do año/1er Semestre/Estructura de Datos (ED)/2024/ProyectoFinalBTree/src/Util/CarFile.txt");
            serialize(root, fichero);
//        } catch (FileNotFoundException e) {
//            System.out.println("No se ha encontrado el archivo");
//        }

    }

    private void serialize(BTreeNode root, ObjectOutputStream fichero) {

        // Base case
        if (root == null) {
            return;
        }

        for (int i = 0; i < root.getKeys().size(); i++) {
            // Else, store current node and recur for its children
            try {
                fichero.write((root.getKeys().get(i) + " ").getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (!root.getChildrens().isEmpty()) {
                try {
                    fichero.write((MARKER1 + " ").getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for (int j = 0; i < root.getOrder()  && i < root.getChildrens().size(); i++) {
                    serialize((BTreeNode) root.getChildrens().get(i), fichero);
                }

                // Store marker at the end of children
                try {
                    fichero.write((MARKER + " ").getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public BTreeNode leer(ObjectInputStream reader){
        try {
            return deSerialize(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BTreeNode deSerialize (ObjectInputStream reader) throws IOException {
        // Read next item from file. If there are no more items or next
        // item is marker, then return null to indicate same
        E val = (E) (Integer) reader.read();
        if (val.equals(-1)  || val.equals(MARKER) || val.equals(MARKER1)) {
            return null;
        }
        E  c =  val;

        // Else create node with this item and recur for children
        BTreeNode root = new BTreeNode((Integer) c);// Aqui hay que ver de que forma se saca el orden del arbol

        for (int i = 0; i < root.getOrder(); i++) {
            root.getChildrens().add(deSerialize(reader));
            if (root.getChildrens() == null) {
                break;
            }
        }

        return root;
    }

}
