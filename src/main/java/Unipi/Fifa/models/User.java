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
    private PlayerNode playerNode;
    @Property(name = "Followdate")
    private Date Followdate;

    @Relationship(type = "Seguire", direction = Relationship.Direction.OUTGOING)
    private User user;
    @Property(name = "Seguiredate")
    private Date Seguiredate;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerNode getPlayerNode() {
        return playerNode;
    }

    public void setPlayerNode(PlayerNode playerNode) {
        this.playerNode = playerNode;
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
