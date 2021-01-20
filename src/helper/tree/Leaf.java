package helper.tree;

import gui.TableController;
import helper.TableContent;

import java.util.ArrayList;
import java.util.List;

public class Leaf {
    private TableContent tableContent;
    private Leaf parent;
    private List<Leaf> children;

    public Leaf() {
        children = new ArrayList<>();
    }

    public void addChildren(Leaf newChild) {
        children.add(newChild);
    }

    public void setParent(Leaf parent) {
        this.parent = parent;
    }

    public void setData(TableContent tableContent) {
        this.tableContent = tableContent;
    }

    public List<Leaf> getChildren() {
        return children;
    }

    public Leaf getParent() {
        return parent;
    }

    public TableContent getData() {
        return tableContent;
    }

    public Leaf getChildren(String name) {
        Leaf searchedChild = null;

        for(Leaf child: children) {
            if(child.getData().getName().equals(name)) {
                searchedChild = child;
            }
        }

        return searchedChild;
    }
}
