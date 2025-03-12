package Unipi.Fifa.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.neo4j.core.schema.GeneratedValue;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Document(collection = "Articles")
public class Article {
    @Id
    @GeneratedValue
    private String id;
    @Field("link")
    private String link;

    @Field("in_associated_with")
    private String inAssociatedWith;

    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("publish-time")
    private Date publishTime;

    @Field("username")
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getInAssociatedWith() {
        return inAssociatedWith;
    }

    public void setInAssociatedWith(String inAssociatedWith) {
        this.inAssociatedWith = inAssociatedWith;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
