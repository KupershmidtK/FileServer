package server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    static public byte[] read(String fileName) throws IOException {
        return Files.readAllBytes(Path.of(fileName));
    }

    static public void write(String fileName, byte[] data) throws IOException {
        File file = new File(fileName);
        try(FileOutputStream fileWriter = new FileOutputStream(file)) {
            fileWriter.write(data);
        } catch (IOException e) {
            throw new IOException("Error writing to file " + file.getAbsoluteFile());
        }
    }

    static public void delete(String fileName) throws IOException {
        Files.delete(Path.of(fileName));
    }

    static public boolean isExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }
}
