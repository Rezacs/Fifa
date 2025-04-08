package Unipi.Fifa.objects;

import Unipi.Fifa.models.PlayerNode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PlayerNodeDTO {
    private Long id;
    private Integer playerId;
    private String longName;
    private String nationality;
    private Integer overall;
    private String clubName;
    private LocalDate dob;
    private String gender; // Using String to keep it simple for API transfer.

    public PlayerNodeDTO(Long id, Integer playerId, String longName,
                         PlayerNode.Gender gender, String nationality,
                         LocalDate dob) {
        this.id = id;
        this.playerId = playerId;
        this.longName = longName;
        this.gender = gender != null ? gender.name() : null;
        this.nationality = nationality;
        this.dob = dob;
    }


//    public PlayerNodeDTO(Long id, Integer playerId, String mongoId, String longName,
//                         PlayerNode.Gender gender, String nationality, String preferredFoot,
//                         LocalDate dob, String position) {
//        this.id = id;
//        this.playerId = playerId;
////        this.mongoId = mongoId;
//        this.longName = longName;
//        this.gender = gender != null ? gender.name() : null;
//        this.nationality = nationality;
////        this.preferredFoot = preferredFoot;
////        this.dob = dob;
////        this.position = position;
//        this.age = dob != null ? calculateAge(dob) : null;
//    }

    private Double calculateAge(LocalDate dob) {
        return (double) ChronoUnit.YEARS.between(dob, LocalDate.now());
    }



    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Integer getOverall() {
        return overall;
    }

    public void setOverall(Integer overall) {
        this.overall = overall;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
