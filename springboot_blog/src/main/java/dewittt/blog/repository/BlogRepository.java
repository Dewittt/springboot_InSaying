package dewittt.blog.repository;

import dewittt.blog.entity.User;
import dewittt.blog.entity.blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<blog,Long> {
    Page<blog> findByUserAndTitleLike(User user, String title, Pageable pageable);
    Page<blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title,User user,String tags,User user1,Pageable pageable);
}
