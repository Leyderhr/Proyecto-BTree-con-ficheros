import StructuredTree.BTree;

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

        System.out.println("Leyder es un puto y Adrian es un Gigollo");
    }
}