package ma.cirestechnologies.miniprojet.exception;

public class UnauthenticatedUserException extends Exception {

    public UnauthenticatedUserException(String message) {
        super(message);
    }

    public UnauthenticatedUserException(Throwable cause) {
        super(cause);
    }

}
