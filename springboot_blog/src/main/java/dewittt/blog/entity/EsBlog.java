package dewittt.blog.entity;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;


@Document(indexName = "blog",type = "blog")
public class EsBlog implements Serializable {

    private static final long SerializableUID=1L;

    @Id
    private String id;

    @Field(type = FieldType.Long)
    private Long blogId;

    @Field(type = FieldType.text)
    private String title;

    @Field(type = FieldType.text)
    private String summary;

    @Field(type = FieldType.text)
    private String content;

    @Field(type = FieldType.keyword,index = false)
    private String username;

    @Field(type = FieldType.text,index = false)
    private String imgPath;

    @Field(type = FieldType.Date,index = false)
    private Timestamp createTime;

    @Field(type = FieldType.Integer,index = false)
    private Integer readSize=0;

    @Field(type = FieldType.Integer,index = false)
    private Integer commentSize=0;

    @Field(type = FieldType.Integer,index = false)
    private Integer voteSize=0;

    @Field(type = FieldType.text,fielddata = true)
    private String tags;

    protected EsBlog() {
    }

    public EsBlog(Long blogId, String title, String summary, String content, String username, String imgPath, Timestamp createTime, Integer readSize, Integer commentSize, Integer voteSize, String tags) {
        this.blogId = blogId;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.username = username;
        this.imgPath = imgPath;
        this.createTime = createTime;
        this.readSize = readSize;
        this.commentSize = commentSize;
        this.voteSize = voteSize;
        this.tags = tags;
    }

    public EsBlog(Blog blog) {
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.summary = blog.getSummary();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.imgPath = blog.getUser().getImgPath();
        this.createTime = blog.getCreateTime();
        this.readSize = blog.getReadtimes();
        this.commentSize = blog.getCommentsSize();
        this.voteSize = blog.getVoteSize();
        this.tags = blog.getTags();
    }

    public void update(Blog blog) {
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.summary = blog.getSummary();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.imgPath = blog.getUser().getImgPath();
        this.createTime = blog.getCreateTime();
        this.readSize = blog.getReadtimes();
        this.commentSize = blog.getCommentsSize();
        this.voteSize = blog.getVoteSize();
        this.tags = blog.getTags();
    }

    @Override
    public String toString() {
        return "EsBlog{" +
                "blogId=" + blogId +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
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
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getReadSize() {
        return readSize;
    }

    public void setReadSize(Integer readSize) {
        this.readSize = readSize;
    }

    public Integer getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(Integer commentSize) {
        this.commentSize = commentSize;
    }

    public Integer getVoteSize() {
        return voteSize;
    }

    public void setVoteSize(Integer voteSize) {
        this.voteSize = voteSize;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
