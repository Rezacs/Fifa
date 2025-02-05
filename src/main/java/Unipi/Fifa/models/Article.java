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
}
