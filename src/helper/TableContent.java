package helper;

public class TableContent implements Comparable{

    // table vars
    private String name;
    private String size;
    private String files;
    private String dirs;
    private boolean isFile;

    // intern vars
    private long sizeRaw;

    public long getSizeRaw() {
        return sizeRaw;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setSizeRaw(long sizeRaw) {
        this.sizeRaw = sizeRaw;
    }

    private String path;

    public TableContent(String name){
        this.name = name;
        this.isFile = false;

        files = "0";
        dirs = "0";
        size = "0";
    }

    // is needed for the columnfactory
    public String getSize() {
        return size;
    }
    public String getFiles() { return (isFile) ? "-" : files; }
    public String getDirs() { return (isFile) ? "-" : dirs; }
    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public void addFile() {
        int tmp = Integer.parseInt(files);
        tmp++;
        files = String.format("%d", tmp);
    }

    public void addDir() {
        int tmp = Integer.parseInt(dirs);
        tmp++;
        dirs = String.format("%d", tmp);
    }

    public void isFile() {
        isFile = true;
    }

    @Override
    public int compareTo(Object o) {
        if(o == null) {
            System.out.printf("compared object is null!\n");
            return 0;
        }
        TableContent tc = (TableContent) o;
        if( tc.getSizeRaw() > this.getSizeRaw() ) {
            return 1;
        }else if( tc.getSizeRaw() == this.getSizeRaw() ) {
            return 0;
        }else{
            return -1;
        }
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Size: %s, Files: %s, Dirs: %s", name, size, files, dirs);
    }
}
