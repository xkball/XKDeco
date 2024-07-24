package com.xkball.xkdeco.utils;

import java.io.File;
import java.util.function.Consumer;

public class FileUtils {

    public static void recursionVisitFile(File directory, Consumer<File> visitor) {
        if(directory.isFile()) {
            visitor.accept(directory);
            return;
        }
        var files = directory.listFiles();
        if(files == null) return;
        for (File file : files) {
            if(file.isDirectory()) {
                recursionVisitFile(file, visitor);
            }
            else if(file.isFile()) {
                visitor.accept(file);
            }
        }
    }
}
