package dewittt.blog.repository;

import dewittt.blog.fileserver.domain.File;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<File,String> {
}
