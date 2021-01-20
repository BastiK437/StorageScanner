package scanner;

import gui.TableController;
import helper.TableContent;
import helper.tree.Leaf;
import helper.tree.Tree;

import java.util.ArrayList;
import java.util.List;

public class DirectoryManager {

    private Tree<TableContent> fileTree;
    private TableController tableController;
    private DirectoryScanner directoryScanner;
    private boolean oldIgnoreHiddenElements;

    private Thread scannerThread;

    public DirectoryManager(TableController tableController) {
        this.tableController = tableController;
    }


    public void getTableToPath(String path, boolean ignoreHiddenElements) {
        if(ignoreHiddenElements != oldIgnoreHiddenElements) {
            oldIgnoreHiddenElements = ignoreHiddenElements;

            createTreeToPath(path, ignoreHiddenElements);
        } else if(fileTree == null) {
            createTreeToPath(path, ignoreHiddenElements);
        } else {
            List<TableContent> resultList = new ArrayList<>();

            for(Leaf<TableContent> leaf: fileTree.getRoot().getChildren()) {
                resultList.add(leaf.getData());
            }

            tableController.setTableContent(resultList);
        }
    }

    public void createTreeToPath(String path, boolean ignoreHiddenElements) {
        if( scannerThread != null && scannerThread.isAlive()) {
            scannerThread.interrupt();
        }

        directoryScanner = new DirectoryScanner(this, path, ignoreHiddenElements);
        scannerThread = new Thread(directoryScanner);

        scannerThread.start();
    }

    public void setFileTree(Tree<TableContent> fileTree) {
        this.fileTree = fileTree;
    }

    public void calculatingFinished(Tree<TableContent> fileTree) {
        setFileTree(fileTree);
        getTableToPath(fileTree.getRoot().getData().getPath(), oldIgnoreHiddenElements);
    }
}
