package me.stevenkin.boomvc.http.multipart;

public class FileItem {

    private String name;
    private String fileName;
    private String contentType;
    private long   length;
    private byte[] data;

    public FileItem(String name, String fileName, String contentType, long length, byte[] data) {
        this.name = name;
        this.fileName = fileName;
        this.contentType = contentType;
        this.length = length;
        this.data = data;
    }

    @Override
    public String toString() {
        long kb = length / 1024;
        return "FileItem(" +
                "name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", size=" + (kb < 1 ? 1 : kb) + "KB)";
    }
}
