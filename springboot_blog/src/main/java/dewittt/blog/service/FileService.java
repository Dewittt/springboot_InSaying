package dewittt.blog.service;

import dewittt.blog.fileserver.domain.File;

import java.util.List;
import java.util.Optional;

public interface FileService {
    File saveFile(File file);
    void removeFile(String id);
    Optional<File> getFileById(String id);
    List<File> listFilesByPage(int pageIndex,int pageSize);
}
