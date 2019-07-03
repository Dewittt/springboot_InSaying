package dewittt.blog.service.Impl;

import dewittt.blog.entity.Blog;
import dewittt.blog.entity.Comment;
import dewittt.blog.entity.User;
import dewittt.blog.repository.BlogRepository;
import dewittt.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog saveBlog(Blog blog) {
        Blog rblog = blogRepository.save(blog);
        return rblog;
    }

    @Override
    public void removeBlog(Long id) {
        blogRepository.deleteById(id);
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
}
