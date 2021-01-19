package scanner;

public class PathInformation {

    private String path;
    private String files;
    private String dirs;
    private long size;
    private int allFiles;
    private int allDirs;

    public PathInformation(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFiles(Integer files) {
        if(files == null) {
            this.files = "-";
        }else{
            this.files = Integer.toString(files);
        }
    }

    public void setDirs(Integer dirs) {
        if(dirs == null) {
            this.dirs = "-";
        }else{
            this.dirs = Integer.toString(dirs);
        }
    }

    public String getPath() {
        return path;
    }

    public String getFiles() {
        return files;
    }

    public String getDirs() {
        return dirs;
    }

    public int getAllFiles() {
        return allFiles;
    }

    public void setAllFiles(int allFiles) {
        this.allFiles = allFiles;
        if(files != null && files.equals("-")) {
            files = "file";
        } else {
            files = String.format("%d", allFiles);
        }
    }

    public int getAllDirs() {
        return allDirs;
    }

    public void setAllDirs(int allDirs) {
        this.allDirs = allDirs;
        if(dirs != null && dirs.equals("-")) {
            dirs = "file";
        } else {
            dirs = String.format("%d", allDirs);
        }
    }

    public void piUpdate( PathInformation pi ) {
        if(pi == null) {
            System.out.printf("Cant update path information\n");
            return;
        }
        path = pi.getPath();
        files = pi.getFiles();
        dirs = pi.getDirs();
        size = pi.getSize();
        allFiles = pi.getAllFiles();
        allDirs = pi.getAllDirs();
    }

    @Override
    public String toString() {
        return String.format("Path: %s, size: %d, files: %s, dirs: %s\n", path, size, files, dirs);
    }
}
