package helper.tree;

import helper.TableContent;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    private Leaf root;

    public Tree(TableContent rootData) {
        root = new Leaf();
        root.setData(rootData);
    }

    public Leaf getRoot() {
        return root;
    }
}
