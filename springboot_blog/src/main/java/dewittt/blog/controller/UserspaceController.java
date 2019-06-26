package dewittt.blog.controller;

import dewittt.blog.entity.User;
import dewittt.blog.service.UserService;
import dewittt.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${file.server.url}")
    private String fileServerUrl;

    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username")String username){
        return "/userspace/u";
    }

    @GetMapping("/{username}/blogs")
    public String listBLogsByOrder(@PathVariable("username")String username,
                                   @RequestParam(value = "order",required = false,defaultValue = "new")String order,
                                   @RequestParam(value = "category",required = false)Long category,
                                   @RequestParam(value = "keyword",required = false)String keyword){
        if (category!=null){
            return "/userspace/u";
        }else if (keyword!=null&&!keyword.isEmpty()){
            return "/userspace/u";
        }
        return "/userspace/u";
    }

    @GetMapping("/{username}/blogs/{id}")
    public String listBlogsByOrder(@PathVariable("id")Long id){
        return "/userspace/blog";
    }

    @GetMapping("/{username}/blogs/edit")
    public String editBlog(){
        return "/userspace/blogedit";
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
}
