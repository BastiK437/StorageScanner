package helper.tree;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> {
    private Leaf<T> root;

    public Tree(T rootData) {
        root = new Leaf<T>();
        root.setData(rootData);
    }

    public Leaf<T> getRoot() {
        return root;
    }
}
