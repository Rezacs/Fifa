package Unipi.Fifa.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubAverageRatingDTO {
    private String clubName;
    private Double averageOverall;

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public Double getAverageOverall() {
        return averageOverall;
    }

    public void setAverageOverall(Double averageOverall) {
        this.averageOverall = averageOverall;
    }
}
