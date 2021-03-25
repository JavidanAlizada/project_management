package rapidus.project_management.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rapidus.project_management.auth.exception.user.PasswordInvalidException;
import rapidus.project_management.auth.exception.user.UserNotFoundException;
import rapidus.project_management.auth.model.User;
import rapidus.project_management.auth.model.UserDTO;
import rapidus.project_management.auth.repository.UserRepository;
import rapidus.project_management.auth.service.UserMapper;
import rapidus.project_management.auth.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private UserMapper mapper;

    public UserDTO loadUserByEmailAndPassword(String email, String password) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        if (!bcryptEncoder.matches(password, user.getPassword())) {
            throw new PasswordInvalidException("User password is invalid");
        }
        return mapper.mapUserToUserDTO(user);
    }

    public UserDTO loadUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User Not Found for given id: " + id));
        return mapper.mapUserToUserDTO(user);
    }

    public List<UserDTO> loadUsers() {
        Iterable<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        users.forEach((user) -> userDTOS.add(mapper.mapUserToUserDTO(user)));
        return userDTOS;
    }

    public UserDTO save(UserDTO user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        User userSaved = userRepository.save(newUser);
        return mapper.mapUserToUserDTO(userSaved);
    }

    public UserDTO updateUserExceptPassword(UserDTO user) {
        Optional userInDb = userRepository.findById(user.getId());
        User newUser = userInDb.isPresent() ? (User) userInDb.get() : null;
        if (newUser == null)
            throw new UserNotFoundException("User Not Found for given id: " + user.getId());
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        User userSaved = userRepository.save(newUser);
        return mapper.mapUserToUserDTO(userSaved);
    }

    public void delete(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User Not Found for given id: " + id));
        userRepository.delete(user);
    }

//    public UserDTO updateUserPassword(String password) {
//        User newUser = new User();
//        newUser.setUsername(user.getUsername());
//        newUser.setEmail(user.getEmail());
//        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
//        User userSaved = userRepository.save(newUser);
//        return mapper.mapUserToUserDTO(userSaved);
//    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
