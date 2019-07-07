package dewittt.blog.repository;

import dewittt.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    Page<User> findByUsernameLike(String username, Pageable pageable);
    List<User> findByUsernameIn(Collection<String> usernames);
}
