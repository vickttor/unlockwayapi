package unlockway.unlockway_api.controllers.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import unlockway.unlockway_api.DTO.user.AuthenticateUserDTO;
import unlockway.unlockway_api.DTO.user.AuthenticationResponseDTO;
import unlockway.unlockway_api.DTO.user.SaveUserDTO;
import unlockway.unlockway_api.services.impl.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthenticationController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SaveUserDTO payload) throws Exception {
        try {
            AuthenticationResponseDTO user = service.register(payload);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);

        }catch(Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticateUserDTO credentials) throws Exception{
        try {
            return ResponseEntity.ok(service.authenticate(credentials));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
