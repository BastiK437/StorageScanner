import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectoryScanner implements Runnable{
    private String path;
    private List<TableContent> tContent;
    private Controller controller;
    private long internResult;
    private int printCnt;
    private Map<String, Long> scannedDirs;

    public DirectoryScanner(String path, Controller controller) {
        assert path != null : "[supportClasses.DirectoryScanner] Path can not be null!";
        assert controller != null : "[supportClasses.DirectoryScanner] control.Controller can not be null!";

        this.path = path;
        this.controller = controller;
        printCnt = 0;
        scannedDirs = new HashMap<>();
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

        for(int i=0; i<content.length; i++){
            internResult = 0;
            tContent.add( new TableContent(content[i].getName(), 0, controller.getselectedSize()) ); // add and create table content to list
            if(content[i].isDirectory() ) {
                tContent.get(i).setSize(getDirSpace(content[i], i));        // set size of current folder
            }else{
                tContent.get(i).setSize(content[i].length());
            }

            //scannedDirs.put(content[i].getPath(), tContent.get(i).getSizeLong());
            if(Thread.currentThread().isInterrupted()) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        controller.updateTable(tContent);
        controller.sortTable();


        for(Map.Entry<String, Long> entry : scannedDirs.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            //System.out.printf("Key: %s, Value: %d\n", key, value);
        }
        System.out.printf("Scan finished\n");
    }

    public long getDirSpace(File dir, int entrance) {
        long result = 0;

        File fileList[] = dir.listFiles();
        if(fileList != null) {
            for (File f : fileList) {
                if(Thread.currentThread().isInterrupted()) {
                    Thread.currentThread().interrupt();
                    return 0;
                }
                if (f.isFile()) {
                    if( !isSymLink(f) ) {
                        result += f.length();
                        internResult += f.length();
                    }
                } else if(f.isDirectory() ){
                    if( !isSymLink(f) ) {
                        //System.out.printf("File: %s, Map: \n", f.getPath());

                        for(Map.Entry<String, Long> entry : scannedDirs.entrySet()) {
                            String key = entry.getKey();
                            Long value = entry.getValue();

                            //System.out.printf("Key: %s, Value: %d\n", key, value);
                        }
                        if (scannedDirs.containsKey(f.getPath())){
                            result += scannedDirs.get(f.getPath());
                            System.out.printf("Result already calculated\n");
                        }else{
                            //System.out.printf("getDirSpace from dir: %s\n", dir.getPath());
                            long space = getDirSpace(f, entrance);
                            scannedDirs.put(f.getPath(), space);
                            result += space;
                        }

                    }
                }
            }
        }else {
            System.out.printf("Directory '%s' is empty!\n", dir.getPath());
            return 0;
        }

        if(printCnt%1000 == 0) {
            tContent.get(entrance).setSize(internResult);
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
