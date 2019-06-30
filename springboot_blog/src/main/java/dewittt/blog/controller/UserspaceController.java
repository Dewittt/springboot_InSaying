package dewittt.blog.controller;

import dewittt.blog.entity.User;
import dewittt.blog.entity.blog;
import dewittt.blog.service.BlogService;
import dewittt.blog.service.UserService;
import dewittt.blog.util.ConstraintViolationExceptionHandler;
import dewittt.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${file.server.url}")
    private String fileServerUrl;

    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username")String username,Model model){
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return "redirect:/u/"+username+"/blogs";
    }

    @GetMapping("/{username}/blogs")
    public String listBLogsByOrder(@PathVariable("username")String username,
                                   @RequestParam(value = "order",required = false,defaultValue = "new")String order,
                                   @RequestParam(value = "catalog",required = false)Long catalogId,
                                   @RequestParam(value = "keyword",required = false,defaultValue = "")String keyword,
                                   @RequestParam(value = "async",required = false)boolean async,
                                   @RequestParam(value = "pageIndex",required = false,defaultValue = "0")int pageIndex,
                                   @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,
                                   Model model){

        User user = (User) userDetailsService.loadUserByUsername(username);
        Page<blog> page = null;

        if (catalogId!=null &&catalogId>0){
            //
        }else if (order.equals("hot")){
            Sort sort = new Sort(Sort.Direction.DESC,"readtimes","comments","votes");
            Pageable pageable = PageRequest.of(pageIndex,pageSize,sort);
            page = blogService.listBlogByTitleVote(user,keyword,pageable);
        }else if (order.equals("new")){
            Pageable pageable = PageRequest.of(pageIndex,pageSize);
            page = blogService.listBlogByTitleVote(user,keyword,pageable);
        }
        List<blog> list = page.getContent();
        model.addAttribute("user",user);
        model.addAttribute("order",order);
        model.addAttribute("catalogId",catalogId);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("blogList",list);
        return async?"/userspace/u :: #mainContainerRepleace":"/userspace/u";
    }

    @GetMapping("/{username}/blogs/{id}")
    public String listBlogsByOrder(@PathVariable("id")Long id,@PathVariable("username")String username,Model model){
        User user = null;
        Optional<blog> blog= blogService.getBlogById(id);
        blogService.readingIncrease(id);
        boolean isBlogOwner = false;
        if (SecurityContextHolder.getContext().getAuthentication()!=null&&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user!=null&&username.equals(user.getUsername())){
                isBlogOwner=true;
            }
        }
        model.addAttribute("isBlogOwner",isBlogOwner);
        model.addAttribute("blogModel",blog.get());
        return "/userspace/blog";
    }

    @GetMapping("/{username}/blogs/edit")
    public ModelAndView editBlog(@PathVariable("username")String username,Model model){
        model.addAttribute("blog",new blog(null,null,null));
        model.addAttribute("fileServerUrl",fileServerUrl);
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username")String username, Model model){
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("fileServerUrl",fileServerUrl);
        return new ModelAndView("/userspace/profile","userModel",model);
    }

    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username")String username,User user){
        User originalUser = userService.getUserById(user.getId()).get();
        originalUser.setEmail(user.getEmail());
        String rawpassword = originalUser.getPassword();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePassword = encoder.encode(user.getPassword());
        boolean isMatch = encoder.matches(rawpassword,encodePassword);
        if (!isMatch){
            originalUser.setEncodePassword(user.getPassword());
        }
        userService.SaveOrUpdateUser(originalUser);
        return "redirect:/u/"+username+"/profile";
    }

    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username")String username,Model model){
        User user = (User) userDetailsService.loadUserByUsername("username");
        model.addAttribute("user",user);
        return new ModelAndView("/userspace/avatar","userModel",model);
    }

    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username")String username,@RequestBody User user){
        String avatarUrl = user.getImgPath();
        User originalUser = userService.getUserById(user.getId()).get();
        originalUser.setImgPath(avatarUrl);
        userService.SaveOrUpdateUser(originalUser);
        return ResponseEntity.ok().body(new Response(true,"保存成功",avatarUrl));
    }

    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username")String username,@PathVariable("id") Long id,Model model){
        model.addAttribute("blog",blogService.getBlogById(id));
        model.addAttribute("fileServerUrl",fileServerUrl);
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    @PostMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username")String username,@RequestBody blog blog){
        try{
            if (blog.getId()!=null){
                Optional<blog> optionalBlog = blogService.getBlogById(blog.getId());
                if (optionalBlog.isPresent()){
                    blog originalBlog = optionalBlog.get();
                    originalBlog.setTitle(blog.getTitle());
                    originalBlog.setContent(blog.getContent());
                    originalBlog.setSummary(blog.getSummary());
                    blogService.saveBlog(originalBlog);
                }else {
                    User user = (User) userDetailsService.loadUserByUsername(username);
                    blog.setUser(user);
                    blogService.saveBlog(blog);
                }
            }
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMsg(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        String redirectUrl = "/u/"+username+"/blogs/"+blog.getId();
        return ResponseEntity.ok().body(new Response(true,"保存成功",redirectUrl));
    }

    @DeleteMapping("/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username")String username,@PathVariable("id")Long id){
        try {
            blogService.removeBlog(id);
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        String redirectUrl = "/u/"+username+"blogs";
        return ResponseEntity.ok().body(new Response(true,"删除成功",redirectUrl));
    }
}
