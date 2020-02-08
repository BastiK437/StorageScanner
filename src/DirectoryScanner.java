import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DirectoryScanner implements Runnable{
    private String path;
    private List<TableContent> tContent;
    private Controller controller;
    private long internResult;

    public DirectoryScanner(String path, Controller controller) {
        this.path = path;
        this.controller = controller;
    }

    @Override
    public void run() {
        File file = new File(path);

        File content[] = file.listFiles();
        tContent = new ArrayList<>();

        for(int i=0; i<content.length; i++){
            // System.out.printf("Name: %s\n", content[i].getName());
            //tContent[i] = new TableContent(content[i].getName(), getDirSpace(content[i], i));
            tContent.add( new TableContent(content[i].getName(), 0, controller.getselectedSize()) );
            internResult = 0;
            getDirSpace(content[i], i);
        }

        controller.sortTable();

        System.out.printf("Scan finished\n");
    }

    public long getDirSpace(File dir, int entrance) {
        long result = 0;

        if(dir.isDirectory()) {
            File fileList[] = dir.listFiles();
            if(fileList != null) {
                for (File f : fileList) {
                    if (f.isFile()) {
                        result += f.length();
                        internResult += f.length();
                    } else if(f.isDirectory() ){
                        // check if directory is a symlink, if it is, skip it
                        File canon = null;
                        try {
                            canon = f.getParent() == null ? f : new File(f.getParentFile().getCanonicalFile(), f.getName());
                            if (!canon.getCanonicalFile().equals(canon.getAbsoluteFile())) {
                                System.out.printf("Dirctory '%s' is a symlink!\n", f.getPath());
                            } else {
                                long space = getDirSpace(f, entrance);
                                result += space;
                                internResult += space;
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

        tContent.get(entrance).setSize(result);
        controller.updateTable(tContent);

        return result;
    }


}
