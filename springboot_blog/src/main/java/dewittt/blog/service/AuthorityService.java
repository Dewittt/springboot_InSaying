package dewittt.blog.service;


import dewittt.blog.entity.Authority;

import java.util.Optional;

public interface AuthorityService {
    Optional<Authority> getAuthorityById(Long id);
}
