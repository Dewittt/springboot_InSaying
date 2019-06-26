package dewittt.blog.fileserver.domain;

import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;
import java.util.Objects;

@Document
public class File {

    @Id
    private String id;
    private String name;
    private String contentType;
    private long size;
    private Date uploadDate;
    private String md5;
    private Binary content;
    private String path;

    protected File(){}

    public File(String name, String contentType, long size, Binary content) {
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return size == file.size &&
                Objects.equals(id, file.id) &&
                Objects.equals(name, file.name) &&
                Objects.equals(contentType, file.contentType) &&
                Objects.equals(uploadDate, file.uploadDate) &&
                Objects.equals(md5, file.md5) &&
                Objects.equals(path, file.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash( name, contentType, size, uploadDate, md5,id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Binary getContent() {
        return content;
    }

    public void setContent(Binary content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "File{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", contentType='" + contentType + '\'' +
                ", size=" + size +
                ", uploadDate=" + uploadDate +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
