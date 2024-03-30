package StructuredTree;

import java.util.ArrayList;

public class BTreeNode <E extends Comparable<E>>{

    private int order;
    private boolean isLeaf;
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

    public Integer isRoot() {
        return (Integer)info;
    }

    public void setRoot(Integer isRoot) {
        this.info = (E)isRoot;
    }

    public E addKey(E key) {
        E add = null;

        if (!keys.isEmpty()) {
            if (!keys.contains(key))
                if (keys.size() < order - 1) {
                    int x = findWhere(key);
                    keys.add(x, key);
                } else
                    add = key;
        } else
            keys.add(key);
        return add;
    }

    public int findWhere(E key) {
        int x = -1;

        if (key.compareTo(keys.get(0)) < 0)
            x = 0;
        else if (key.compareTo(keys.get(keys.size() - 1)) > 0)
            x = keys.size();
        else {
            boolean agg = false;
            int left = 0;
            int right = keys.size() - 1;
            int mid;
            while (!agg) {
                mid = (right + left) / 2;

                if(key.compareTo(keys.get(mid)) == 0){
                    x = mid;
                    agg = true;
                }else if (key.compareTo(keys.get(mid)) > 0) {
                    if (key.compareTo(keys.get(mid + 1)) < 0) {
                        agg = true;
                        x = mid + 1;
                    }
                    left = mid + 1;
                } else {
                    if (key.compareTo(keys.get(mid - 1)) > 0) {
                        agg = true;
                        x = mid;
                    }
                    right = mid - 1;
                }
            }
        }
        return x;
    }

    public boolean isEmpty() {
        return keys.isEmpty() && childrens.isEmpty();
    }





}

