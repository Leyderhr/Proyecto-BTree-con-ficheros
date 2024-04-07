import StructuredTree.BTree;
import StructuredTree.BTreeNode;

import java.io.*;

public class Main {


    public static void main(String[] args) {

        BTree <Integer> b1 = new BTree<>(4);
        b1.insert(1);
        b1.insert(5);
        b1.insert(2);
        b1.insert(3);
        b1.insert(4);
        b1.insert(6);
        b1.insert(10);
        b1.insert(11);
        b1.insert(12);
        b1.insert(7);
        b1.insert(8);
        b1.insert(9);
        b1.insert(13);
        b1.insert(14);
        b1.insert(15);
        b1.insert(16);
        b1.insert(17);
        b1.insert(18);
        b1.insert(19);
        b1.insert(20);
        b1.printTree();

        try(ObjectOutputStream fichero = new ObjectOutputStream(new FileOutputStream("E:/Escuela de Leyder/Carrera Informática/2do año/1er Semestre/Estructura de Datos (ED)/2024/ProyectoFinalBTree/src/Util/CarFile.txt"))){
            b1.getRoot().writeObject(fichero, b1.getRoot());
            BTreeNode a = b1.getRoot().readObject(new ObjectInputStream(new FileInputStream("E:/Escuela de Leyder/Carrera Informática/2do año/1er Semestre/Estructura de Datos (ED)/2024/ProyectoFinalBTree/src/Util/CarFile.txt")));
        }catch (IOException e){
            System.out.println("No se ha encontrado el archivo");
        }

        b1.remove(20);
        b1.remove(10);
        b1.remove(5);
        b1.remove(6);
        b1.remove(11);
        b1.remove(15);
        b1.remove(18);
        b1.printTree();

        System.out.println(b1.find(20));

    }
}