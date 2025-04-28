package Unipi.Fifa.objects;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CoachClubHistoryDTO {

    private Integer teamId;
    private String teamName;
    private String nationalityName;
    private List<Integer> fifaVersionsManaged;

}
