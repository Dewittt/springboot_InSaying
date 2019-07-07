package dewittt.blog.controller;

import dewittt.blog.entity.User;
import dewittt.blog.entity.Vote;
import dewittt.blog.service.BlogService;
import dewittt.blog.service.VoteService;
import dewittt.blog.util.ConstraintViolationExceptionHandler;
import dewittt.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@Controller
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private VoteService voteService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<Response> createVote(Long blogId){
        try {
            blogService.createVote(blogId);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMsg(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true,"点赞成功!",null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> delete(@PathVariable("id")Long id,Long blogId){
        boolean isOwner = false;
        Optional<Vote> optionalVote = voteService.getVoteById(id);
        User user = null;
        if (optionalVote.isPresent()){
            user = optionalVote.get().getUser();
        }else {
            return ResponseEntity.ok().body(new Response(false,"不存在该点赞"));
        }
        if (SecurityContextHolder.getContext().getAuthentication()!=null&&
        SecurityContextHolder.getContext().getAuthentication().isAuthenticated()&&!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal!=null&& user.getUsername().equals(principal.getUsername())){
                isOwner=true;
            }
        }
        if (!isOwner){
            return ResponseEntity.ok().body(new Response(false,"没有权限"));
        }

        try {
            blogService.removeVote(blogId,id);
            voteService.removeVote(id);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false,ConstraintViolationExceptionHandler.getMsg(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true,"取消成功",null));

    }
}
