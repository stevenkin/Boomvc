package me.stevenkin.boomvc.common.kit;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

/**
 * Created by wjg on 16-4-16.
 */
public class FileKit {
    public static String getFileMimeType(File file) throws Exception {
        URL url = file.toURL();
        URLConnection connection = url.openConnection();
        return connection.getContentType();
    }

    public static byte[] getFileContent(File file) throws Exception {
        return Files.readAllBytes(file.toPath());
    }
}
