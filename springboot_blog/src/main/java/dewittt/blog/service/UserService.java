package dewittt.blog.service;

import dewittt.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    User SaveOrUpdateUser(User user);
    User registerUser(User user);
    void removeUser(Long id);
    Optional<User> getUserById(Long id);
    Page<User> listUserByUsernameLike(String username, Pageable pageable);
}
