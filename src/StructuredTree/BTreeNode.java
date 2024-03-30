package StructuredTree;

import java.util.ArrayList;

public class BTreeNode <E extends Comparable<E>>{

    private int order;
    private boolean isLeaf;
    private boolean isRoot;
    private ArrayList<E> keys;
    private ArrayList<BTreeNode<E>> childrens;



    public BTreeNode(int order) {
        this.order = order;
        keys = new ArrayList<>();
        childrens = new ArrayList<>();
        isLeaf = true;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public ArrayList<E> getKeys() {
        return keys;
    }


    public int getOrder() {
        return order;
    }

    public ArrayList<BTreeNode<E>> getChildrens() {
        return childrens;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }






}

