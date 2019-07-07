package dewittt.blog.repository;

import dewittt.blog.entity.Blog;
import dewittt.blog.entity.Catalog;
import dewittt.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog,Long> {
    Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);
    Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title, User user, String tags, User user1, Pageable pageable);
    Page<Blog> findByCatalog(Catalog catalog,Pageable pageable);
}
