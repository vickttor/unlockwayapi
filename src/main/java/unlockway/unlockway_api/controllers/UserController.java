package  unlockway.unlockway_api.controllers;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import unlockway.unlockway_api.DTO.user.GetUserDTO;
import unlockway.unlockway_api.DTO.user.SaveUserDTO;
import unlockway.unlockway_api.models.UserModel;
import unlockway.unlockway_api.services.impl.UserService;

@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final ModelMapper modelMapper;
    private final UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id){
        try {
            GetUserDTO user = service.findById(id);
            GetUserDTO userDTO = modelMapper.map(user, GetUserDTO.class);

            return ResponseEntity.status(HttpStatus.OK).body(userDTO);
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());   
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody SaveUserDTO user) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateById(id, user));
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<?> getUserAnalysis(@PathVariable UUID id){
        try {
            UserModel user = service.getUserAnalysis(id);
            GetUserDTO userDTO = modelMapper.map(user, GetUserDTO.class);

            return ResponseEntity.status(HttpStatus.OK).body(userDTO);
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/photo/{id}")
    public ResponseEntity<String> setUserProfilePhoto(@PathVariable UUID id, @RequestParam(value = "photo", required = true) MultipartFile profilePhoto) {
        try {
            var filename = service.setProfilePhoto(id, profilePhoto);
            return ResponseEntity.status(HttpStatus.OK).body(filename);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
