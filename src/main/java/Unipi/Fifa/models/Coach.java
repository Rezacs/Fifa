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

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public String getCoachUrl() {
        return coachUrl;
    }

    public void setCoachUrl(String coachUrl) {
        this.coachUrl = coachUrl;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getNationalityName() {
        return nationalityName;
    }

    public void setNationalityName(String nationalityName) {
        this.nationalityName = nationalityName;
    }

    public String getCoachFaceUrl() {
        return coachFaceUrl;
    }

    public void setCoachFaceUrl(String coachFaceUrl) {
        this.coachFaceUrl = coachFaceUrl;
    }

    public String getNationFlagUrl() {
        return nationFlagUrl;
    }

    public void setNationFlagUrl(String nationFlagUrl) {
        this.nationFlagUrl = nationFlagUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
