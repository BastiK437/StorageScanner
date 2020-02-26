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

            PathInformation pi = new PathInformation(content[i].getPath());
            fileCnt = 0;
            dirCnt = 0;
            long tmpSize = 0;

            // add entry to list
            tContent.add( new TableContent(content[i].getName(), controller.getselectedSize(), pi) );

            // check wheter the element is a file or a directory, if it is a file, directly set size, else call getDirSpace()
            if(content[i].isDirectory() ) {
                // check if the directory was already scanned
                if (controller.containsKey(content[i].getPath()) && !reloadDirs){
                    pi.piUpdate(controller.getKeyInformation(content[i].getPath() ) );
                }else {
                    pi.piUpdate(getDirInformation(content[i], indexCnt) );        // set size of current folder
                    pi.setAllDirs(dirCnt);
                    pi.setAllFiles(fileCnt);
                    controller.putScannedDirs(content[indexCnt].getPath(), pi);
                }
            }else{
                pi.setSize(content[i].length());
                pi.setFiles(null);
                pi.setDirs(null);
                pi.setAllDirs(dirCnt);
                pi.setAllFiles(fileCnt);
            }

            tContent.get(indexCnt).setPathInformation(pi);
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

    public PathInformation getDirInformation(File dir, int entrance) {
        PathInformation pi = new PathInformation(dir.getPath());
        long result = 0;
        int internFiles = 0;
        int internDirs = 0;

        if(ignoreHiddenElements && dir.getName().startsWith(".")){
            return null;
        }

        File fileList[] = dir.listFiles();
        if(fileList != null) {
            for (File f : fileList) {
                if(Thread.currentThread().isInterrupted()) {
                    Thread.currentThread().interrupt();
                    return null;
                }
                if (f.isFile()) {
                    fileCnt++;
                    internFiles++;
                    if( !isSymLink(f) ) {
                        result += f.length();
                        internResult += f.length();
                    }
                } else if(f.isDirectory() ){
                    dirCnt++;
                    internDirs++;
                    if( !isSymLink(f) ) {
                        if (controller.containsKey(f.getPath()) && !reloadDirs){
                            result += controller.getKeyInformation(f.getPath()).getSize();
                        }else{
                            //System.out.printf("getDirSpace from dir: %s\n", dir.getPath());
                            PathInformation information = getDirInformation(f, entrance);
                            if(information != null) {
                                controller.putScannedDirs(f.getPath(), information);
                                result += information.getSize();
                            }
                        }
                    }
                }
            }
        }else {
            System.out.printf("Directory '%s' is empty!\n", dir.getPath());
            return null;
        }

        tContent.get(entrance).getPathInformation().setSize(internResult);
        if(printCnt%500 == 0) {
            controller.updateTable(tContent);
        }
        printCnt++;

        pi.setDirs(internDirs);
        pi.setFiles(internFiles);
        pi.setSize(result);
        return pi;
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
