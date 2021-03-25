package rapidus.project_management.auth.service.impl;

import org.springframework.stereotype.Service;
import rapidus.project_management.auth.model.User;
import rapidus.project_management.auth.model.UserDTO;
import rapidus.project_management.auth.model.request.UserRequest;
import rapidus.project_management.auth.model.response.user.UserResponse;
import rapidus.project_management.auth.service.UserMapper;

@Service
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO mapUserToUserDTO(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getPassword(), user.getUsername());
    }

    @Override
    public User mapUserDTOToUser(UserDTO user) {
        return new User(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }

    @Override
    public UserDTO mapUserRequestToUserDTO(UserRequest user) {
        return new UserDTO(0, user.getEmail(), user.getPassword(), user.getUsername());
    }

    @Override
    public UserResponse mapUserDTOToUserResponse(UserDTO user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }
}
