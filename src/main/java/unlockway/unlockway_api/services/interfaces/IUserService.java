package unlockway.unlockway_api.services.interfaces;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import unlockway.unlockway_api.DTO.user.AuthenticateUserDTO;
import unlockway.unlockway_api.DTO.user.AuthenticationResponseDTO;
import unlockway.unlockway_api.DTO.user.GetUserDTO;
import unlockway.unlockway_api.DTO.user.SaveUserDTO;
import unlockway.unlockway_api.exceptions.ResourceNotFoundException;
import unlockway.unlockway_api.models.UserModel;

public interface IUserService {
  AuthenticationResponseDTO register(SaveUserDTO user) throws Exception;
  AuthenticationResponseDTO authenticate(AuthenticateUserDTO credentials) throws Exception;

  AuthenticationResponseDTO updateById(UUID id, SaveUserDTO user) throws ResourceNotFoundException;
  GetUserDTO findById(UUID id) throws ResourceNotFoundException;
  UserModel getUserAnalysis(UUID id) throws ResourceNotFoundException;
  String setProfilePhoto(UUID userId, MultipartFile photo) throws ResourceNotFoundException;
}
