package Unipi.Fifa.models;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
    @Property(name = "Followdate")
    private Date Followdate;

    @Relationship(type = "Seguire", direction = Relationship.Direction.OUTGOING)
    private List<User> users;
    @Property(name = "Seguiredate")
    private Date Seguiredate;

    @Relationship(type="Piace", direction = Relationship.Direction.OUTGOING)
    private List<ClubNode> clubNodes;
    @Property(name = "PiaceDate")
    private Date PiaceDate;


    public List<PlayerNode> getPlayerNodes() {
        return playerNodes;
    }

    public void setPlayerNodes(List<PlayerNode> playerNodes) {
        this.playerNodes = playerNodes;
    }

    public Date getFollowdate() {
        return Followdate;
    }

    public void setFollowdate(Date followdate) {
        Followdate = followdate;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Date getSeguiredate() {
        return Seguiredate;
    }

    public void setSeguiredate(Date seguiredate) {
        Seguiredate = seguiredate;
    }

    public List<ClubNode> getClubNodes() {
        return clubNodes;
    }

    public void setClubNodes(List<ClubNode> clubNodes) {
        this.clubNodes = clubNodes;
    }

    public Date getPiaceDate() {
        return PiaceDate;
    }

    public void setPiaceDate(Date piaceDate) {
        PiaceDate = piaceDate;
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
