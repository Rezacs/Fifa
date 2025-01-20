package Unipi.Fifa.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "Coaches")
public class Coach {
    @Id
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
