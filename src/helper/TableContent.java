package helper;

public class TableContent implements Comparable{

    // table vars
    private String name;
    private String size;
    private String files;
    private String dirs;

    // intern vars
    private long sizeLong;
    private String path;

    public TableContent(String name){
        this.name = name;

        files = "0";
        dirs = "0";
        size = "0";
    }

    // is needed for the columnfactory
    public String getSize() {
        return size;
    }
    public String getFiles() { return files; }
    public String getDirs() { return dirs; }
    public String getName() {
        return name;
    }

    public long getSizeLong() {
        return sizeLong;
    }

    public String getPath() {
        return path;
    }

    public void setSizeLong(long size) {
        this.sizeLong = size;
        this.size = String.format("%d", size);
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

    @Override
    public int compareTo(Object o) {
        if(o == null) {
            System.out.printf("compared object is null!\n");
            return 0;
        }
        TableContent tc = (TableContent) o;
        if( tc.getSizeLong() > this.getSizeLong() ) {
            return 1;
        }else if( tc.getSizeLong() == this.getSizeLong() ) {
            return 0;
        }else{
            return -1;
        }
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Size: %s, Files: %s, Dirs: %s\n", name, size, files, dirs);
    }
}
