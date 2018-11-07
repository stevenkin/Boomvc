package me.stevenkin.boomvc.http.kit;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

/**
 * IO Kit
 *
 */
public class IOKit {

    public static void closeQuietly(Closeable closeable) {
        try {
            if (null == closeable) {
                return;
            }
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readToString(String file) throws IOException {
        return readToString(Paths.get(file));
    }

    public static String readToString(BufferedReader bufferedReader) {
        return bufferedReader.lines().collect(Collectors.joining());
    }

    public static String readToString(Path path) throws IOException {
        BufferedReader bufferedReader = Files.newBufferedReader(path);
        return bufferedReader.lines().collect(Collectors.joining());
    }

    public static String readToString(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input, "UTF-8"))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    public static void copyFile(File source, File dest) throws IOException {
        try (FileChannel in = new FileInputStream(source).getChannel(); FileChannel out = new FileOutputStream(dest).getChannel()) {
            out.transferFrom(in, 0, in.size());
        }
    }

    public static void compressGZIP(File input, File output) throws IOException {
        try (GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(output))) {
            try (FileInputStream in = new FileInputStream(input)) {
                byte[] buffer = new byte[1024];
                int    len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            }
        }
    }

    public static byte[] compressGZIPAsString(String content, Charset charset) throws IOException {
        if (content == null || content.length() == 0) {
            return null;
        }
        GZIPOutputStream      gzip;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gzip = new GZIPOutputStream(out);
        gzip.write(content.getBytes(charset));
        gzip.close();
        return out.toByteArray();
    }

}
