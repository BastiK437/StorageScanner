package scanner;

import helper.TableContent;
import helper.tree.Leaf;
import helper.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DirectoryScanner implements Runnable{
    // extern
    private DirectoryManager directoryManager;


    // intern
    private String path;
    private List<TableContent> tContent;
    private boolean ignoreHiddenElements;
    private Tree fileTree;

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
            String fileName = file.getName();
            if(fileName.equals("")) {
                fileName = "root";
            }
            TableContent root = new TableContent(fileName);
            root.setPath(file.getPath());
            fileTree = new Tree(root);
            createTree(file, fileTree.getRoot());
        }

        directoryManager.calculatingFinished(fileTree);

        System.out.printf("Scan finished\n");
    }

    public long createTree(File dir, Leaf root) {
        long result = 0;

        if(ignoreHiddenElements && dir.getName().startsWith(".")){
            return 0;
        }

        File fileList[] = dir.listFiles();
        int progress = 0;
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
                Leaf newLeaf = new Leaf();
                newLeaf.setData(newContent);
                newLeaf.setParent(root);
                root.addChildren(newLeaf);

                if(Thread.currentThread().isInterrupted()) {
                    Thread.currentThread().interrupt();
                    return 0;
                }
                if (f.isFile()) {
                    root.getData().addFile();
                    if( !isSymLink(f) ) {
                        result += f.length();
                        newLeaf.getData().setSizeRaw(f.length());
                        newLeaf.getData().isFile();
                    }
                } else if(f.isDirectory() ){
                    root.getData().addDir();
                    if( !isSymLink(f) ) {
                        result += createTree(f, newLeaf);
                    }
                }
                root.getData().setSizeRaw(result);
                progress++;
                directoryManager.setProgress(((double)fileList.length/(double)progress) / 100.0);
            }
        }else {
            //System.out.printf("Directory '%s' is empty!\n", dir.getPath());
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
