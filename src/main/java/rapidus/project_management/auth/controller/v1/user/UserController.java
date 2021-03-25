package rapidus.project_management.auth.controller.v1.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rapidus.project_management.auth.exception.user.UserNotFoundException;
import rapidus.project_management.auth.model.UserDTO;
import rapidus.project_management.auth.model.request.UserRequest;
import rapidus.project_management.auth.model.response.Response;
import rapidus.project_management.auth.model.response.user.UserResponse;
import rapidus.project_management.auth.security.JwtTokenUtil;
import rapidus.project_management.auth.service.UserMapper;
import rapidus.project_management.auth.service.UserService;
import rapidus.project_management.auth.service.helper.UserAuthorizationHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @Autowired
    private UserAuthorizationHelper userAuthorization;

    @GetMapping("")
    public ResponseEntity<Response<List<UserResponse>>> getAllUsers() {
        List<UserDTO> userDetails = userService.loadUsers();
        List<UserResponse> userResponses = new ArrayList<>();
        if (userDetails.isEmpty()) {
            Response<List<UserResponse>> response = new Response<>(
                    new UserNotFoundException("User not found. User List is Empty"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        for (UserDTO userDTO : userDetails) {
            UserResponse userResponse = userMapper.mapUserDTOToUserResponse(userDTO);
            userResponses.add(userResponse);
        }
        Response<List<UserResponse>> response = new Response<>(userResponses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Integer id,
                                                    HttpServletRequest request) {
        if (userAuthorization.isUserNonAuthorized(request, tokenUtil, id))
            throw new UserNotFoundException("User Not Found for given id: " + id);
        UserDTO userDTO = userService.loadUserById(id);
        UserResponse userResponse = userMapper.mapUserDTOToUserResponse(userDTO);
        return ResponseEntity.ok(userResponse);
    }

    @PatchMapping("/{id}/")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable("id") Integer id,
                                                       @RequestBody UserRequest userRequest,
                                                       HttpServletRequest request) {
        if (userAuthorization.isUserNonAuthorized(request, tokenUtil, id))
            throw new UserNotFoundException("User Not Found for given id: " + id);
        UserDTO userDto = userMapper.mapUserRequestToUserDTO(userRequest);
        userDto.setId(id);
        UserDTO updatedUser = userService.updateUserExceptPassword(userDto);
        UserResponse userResponse = userMapper.mapUserDTOToUserResponse(updatedUser);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Integer id, HttpServletRequest request) {
        if (userAuthorization.isUserNonAuthorized(request, tokenUtil, id))
            throw new UserNotFoundException("User Not Found for given id: " + id);
        userService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}
