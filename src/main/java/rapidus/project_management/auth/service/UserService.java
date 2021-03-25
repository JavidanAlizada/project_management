package rapidus.project_management.auth.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import rapidus.project_management.auth.model.UserDTO;

import java.util.List;

public interface UserService extends UserDetailsService {

    public UserDTO loadUserByEmailAndPassword(String email, String password);

    public UserDTO loadUserById(Integer id);

    public List<UserDTO> loadUsers();

    public UserDTO save(UserDTO user);

    public UserDTO updateUserExceptPassword(UserDTO user);

//    public UserDTO updateUserPassword(UserDTO user);

    public void delete(Integer id);

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;
}