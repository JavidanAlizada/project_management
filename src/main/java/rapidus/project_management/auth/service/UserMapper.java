package rapidus.project_management.auth.service;

import org.springframework.stereotype.Component;
import rapidus.project_management.auth.model.User;
import rapidus.project_management.auth.model.UserDTO;
import rapidus.project_management.auth.model.request.UserRequest;
import rapidus.project_management.auth.model.response.user.UserResponse;

public interface UserMapper {

    UserDTO mapUserToUserDTO(User user);

    User mapUserDTOToUser(UserDTO user);

    UserDTO mapUserRequestToUserDTO(UserRequest user);

    UserResponse mapUserDTOToUserResponse(UserDTO user);
}
