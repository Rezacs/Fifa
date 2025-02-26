package Unipi.Fifa.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class UserDTO {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserDTO(String name, String username, String roles) {
        this.username = username;
    }

    public UserDTO(String username) {
        this.username = username;
    }
}
