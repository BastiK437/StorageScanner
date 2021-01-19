package scanner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CheckAvailableFileSystems {

    boolean DEBUG = false;

    public File[] checkFileSystems() {
        String[] mountpoints;
        List<File> deviceList = new ArrayList<>();

        if( (mountpoints = getMountpoints() ) == null) {
            return null;
        }

        // add external partitions
        for(String s: mountpoints) {
            deviceList.add(new File(s));
        }

        // add home directory
        deviceList.add(new File("/home/"));

        // add all user dirs
        String[] userDirs = getUserDirs();
        for(String s: userDirs) {
            deviceList.add(new File(s));
        }

        return (File[]) deviceList.toArray(new File[0]);
    }

    private String[] getMountpoints() {
        File mounts = new File("/proc/mounts");
        List<String> mountPoints = new ArrayList<>();
        String line;

        if( !mounts.exists() ) {
            System.out.printf("No mount data available, check for \"/proc/mounts\"\n");
            return null;
        }

        try( BufferedReader reader = new BufferedReader(new FileReader(mounts)) ){
            while( ( line = reader.readLine() ) != null) {
                String fileSystemType = getColumn(line, 2);
                if(DEBUG) System.out.printf("FileSystemType: %s\n", fileSystemType);
                if (    ( fileSystemType.equals("ext4") ) ||
                        ( fileSystemType.equals("fuseblk") )
                ) {
                    if(DEBUG) System.out.printf("Add to mountpoint\n");
                    mountPoints.add(getColumn(line, 1));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mountPoints.toArray(new String[0]);
    }

    private String[] getUserDirs() {
        List<String> userDirList = new ArrayList<>();
        File homeDir = new File("/home/");

        File[] userDirs = homeDir.listFiles();
        for(int i=0; i<userDirs.length; i++) {
            if(userDirs[i].isDirectory()) {
                userDirList.add(userDirs[i].getAbsolutePath());
            }
        }
        return userDirList.toArray(new String[0]);
    }

    private String getColumn(String line, int column) {
        String result = "";
        int columnCnt = 0;

        for(int i=0; i<line.length(); i++) {
            if(line.charAt(i) == ' ' ){
                if(columnCnt == column) break;
                result = "";
                columnCnt++;
            }else{
                result += line.charAt(i);
            }
        }

        return result;
    }
}

