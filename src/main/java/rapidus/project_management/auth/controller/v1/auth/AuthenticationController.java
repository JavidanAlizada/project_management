package rapidus.project_management.auth.controller.v1.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rapidus.project_management.auth.model.UserDTO;
import rapidus.project_management.auth.model.request.UserRequest;
import rapidus.project_management.auth.model.response.token.TokenResponse;
import rapidus.project_management.auth.model.response.user.UserResponse;
import rapidus.project_management.auth.security.JwtTokenUtil;
import rapidus.project_management.auth.service.UserMapper;
import rapidus.project_management.auth.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/auth/", method = RequestMethod.POST)
    public ResponseEntity<TokenResponse> createAuthenticationToken(@RequestBody UserRequest userRequest) throws Exception {
        final UserDTO userDetails = userService.loadUserByEmailAndPassword(userRequest.getEmail(), userRequest.getPassword());
        String token = null;
        if (userDetails != null) {
            token = jwtTokenUtil.generateToken(userDetails);
        }
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @RequestMapping(value = "/example", method = RequestMethod.GET)
    public ResponseEntity<String> getInfo(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null)
            return ResponseEntity.ok("Hello");
        return new ResponseEntity<>("You have no Authorization header", HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/register/", method = RequestMethod.POST)
    public ResponseEntity<UserResponse> saveUser(@RequestBody UserRequest userRequest) throws Exception {
        UserDTO savedDto = userMapper.mapUserRequestToUserDTO(userRequest);
        UserDTO newUser = userService.save(savedDto);
        UserResponse userResponse = userMapper.mapUserDTOToUserResponse(newUser);
        return ResponseEntity.ok(userResponse);
    }

}