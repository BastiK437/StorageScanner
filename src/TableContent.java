public class TableContent implements Comparable{

    private String name;
    private String size;
    private String files;
    private String dirs;
    private PathInformation pi;
    private int convertion;

    public TableContent(String name, int convertion, PathInformation pi){
        this.name = name;
        this.convertion = convertion;
        this.pi = pi;
        updateSize(0);
    }

    // is needed for the columnfactory
    public String getSize() {
        return size;
    }
    public String getFiles() { return files; }
    public String getDirs() { return dirs; }

    public void setPathInformation(PathInformation pi) {
        this.pi = pi;
        files = pi.getFiles();
        dirs = pi.getDirs();
        updateSize(pi.getSize());
    }

    public PathInformation getPathInformation() {
        return pi;
    }

    public void setSizeConvertion(int convertion) {
        this.convertion = convertion;
        updateSize(pi.getSize());
    }

    private void updateSize(long sizeLong){
        double sizeDouble = (double) sizeLong;
        switch (convertion) {
            case 0:
                size = String.format("%,.0f", sizeDouble);
                break;
            case 1:
                size = String.format("%,.2f", sizeDouble/1024);     // 2^10
                break;
            case 2:
                size = String.format("%,.2f", sizeDouble/1048576);  // 2^20
                break;
            case 3:
                size = String.format("%,.2f", sizeDouble/1073741824);    // 2^30
                break;
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Object o) {
        if(o == null) {
            System.out.printf("compared object is null!\n");
            return 0;
        }
        TableContent tc = (TableContent) o;
        if (tc.pi.getSize() > this.pi.getSize()) {
            return 1;
        }else if( tc.pi.getSize() == this.pi.getSize()) {
            return 0;
        }else{
            return -1;
        }
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Size: %s, Files: %d, Dirs: %d\n", name, size, pi.getFiles(), pi.getFiles());
    }
}
