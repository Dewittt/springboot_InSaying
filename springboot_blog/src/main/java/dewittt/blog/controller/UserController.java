package dewittt.blog.controller;

import dewittt.blog.entity.Authority;
import dewittt.blog.vo.Response;
import dewittt.blog.entity.User;
import dewittt.blog.service.AuthorityService;
import dewittt.blog.service.UserService;
import dewittt.blog.util.ConstraintViolationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @GetMapping
    public ModelAndView list(@RequestParam(value = "async",required = false)boolean async,
                             @RequestParam(value = "pageIndex",required = false,defaultValue = "0")int pageIndex,
                             @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,
                             @RequestParam(value = "username",required = false,defaultValue = "")String username,
                             Model model){
        Pageable pageable = PageRequest.of(pageIndex,pageSize);
        Page<User> page = userService.listUserByUsernameLike(username,pageable);
        List<User> list = page.getContent();
        model.addAttribute("page",page);
        model.addAttribute("userList",list);
        return new ModelAndView(async?"users/list :: #mainContainerRepleace" : "users/list","userModel",model);
    }

    @GetMapping("/add")
    public ModelAndView add(Model model){
        model.addAttribute("user",new User(null,null,null));
        return new ModelAndView("users/add","userModel",model);
    }

    @PostMapping
    public ResponseEntity<Response> saveOrUpdateUser(User user,Long authorityId){

        List<Authority> list = new ArrayList<>();
        list.add(authorityService.getAuthorityById(authorityId).get());
        user.setAuthorities(list);

        try{
            userService.SaveOrUpdateUser(user);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMsg(e)));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功",user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id")Long id, Model model){
        try{
            userService.removeUser(id);
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"删除成功!"));
    }

    @GetMapping("/edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id")Long id,Model model){
        Optional<User> user = userService.getUserById(id);
        model.addAttribute("user",user.get());
        return new ModelAndView("users/edit","userModel",model);
    }
}
