package Unipi.Fifa.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.neo4j.core.schema.GeneratedValue;

import java.time.LocalDateTime;

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
    @Field("author")
    private String author;
    @Field("title")
    private String title;
    @Field("content")
    private String content;
    @Field("source")
    private String source;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
