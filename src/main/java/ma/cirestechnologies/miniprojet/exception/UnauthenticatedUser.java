package ma.cirestechnologies.miniprojet.exception;

public class UnauthenticatedUser extends Exception {
    public UnauthenticatedUser() {
        super();
    }

    public UnauthenticatedUser(String message) {
        super(message);
    }

    public UnauthenticatedUser(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthenticatedUser(Throwable cause) {
        super(cause);
    }

    protected UnauthenticatedUser(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
