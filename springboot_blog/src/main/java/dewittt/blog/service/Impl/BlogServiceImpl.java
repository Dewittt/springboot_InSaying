package dewittt.blog.service.Impl;

import dewittt.blog.entity.*;
import dewittt.blog.repository.BlogRepository;
import dewittt.blog.service.BlogService;
import dewittt.blog.service.EsBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private EsBlogService esBlogService;

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        boolean isNew = (blog.getId()==null);
        EsBlog esBlog;
        Blog rblog = blogRepository.save(blog);
        if (isNew){
            esBlog = new EsBlog(rblog);
        }else {
            esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
            esBlog.update(rblog);
        }

        esBlogService.updateEsBlog(esBlog);

        return rblog;
    }

    @Transactional
    @Override
    public void removeBlog(Long id) {
        blogRepository.deleteById(id);
        EsBlog esBlog = esBlogService.getEsBlogByBlogId(id);
        esBlogService.removeEsBlog(esBlog.getId());
    }

    @Override
    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    @Override
    public Page<Blog> listBlogByTitleVote(User user, String title, Pageable pageable) {
        title = "%"+title+"%";
        String tags = title;
        Page<Blog> blogs = blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title,user,tags,user,pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogByTitleVoteAndSort(User user, String title, Pageable pageable) {
        title = "%"+title+"%";
        Page<Blog> blogs = blogRepository.findByUserAndTitleLike(user,title,pageable);
        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Optional<Blog> blog = blogRepository.findById(id);
        Blog newBlog = null;
        if (blog.isPresent()){
            newBlog = blog.get();
            newBlog.setReadtimes(newBlog.getReadtimes()+1);
            this.saveBlog(newBlog);
        }
    }

    @Override
    public Blog createComment(Long blogId, String commentContent) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        Blog originalBlog = null;
        if (optionalBlog.isPresent()){
            originalBlog = optionalBlog.get();
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Comment comment = new Comment(commentContent,user);
            originalBlog.addComment(comment);
        }

        return this.saveBlog(originalBlog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if (optionalBlog.isPresent()){
            Blog origionalBlog = optionalBlog.get();
            origionalBlog.removeComment(commentId);
            this.saveBlog(origionalBlog);
        }
    }

    @Override
    public Blog createVote(Long blogId) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        Blog originalBlog = null;

        if (optionalBlog.isPresent()){
            originalBlog = optionalBlog.get();

            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Vote vote = new Vote(user);
            boolean isExist = originalBlog.addVote(vote);
            if (isExist){
                throw new IllegalArgumentException("该用户已经点赞了");
            }
        }

        return this.saveBlog(originalBlog);
    }


    @Override
    public void removeVote(Long blogId, Long voteId) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        Blog originalBlog = null;

        if (optionalBlog.isPresent()){
            originalBlog = optionalBlog.get();
            originalBlog.removeVote(voteId);
            this.saveBlog(originalBlog);
        }
    }

    @Override
    public Page<Blog> listBlogByCatalog(Catalog catalog, Pageable pageable) {
        Page<Blog> blogs = blogRepository.findByCatalog(catalog,pageable);
        return blogs;
    }
}
