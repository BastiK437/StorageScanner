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

    public DirectoryScanner(String path, Controller controller) {
        assert path != null : "[DirectoryScanner] Path can not be null!";
        assert controller != null : "[DirectoryScanner] Controller can not be null!";

        this.path = path;
        this.controller = controller;
        printCnt = 0;
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
            // System.out.printf("Name: %s\n", content[i].getName());
            //tContent[i] = new TableContent(content[i].getName(), getDirSpace(content[i], i));
            tContent.add( new TableContent(content[i].getName(), 0, controller.getselectedSize()) );
            internResult = 0;
            long tmpSize = getDirSpace(content[i], i);
            tContent.get(i).setSize(tmpSize);
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

        if(dir.isDirectory()) {
            File fileList[] = dir.listFiles();
            if(fileList != null) {
                for (File f : fileList) {
                    if(Thread.currentThread().isInterrupted()) {
                        Thread.currentThread().interrupt();
                        return 0;
                    }
                    if (f.isFile()) {
                        result += f.length();
                        internResult += f.length();
                    } else if(f.isDirectory() ){
                        // check if directory is a symlink, if it is, skip it
                        File canon = null;
                        try {
                            canon = f.getParent() == null ? f : new File(f.getParentFile().getCanonicalFile(), f.getName());
                            if (!canon.getCanonicalFile().equals(canon.getAbsoluteFile())) {
                                //System.out.printf("Dirctory '%s' is a symlink!\n", f.getPath());
                            } else {
                                long space = getDirSpace(f, entrance);
                                result += space;
                                //internResult += space;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else {
                System.out.printf("Directory '%s' is empty!\n", dir.getPath());
                return 0;
            }
        }else{
            result += dir.length();
        }

        if(printCnt%1000 == 0) {
            tContent.get(entrance).setSize(internResult);
            controller.updateTable(tContent);
        }
        printCnt++;

        return result;
    }


}
