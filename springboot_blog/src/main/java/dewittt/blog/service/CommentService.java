package dewittt.blog.service;

import dewittt.blog.entity.Comment;

import java.util.Optional;

public interface CommentService {

    Optional<Comment> getCommentById(Long id);
    void removeComment(Long id);
}
