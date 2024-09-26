package kolya.study.bookservice.service;

import kolya.study.bookservice.entity.Role;
import kolya.study.bookservice.entity.User;
import kolya.study.bookservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> user  = userRepository.findByUsername(username);
      if (user.isPresent()) return new MyUserDetails(user.get());
      else throw new UsernameNotFoundException("not found");
    }
}
