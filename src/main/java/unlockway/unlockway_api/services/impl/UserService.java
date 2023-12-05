package  unlockway.unlockway_api.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unlockway.unlockway_api.DTO.user.*;
import unlockway.unlockway_api.azure.services.BlobStorage;
import unlockway.unlockway_api.config.JwtService;
import unlockway.unlockway_api.enums.Role;
import unlockway.unlockway_api.exceptions.ResourceNotFoundException;
import unlockway.unlockway_api.models.relationships.GoalsModel;
import unlockway.unlockway_api.models.UserModel;
import unlockway.unlockway_api.repositories.UserRepository;
import unlockway.unlockway_api.services.interfaces.IUserService;
import unlockway.unlockway_api.utils.UnlockwayUtils;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UnlockwayUtils utils;
    private final ModelMapper modelMapper;

    @Override
    public AuthenticationResponseDTO register(SaveUserDTO userDTO) throws Exception {
        UserModel userToBeRegistered = new UserModel();

        Optional<UserModel> existingUser = repository.findByEmail(userDTO.getEmail().toLowerCase());

        if(existingUser.isPresent()) {
            throw new Exception("usuário com e-mail " + userDTO.getEmail() + " já existe");
        }

        userToBeRegistered.setFirstname(userDTO.getFirstname());
        userToBeRegistered.setLastname(userDTO.getLastname());
        userToBeRegistered.setEmail(userDTO.getEmail().toLowerCase());
        userToBeRegistered.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userToBeRegistered.setBiotype(userDTO.getBiotype());
        userToBeRegistered.setWeight(userDTO.getWeight());
        userToBeRegistered.setHeight(userDTO.getHeight());
        userToBeRegistered.setSex(userDTO.getSex());
        userToBeRegistered.setRole(Role.USER);
        userToBeRegistered.setDeviceToken(userDTO.getDeviceToken());

        // Goals
        GoalsModel userGoals = new GoalsModel();

        userGoals.setLoseWeight(userDTO.getGoals().isLoseWeight());
        userGoals.setMaintainHealth(userDTO.getGoals().isMaintainHealth());
        userGoals.setGainMuscularMass(userDTO.getGoals().isGainMuscularMass());

        userToBeRegistered.setGoals(userGoals);

        UserModel createdUser = repository.save(userToBeRegistered);

        return generateTokenAndReturnUser(createdUser);
    }

    private AuthenticationResponseDTO generateTokenAndReturnUser(UserModel user) {
        var jwtToken = jwtService.generateToken(user);

        UserGoalsDTO goals = new UserGoalsDTO();

        goals.setLoseWeight(user.getGoals().isLoseWeight());
        goals.setGainMuscularMass(user.getGoals().isGainMuscularMass());
        goals.setMaintainHealth(user.getGoals().isMaintainHealth());

        AuthenticationResponseDTO response =  modelMapper.map(returnUserDTO(user), AuthenticationResponseDTO.class);
        response.setToken(jwtToken);

        return response;
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticateUserDTO credentials) throws Exception {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));

        if(authentication.isAuthenticated()) {
            var user = repository.findByEmail(credentials.getEmail()).orElseThrow(()-> new Exception("Usuário não existe"));
            return generateTokenAndReturnUser(user);
        }else{
            throw new Exception("Usuário com e-mail ou senha inválida");
        }
    }

    @Override
    public AuthenticationResponseDTO updateById(UUID id, SaveUserDTO userDTO) throws ResourceNotFoundException {
        UserModel existingUser = repository.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("Usuário não encontrado!"));

        if(userDTO.getEmail() != null)  existingUser.setEmail(userDTO.getEmail());
        if(userDTO.getPassword() != null) existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if(userDTO.getFirstname() != null) existingUser.setFirstname(userDTO.getFirstname());
        if(userDTO.getLastname() != null) existingUser.setLastname(userDTO.getLastname());
        if(userDTO.getHeight() != null) existingUser.setHeight(userDTO.getHeight());
        if(userDTO.getWeight() !=  null) existingUser.setWeight(userDTO.getWeight());
        if(userDTO.getBiotype() != null) existingUser.setBiotype(userDTO.getBiotype());
        if(userDTO.getSex() != null) existingUser.setSex(userDTO.getSex());
        if(userDTO.getDeviceToken() != null) existingUser.setDeviceToken(userDTO.getDeviceToken());

        // Goals
        if(userDTO.getGoals() != null) {
            existingUser.getGoals().setLoseWeight(userDTO.getGoals().isLoseWeight());
            existingUser.getGoals().setMaintainHealth(userDTO.getGoals().isMaintainHealth());
            existingUser.getGoals().setGainMuscularMass(userDTO.getGoals().isGainMuscularMass());
        }

        var updatedUser = repository.save(existingUser);

        return generateTokenAndReturnUser(updatedUser);
    }

    @Override
    public GetUserDTO findById(UUID id) throws ResourceNotFoundException {
        var user = repository.findById(id)
        .orElseThrow(()->new ResourceNotFoundException("Usuário não encontrado!"));

        return returnUserDTO(user);
    }

    private GetUserDTO returnUserDTO(UserModel user) {
        UserGoalsDTO goals = new UserGoalsDTO();

        goals.setLoseWeight(user.getGoals().isLoseWeight());
        goals.setGainMuscularMass(user.getGoals().isGainMuscularMass());
        goals.setMaintainHealth(user.getGoals().isMaintainHealth());

        return GetUserDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .photo(user.getPhoto())
            .sex(user.getSex())
            .height(user.getHeight())
            .weight(user.getWeight())
            .imc(utils.generateUserImc(user.getHeight(), user.getWeight()))
            .goals(goals)
            .biotype(user.getBiotype())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }

    @Override
    public UserModel getUserAnalysis(UUID id){
        return repository.getUserAnalysis(id);
    }

    @Override
    @Transactional
    public String setProfilePhoto(UUID userId, MultipartFile photo) throws ResourceNotFoundException {
        UserModel user = repository.findById(userId).orElseThrow(()->new ResourceNotFoundException("Usuário não existe e por tanto não pode ter uma foto salva"));
        String containerName = "users";

        deleteUserPhotoFromAzureBlobStorage(user, containerName);

        String filename = BlobStorage.storePhotoIntoAzureBlobStorage(photo, containerName);

        user.setPhoto("https://unlockways3.blob.core.windows.net/" + containerName  + "/" + filename);

        repository.save(user);

        return user.getPhoto();
    }


    private void deleteUserPhotoFromAzureBlobStorage(UserModel user, String containerName) {
        var oldPhoto = user.getPhoto();

        if(oldPhoto != null) {
            oldPhoto = oldPhoto.substring(("https://unlockways3.blob.core.windows.net/" + containerName + "/").length());
            BlobStorage.removePhotoFromAzureBlobStorage(oldPhoto, containerName);
        }
    }
}
