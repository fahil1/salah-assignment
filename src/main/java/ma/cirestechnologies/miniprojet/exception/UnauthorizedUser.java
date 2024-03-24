package ma.cirestechnologies.miniprojet.exception;

public class UnauthorizedUser extends Exception {
    public UnauthorizedUser() {
        super();
    }

    public UnauthorizedUser(String message) {
        super(message);
    }

    public UnauthorizedUser(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedUser(Throwable cause) {
        super(cause);
    }

    protected UnauthorizedUser(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
