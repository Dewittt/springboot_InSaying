package dewittt.blog.service;

import dewittt.blog.entity.Blog;
import dewittt.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BlogService {
    Blog saveBlog(Blog blog);
    void removeBlog(Long id);
    Optional<Blog> getBlogById(Long id);
    Page<Blog> listBlogByTitleVote(User user, String title, Pageable pageable);
    Page<Blog> listBlogByTitleVoteAndSort(User user, String title, Pageable pageable);
    void readingIncrease(Long id);
    Blog createComment(Long blogId, String commentContent);
    void removeComment(Long blogId,Long commentId);
}
