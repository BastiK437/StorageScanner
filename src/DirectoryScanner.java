import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DirectoryScanner implements Runnable{
    private String path;
    private List<TableContent> tContent;
    private Controller controller;
    private long internResult;
    private int printCnt;
    private boolean reloadDirs;
    private boolean ignoreHiddenElements;

    private int fileCnt;
    private int dirCnt;

    public DirectoryScanner(String path, Controller controller, boolean reloadDirs, boolean ignoreHiddenElements) {
        assert path != null : "[supportClasses.DirectoryScanner] Path can not be null!";
        assert controller != null : "[supportClasses.DirectoryScanner] control.Controller can not be null!";

        this.path = path;
        this.controller = controller;
        printCnt = 0;
        this.reloadDirs = reloadDirs;
        this.ignoreHiddenElements = ignoreHiddenElements;
    }

    @Override
    public void run() {
        File file = new File(path);

        if(file.isFile() || !file.exists()) {
            System.out.printf("file or not existing\n");
            return;
        }

        File content[] = file.listFiles();
        tContent = new ArrayList<>();

        int indexCnt = 0;
        for(int i=0; i<content.length; i++){
            internResult = 0;

            // check if element is a hidden element, aka starts with a dot '.'
            if(ignoreHiddenElements && content[i].getName().startsWith(".")){
                continue;
            }

            // check if it is the proc/ dir
            if(content[i].getName().equals("proc")){
                continue;
            }

            fileCnt = 0;
            dirCnt = 0;

            // add entry with size 0 to list
            tContent.add( new TableContent(content[i].getName(), 0, controller.getselectedSize()) );

            // check wheter the element is a file or a directory, if it is a file, directly set size, else call getDirSpace()
            if(content[i].isDirectory() ) {
                // check if the directory was already scanned
                if (controller.containsKey(content[i].getPath()) && !reloadDirs){
                    tContent.get(indexCnt).setSize(controller.getKeySize(content[i].getPath() ) );
                }else {
                    tContent.get(indexCnt).setSize(getDirSpace(content[i], indexCnt));        // set size of current folder
                    controller.putPath(content[indexCnt].getPath(), tContent.get(indexCnt).getSizeLong());
                }
            }else{
                tContent.get(indexCnt).setSize(content[i].length());
            }

            tContent.get(indexCnt).setDirs(dirCnt);
            tContent.get(indexCnt).setFiles(fileCnt);
            indexCnt++;

            // check for interruption on the thread
            if(Thread.currentThread().isInterrupted()) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        controller.updateTable(tContent);
        controller.sortTable();

        System.out.printf("Scan finished\n");
    }

    public long getDirSpace(File dir, int entrance) {
        long result = 0;

        if(ignoreHiddenElements && dir.getName().startsWith(".")){
            return 0;
        }

        File fileList[] = dir.listFiles();
        if(fileList != null) {
            for (File f : fileList) {
                if(Thread.currentThread().isInterrupted()) {
                    Thread.currentThread().interrupt();
                    return 0;
                }
                if (f.isFile()) {
                    fileCnt++;
                    if( !isSymLink(f) ) {
                        result += f.length();
                        internResult += f.length();
                    }
                } else if(f.isDirectory() ){
                    dirCnt++;
                    if( !isSymLink(f) ) {
                        if (controller.containsKey(f.getPath()) && !reloadDirs){
                            result += controller.getKeySize(f.getPath());
                        }else{
                            //System.out.printf("getDirSpace from dir: %s\n", dir.getPath());
                            long space = getDirSpace(f, entrance);
                            controller.putPath(f.getPath(), space);
                            result += space;
                        }
                    }
                }
            }
        }else {
            System.out.printf("Directory '%s' is empty!\n", dir.getPath());
            return 0;
        }

        tContent.get(entrance).setSize(internResult);
        if(printCnt%500 == 0) {
            controller.updateTable(tContent);
        }
        printCnt++;
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
