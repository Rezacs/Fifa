package Unipi.Fifa.models;

import Unipi.Fifa.relations.ManagesClub;
import Unipi.Fifa.relations.PlaysForClub;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "Coaches")
public class Coach {

    @Id
    @GeneratedValue
    private String id;

    @Field("coach_id")
    private Integer coachId;

    @Field("coach_url")
    private String coachUrl;

    @Field("short_name")
    private String shortName;

    @Field("long_name")
    private String longName;

    @Field("dob")
    private Date dob;

    @Field("nationality_name")
    private String nationalityName;

    @Field("coach_face_url")
    private String coachFaceUrl;

    @Field("nation_flag_url")
    private String nationFlagUrl;

    @Field("gender")
    private String gender;
}
