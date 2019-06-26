package dewittt.blog.service.Impl;

import dewittt.blog.fileserver.domain.File;
import dewittt.blog.repository.FileRepository;
import dewittt.blog.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;


    @Override
    public File saveFile(File file) {
        return fileRepository.save(file);
    }

    @Override
    public void removeFile(String id) {
        fileRepository.deleteById(id);
    }

    @Override
    public Optional<File> getFileById(String id) {
        return fileRepository.findById(id);
    }

    @Override
    public List<File> listFilesByPage(int pageIndex, int pageSize) {
        Page<File> page = null;
        List<File> list = null;
        Sort sort = new Sort(Sort.Direction.DESC,"uploadDate");
        Pageable pageable = PageRequest.of(pageIndex,pageSize);
        page = fileRepository.findAll(pageable);
        list = page.getContent();
        return list;
    }
}
