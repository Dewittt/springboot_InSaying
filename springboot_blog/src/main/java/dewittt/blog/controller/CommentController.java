package dewittt.blog.controller;

import dewittt.blog.entity.Blog;
import dewittt.blog.entity.Comment;
import dewittt.blog.entity.User;
import dewittt.blog.service.BlogService;
import dewittt.blog.service.CommentService;
import dewittt.blog.util.ConstraintViolationExceptionHandler;
import dewittt.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public String listComments(@RequestParam(value = "blogId",required = true)Long blogId, Model model){
        Optional<Blog> optionalBlog = blogService.getBlogById(blogId);
        List<Comment> comments = null;
        if (optionalBlog.isPresent()){
            comments = optionalBlog.get().getComments();
        }
        String commentOwner = "";
        if (SecurityContextHolder.getContext().getAuthentication()!=null&&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()&&
        !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal!=null){
                commentOwner = principal.getUsername();
            }
        }

        model.addAttribute("commentOwner",commentOwner);
        model.addAttribute("comments",comments);
        return "/userspace/blog :: #mainContainerRepleace";

    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> createComment(Long blogId,String commentContent){
        try {
            blogService.createComment(blogId,commentContent);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMsg(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true,"发表成功",null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> delete(@PathVariable("id")Long id,Long blogId){
        boolean isOwner=false;
        Optional<Comment> optionalComment = commentService.getCommentById(id);
        User user = null;
        if (optionalComment.isPresent()){
            user = optionalComment.get().getUser();
        }else {
            return ResponseEntity.ok().body(new Response(false,"不存在该评论"));
        }

        if (SecurityContextHolder.getContext().getAuthentication()!=null&&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()&&
        !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal!=null&&user.getUsername().equals(principal.getUsername())){
                isOwner=true;
            }
        }
        if (!isOwner){
            return ResponseEntity.ok().body(new Response(false,"没有权限"));
        }
        try{
            blogService.removeComment(blogId,id);
            commentService.removeComment(id);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false,ConstraintViolationExceptionHandler.getMsg(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true,"删除成功",null));
    }
}
