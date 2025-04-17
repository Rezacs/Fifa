package Unipi.Fifa.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Articles")
public class Article {

    @Id
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
}
