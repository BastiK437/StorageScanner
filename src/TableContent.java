public class TableContent implements Comparable{

    private String name;
    private String size;
    private int files;
    private int dirs;
    private long sizeLong;
    private int convertion;

    public void setFiles(int files) {
        this.files = files;
    }

    public void setDirs(int dirs) {
        this.dirs = dirs;
    }

    public int getFiles() {
        return files;
    }

    public int getDirs() {
        return dirs;
    }

    public TableContent(String name, long size, int convertion){
        this.name = name;
        this.sizeLong = sizeLong;
        this.convertion = convertion;
        updateSize(size);

        files = 0;
        dirs = 0;
    }

    public long getSizeLong() {
        return sizeLong;
    }

    public String getSize() {
        return size;
    }

    public void setSize(long sizeLong) {
        this.sizeLong = sizeLong;
        updateSize(sizeLong);
    }

    public void setSizeConvertion(int convertion) {
        this.convertion = convertion;
        updateSize(sizeLong);
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
        size += String.format(" %d, %d", files, dirs);
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
        if (tc.sizeLong > this.sizeLong) {
            return 1;
        }else if( tc.sizeLong == this.sizeLong) {
            return 0;
        }else{
            return -1;
        }
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Size: %s, Files: %d, Dirs: %d\n", name, size, files, dirs);
    }
}
