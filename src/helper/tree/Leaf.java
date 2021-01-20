package helper.tree;

import java.util.ArrayList;
import java.util.List;

public class Leaf<T> {
    private T data;
    private Leaf<T> parent;
    private List<Leaf<T>> children;

    public Leaf() {
        children = new ArrayList<>();
    }

    public void addChildren(Leaf<T> newChild) {
        children.add(newChild);
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<Leaf<T>> getChildren() {
        return children;
    }

    public Leaf<T> getParent() {
        return parent;
    }

    public T getData() {
        return data;
    }
}
