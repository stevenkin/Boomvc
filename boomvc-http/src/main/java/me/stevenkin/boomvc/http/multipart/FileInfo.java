package me.stevenkin.boomvc.http.multipart;

public class FileInfo {

    private String name;

    private String fileName;

    private String contentType;

    private long length;

    public FileInfo(String name, String fileName, String contentType, long length) {
        this.name = name;
        this.fileName = fileName;
        this.contentType = contentType;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getLength() {
        return length;
    }
}
