package scanner;

import gui.TableController;
import helper.TableContent;
import helper.tree.Leaf;
import helper.tree.Tree;

import java.util.ArrayList;
import java.util.List;

public class DirectoryManager {

    private Tree fileTree;
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
            String pathDirs[] = path.split("/");
            int rootIndex = -1;
            // check if the tree is build with an available root
            for(int i=0; i<pathDirs.length; i++) {
                if(pathDirs[i].equals(fileTree.getRoot().getData().getName())) {
                    rootIndex = i;
                }
            }

            // if not, create new tree
            if(rootIndex == -1) {
                createTreeToPath(path, ignoreHiddenElements);
            } else {
                // if root is in the tree, get leaf from path
                Leaf pathDir = fileTree.getRoot();
                for(int i=rootIndex+1; i<pathDirs.length; i++) {
                    pathDir = pathDir.getChildren(pathDirs[i]);
                }

                List<TableContent> resultList = new ArrayList<>();

                for(Leaf leaf: pathDir.getChildren()) {
                    resultList.add(leaf.getData());
                }

                tableController.setTableContent(resultList);
            }
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

    public void setFileTree(Tree fileTree) {
        this.fileTree = fileTree;
    }

    public void calculatingFinished(Tree fileTree) {
        setFileTree(fileTree);
        getTableToPath(fileTree.getRoot().getData().getPath(), oldIgnoreHiddenElements);
    }
}
