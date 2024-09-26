package kolya.study.bookservice.service;

import kolya.study.bookservice.entity.Role;
import kolya.study.bookservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role findRoleOrCreate(String authority) {
        Optional<Role> byAuthority = roleRepository.findByAuthority(authority);
        if (byAuthority.isPresent()) {
            return byAuthority.get();
        }
        else{
            Role role = new Role();
            role.setAuthority("ROLE_USER");
            return roleRepository.save(role);
        }
    }
}
