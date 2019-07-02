package dewittt.blog.service;

import dewittt.blog.entity.User;
import dewittt.blog.entity.blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BlogService {
    blog saveBlog(blog blog);
    void removeBlog(Long id);
    Optional<blog> getBlogById(Long id);
    Page<blog> listBlogByTitleVote(User user, String title, Pageable pageable);
    Page<blog> listBlogByTitleVoteAndSort(User user,String title,Pageable pageable);
    void readingIncrease(Long id);
    blog createComment(Long blogId,String commentContent);
    void removeComment(Long blogId,Long commentId);
}
