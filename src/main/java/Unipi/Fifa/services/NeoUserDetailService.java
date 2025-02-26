package Unipi.Fifa.services;

import Unipi.Fifa.repositories.User2Repository;
import Unipi.Fifa.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NeoUserDetailService implements UserDetailsService {
    private final User2Repository user2Repository;

    public NeoUserDetailService(User2Repository user2Repository) {
        this.user2Repository = user2Repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return user2Repository
                .findFullByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found" + username));
    }
}
