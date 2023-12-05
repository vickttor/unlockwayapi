package unlockway.unlockway_api.exceptions;

public class ResourceNotFoundException extends Exception{
  public ResourceNotFoundException(String statement) {
    super(statement);
  }
}
