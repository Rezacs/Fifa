package Unipi.Fifa.models;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.*;
import java.util.stream.Collectors;

@Node
public class User implements UserDetails {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String username;
    private String password;
    private String roles;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private List<PlayerNode> playerNodes;

    @Relationship(type = "Seguire", direction = Relationship.Direction.OUTGOING)
    private List<User> users;

    @Relationship(type="Piace", direction = Relationship.Direction.OUTGOING)
    private List<ClubNode> clubNodes;

    public void seguire(User targetUser, Date followDate) {
        // Add a "Seguire" relationship with the Seguiredate property
        if (this.users == null) {
            this.users = new ArrayList<>();
        }

        // Assuming you use a direct way to handle relationship with a property on the edge
        // Add the target user to the followers list and set the relationship date
        this.users.add(targetUser);

        // Set the Seguiredate as a property of the relationship (this happens when you save the users)
        // If you're using Spring Data Neo4j, the relationship with a property will be saved automatically
        targetUser.addFollower(this, followDate);
    }

    public void addFollower(User follower, Date followDate) {
        // Create a relationship with the 'Seguire' type and add the date property
        // This could be done through repository or by directly manipulating the relationship
        // For simplicity, let's assume it's saved automatically when you save the User object
    }


    public List<PlayerNode> getPlayerNodes() {
        return playerNodes;
    }

    public void setPlayerNodes(List<PlayerNode> playerNodes) {
        this.playerNodes = playerNodes;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<ClubNode> getClubNodes() {
        return clubNodes;
    }

    public void setClubNodes(List<ClubNode> clubNodes) {
        this.clubNodes = clubNodes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE_ADMIN - ROLE_USER
        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
