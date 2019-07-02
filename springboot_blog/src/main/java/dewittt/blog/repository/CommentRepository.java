package dewittt.blog.repository;

import dewittt.blog.entity.Comment;
import dewittt.blog.entity.blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
