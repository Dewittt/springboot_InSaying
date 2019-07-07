package dewittt.blog.service;

import dewittt.blog.entity.EsBlog;
import dewittt.blog.entity.User;
import dewittt.blog.vo.TagVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EsBlogService {
    void removeEsBlog(String id);
    EsBlog updateEsBlog(EsBlog esBlog);
    EsBlog getEsBlogByBlogId(Long blogId);
    Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable);
    Page<EsBlog> listHotestEsBlogs(String keyword,Pageable pageable);
    Page<EsBlog> listEsBlogs(Pageable pageable);
    List<EsBlog> listTop5NewestEsBlogs();
    List<EsBlog> listTop5HotestEsBlogs();
    List<TagVO> listTop30Tags();
    List<User> listTop12Users();
}
