package test;

import scanner.CheckAvailableFileSystems;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        CheckAvailableFileSystems check = new CheckAvailableFileSystems();

        for( File f: check.checkFileSystems()) {
            System.out.printf("Path: %s, Size: %d\n", f.getPath(), f.getTotalSpace()) ;
        }
    }
}
