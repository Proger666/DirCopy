package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    static long sum = 0;

    public static void main(String[] args) throws InterruptedException {
        final long[] DirsSizes = new long[2];
        long dstDirSize;
        File srcDir = new File("c:\\temp");
        File dstDir = new File("h:\\tempCopy");






        System.out.println("Lets copy only .txt files and directory structure \n");
        long startTime = System.currentTimeMillis();
        copyDir(srcDir, dstDir, ".txt");
        long stopTime = System.currentTimeMillis();

        System.out.println("Seems its copied in " + ((stopTime - startTime) / 1000f) + "s. Lets check...");


        //Cant change link but be able to change object by link
        Thread srcDirMeasureThread = new Thread(() -> DirsSizes[0] = getDirSize(srcDir));

        //No idea how to fix
        sum = 0;

        Thread dstDirMeasureThread = new Thread(() -> DirsSizes[1] = getDirSize(dstDir));

        srcDirMeasureThread.start();
        dstDirMeasureThread.start();

        //Wait for calc
        srcDirMeasureThread.join();
        dstDirMeasureThread.join();

        System.out.println("Source directory size is: " + DirsSizes[0] / 1024 / 1024f + " mb");
        System.out.println("Copied directory size is: " + DirsSizes[1] / 1024 / 1024f + " mb");



    }

    private static void copyDir(File srcDir, File dstDir, String s) {
        try {
            if (srcDir.isDirectory()) {
                if (!dstDir.exists())
                    dstDir.mkdir();
            } else if (srcDir.isFile()) {
                if (!dstDir.exists())
                    copyFile(srcDir, dstDir, s);
            }
            if (srcDir.isDirectory()) {
                for (File srcSubDir : srcDir.listFiles()) {
                    String subDirName = srcSubDir.getName();
                    copyDir(srcSubDir, new File(dstDir, subDirName), ".txt");
                    if (srcSubDir.isFile()) {
                        copyFile(srcSubDir, new File(dstDir, subDirName), s);
                    }
                }


            }
        } catch (NullPointerException ex) {
            System.out.println("We screwed with dir " + srcDir.getName());
        }
    }


    private static void copyFile(File srcDir, File dstDir, String s) {

        int count;
        if (srcDir.getName().toLowerCase().contains(s)) {

            try (FileInputStream in = new FileInputStream(srcDir);
                 FileOutputStream out = new FileOutputStream(dstDir, false)) {


                byte[] buff = new byte[64 * 1024];
                while ((count = in.read(buff)) != -1) {
                    out.write(buff, 0, count);
                }
                out.flush();

            } catch (IOException ex) {
                System.out.println("We screwed at copyFile, Files is " + srcDir.getName() + " and " + dstDir.getName());
            }
        }
    }


    private synchronized static long getDirSize(File srcDir) {


        long sum = 0;
            if (srcDir.isFile()){
               sum += srcDir.length();
        }
        else {
                for (File curFile :
                        srcDir.listFiles()) {
                    sum += getDirSize(curFile);
                }
            }
        return sum;

    }
}
