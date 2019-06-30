package dewittt.blog.service.Impl;

import dewittt.blog.entity.User;
import dewittt.blog.entity.blog;
import dewittt.blog.repository.BlogRepository;
import dewittt.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public blog saveBlog(blog blog) {
        blog rblog = blogRepository.save(blog);
        return rblog;
    }

    @Override
    public void removeBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Optional<blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    @Override
    public Page<blog> listBlogByTitleVote(User user, String title, Pageable pageable) {
        title = "%"+title+"%";
        String tags = title;
        Page<blog> blogs = blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title,user,tags,user,pageable);
        return blogs;
    }

    @Override
    public Page<blog> listBlogByTitleVoteAndSort(User user, String title, Pageable pageable) {
        title = "%"+title+"%";
        Page<blog> blogs = blogRepository.findByUserAndTitleLike(user,title,pageable);
        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Optional<blog> blog = blogRepository.findById(id);
        blog newBlog = null;
        if (blog.isPresent()){
            newBlog = blog.get();
            newBlog.setReadtimes(newBlog.getReadtimes()+1);
            this.saveBlog(newBlog);
        }
    }
}
