package sample.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FindFiles implements FileVisitor<Path> {

    private final PathMatcher matcher;

    private static final String folderName = "rezultati";

    List<MyFile> listaFajlova;

    public FindFiles(String pattern) {
        matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        listaFajlova = new ArrayList<>();
    }


    public static int numberOfFiles() {
        String path = System.getProperty("user.dir") + File.separator + folderName;

        File f = new File(path);

        if (f.exists()) {
            File[] f1 = f.listFiles();
            if (f1 != null)
                return f1.length;
        }

        return 0;
    }


    void find (Path file) {
        Path name = file.getFileName();
        if(name != null && matcher.matches(name)) {
            listaFajlova.add(new MyFile(String.valueOf(file.getFileName()), file.toFile().getAbsolutePath()));
        }
    }

    public List<MyFile> getListaFajlova() {
        return this.listaFajlova;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes basicFileAttributes) throws IOException {
        find(dir);
        return FileVisitResult.CONTINUE;

    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes basicFileAttributes) throws IOException {
        find(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
        System.err.println(e);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
        find(dir);
        return FileVisitResult.CONTINUE;
    }
}
