package scanner;

import gui.TableController;
import helper.TableContent;
import helper.tree.Leaf;
import helper.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DirectoryScanner implements Runnable{
    // extern
    private DirectoryManager directoryManager;


    // intern
    private String path;
    private List<TableContent> tContent;
    private boolean ignoreHiddenElements;
    private Tree<TableContent> fileTree;

    public DirectoryScanner(DirectoryManager directoryManager, String path, boolean ignoreHiddenElements) {
        this.directoryManager = directoryManager;

        this.path = path;
        this.ignoreHiddenElements = ignoreHiddenElements;
    }

    @Override
    public void run() {
        File file = new File(path);

        if(file.isFile() || !file.exists()) {
            System.out.printf("file or dir not existing\n");
            return;
        }

        if(file.isDirectory()) {
            fileTree = new Tree<>(new TableContent(file.getName()));
            createTree(file, fileTree.getRoot());
        }

        directoryManager.calculatingFinished(fileTree);

        System.out.printf("Scan finished\n");
    }

    public long createTree(File dir, Leaf<TableContent> root) {
        long result = 0;

        if(ignoreHiddenElements && dir.getName().startsWith(".")){
            return 0;
        }

        File fileList[] = dir.listFiles();
        if(fileList != null) {
            for (File f : fileList) {
                // check for interruption on the thread
                if(Thread.currentThread().isInterrupted()) {
                    Thread.currentThread().interrupt();
                    break;
                }
                if(ignoreHiddenElements && f.getName().startsWith(".")){
                    continue;
                }
                // check if it is the proc/ dir
                if(f.getName().equals("proc")){
                    continue;
                }

                TableContent newContent = new TableContent(f.getName());
                newContent.setPath(f.getPath());

                // set new leaf in tree
                Leaf<TableContent> newLeaf = new Leaf<>();
                newLeaf.setData(newContent);
                root.addChildren(newLeaf);

                if(Thread.currentThread().isInterrupted()) {
                    Thread.currentThread().interrupt();
                    return 0;
                }
                if (f.isFile()) {
                    root.getData().addFile();
                    if( !isSymLink(f) ) {
                        result += f.length();
                    }
                } else if(f.isDirectory() ){
                    root.getData().addDir();
                    if( !isSymLink(f) ) {
                        result += createTree(f, newLeaf);
                    }
                }
                root.getData().setSizeLong(result);
            }
        }else {
            System.out.printf("Directory '%s' is empty!\n", dir.getPath());
            return 0;
        }

        return result;
    }


    private boolean isSymLink(File f) {
        boolean result = false;
        // check if file or directory is a symlink, if it is, skip it
        File canon = null;
        try {
            canon = f.getParent() == null ? f : new File(f.getParentFile().getCanonicalFile(), f.getName());
            if (!canon.getCanonicalFile().equals(canon.getAbsoluteFile())) {
                result = true;
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
