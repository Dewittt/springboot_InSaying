package dewittt.blog.controller;

import dewittt.blog.entity.EsBlog;
import dewittt.blog.service.EsBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private EsBlogService esBlogService;

    @GetMapping
    public String listEsBlogs(
            @RequestParam(value="order",required=false,defaultValue="new") String order,
            @RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
            @RequestParam(value="async",required=false) boolean async,
            @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
            @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
            Model model) {
        Page<EsBlog> page = null;
        List<EsBlog> list = null;
        boolean isEmpty = true;
        try {
            if (order.equals("hot")){
                Sort sort = new Sort(Sort.Direction.DESC,"readtimes","commentSize","voteSize","createTime");
                Pageable pageable = PageRequest.of(pageIndex,pageSize,sort);
                page = esBlogService.listHotestEsBlogs(keyword,pageable);
            }//改写new了




        }
        return "redirect:/index?order="+order+"&keyword="+keyword;
    }
}
