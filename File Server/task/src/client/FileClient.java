package client;

import server.FileUtils;
import java.io.File;
import java.io.IOException;

/**
 * Read and write file from/to specific folder.
 * Using server.FileUtils class.
 */
public class FileClient {
    final static String PATH = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "client" + File.separator + "data" + File.separator;

    public static byte[] read(String fileName) throws IOException {
        String filePathName = PATH + fileName;
        return FileUtils.read(filePathName);
    }

    public static void write(String fileName, byte[] data) throws IOException {
        String filePathName = PATH + fileName;
        FileUtils.write(filePathName, data);
    }
}
