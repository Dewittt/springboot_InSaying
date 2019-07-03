package dewittt.blog.entity;

import com.github.rjeschke.txtmark.Processor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Blog implements Serializable {
    private static final long SerialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "标题不能为空")
    @Size(min = 2,max = 50)
    @Column(nullable = false,length = 50)
    private String title;

    @NotEmpty(message = "简介不能为空")
    @Size(min = 2,max = 300)
    @Column(nullable = false)
    private String summary;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @NotEmpty(message = "内容不能为空")
    @Size(min = 2)
    @Column(nullable = false)
    private String content;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @NotEmpty(message = "内容不能为空")
    @Size(min = 2)
    @Column(nullable = false)
    private String htmlContent;

    @OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp createTime;

    @Column(name = "readtimes")
    private Integer readtimes=0;

    @Column(name = "commentsSize")
    private Integer commentsSize;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "blog_comment",joinColumns = @JoinColumn(name = "blog_id",referencedColumnName = "id"))
    private List<Comment> comments;

    @Column(name = "votes")
    private Integer votes=0;

    @Column(name = "tags",length = 100)
    private String tags;

    protected Blog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.htmlContent = Processor.process(content);
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp timestamp) {
        this.createTime = timestamp;
    }

    public Integer getReadtimes() {
        return readtimes;
    }

    public void setReadtimes(Integer readtimes) {
        this.readtimes = readtimes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.commentsSize = comments.size();
    }

    public Integer getCommentsSize() {
        return commentsSize;
    }

    public void setCommentsSize(Integer commentsSize) {
        this.commentsSize = commentsSize;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public void removeComment(Long commentId){
        for (int i=0;i<comments.size();i++){
            if (comments.get(i).getId()==commentId){
                this.comments.remove(i);
                break;
            }
        }

        this.commentsSize = this.comments.size();
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Blog(@NotEmpty(message = "标题不能为空") @Size(min = 2, max = 50) String title, @NotEmpty(message = "简介不能为空") @Size(min = 2, max = 300) String summary, @NotEmpty(message = "内容不能为空") @Size(min = 2) String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }
}

