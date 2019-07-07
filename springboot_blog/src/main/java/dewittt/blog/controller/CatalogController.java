package dewittt.blog.controller;


import dewittt.blog.entity.Catalog;
import dewittt.blog.entity.User;
import dewittt.blog.service.CatalogService;
import dewittt.blog.util.ConstraintViolationExceptionHandler;
import dewittt.blog.vo.CatalogVo;
import dewittt.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/catalogs")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping
    public String listComments(@RequestParam(value = "username",required = true)String username, Model model){
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);

        boolean isOwner = false;

        if (SecurityContextHolder.getContext().getAuthentication()!=null&&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
        && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal!=null&&user.getUsername().equals(principal.getUsername())){
                isOwner = true;
            }
        }

        model.addAttribute("isCatalogsOwner",isOwner);
        model.addAttribute("catalogs",catalogs);
        return "/userspace/u :: #catalogRepleace";
    }

    @PostMapping
    @PreAuthorize("authentication.name.equals(#catalogVO.username)")
    public ResponseEntity<Response> create(@RequestBody CatalogVo catalogVo){
        String username = catalogVo.getUsername();
        Catalog catalog = catalogVo.getCatalog();

        User user = (User) userDetailsService.loadUserByUsername(username);
        try {
            catalog.setUser(user);
            catalogService.saveCatalog(catalog);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false,ConstraintViolationExceptionHandler.getMsg(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true,"创建标签成功",null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> delete(String username,@PathVariable("id")Long id){
        try{
            catalogService.removeCatalog(id);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false,ConstraintViolationExceptionHandler.getMsg(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true,"删除成功",null));
    }

    @GetMapping("/edit")
    public String getCatalogEdit(Model model){
        Catalog catalog = new Catalog(null,null);
        model.addAttribute("catalog",catalog);
        return "/userspace/catalogedit";
    }

    @GetMapping("/edit/{id}")
    public String getCatalogById(@PathVariable("id") Long id, Model model) {
        Optional<Catalog> optionalCatalog = catalogService.getCatalogById(id);
        Catalog catalog = null;

        if (optionalCatalog.isPresent()) {
            catalog = optionalCatalog.get();
        }

        model.addAttribute("catalog",catalog);
        return "/userspace/catalogedit";
    }

}
