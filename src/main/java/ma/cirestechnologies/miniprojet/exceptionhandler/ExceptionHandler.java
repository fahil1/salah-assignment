package ma.cirestechnologies.miniprojet.exceptionhandler;

import ma.cirestechnologies.miniprojet.dto.ErrorResponse;
import ma.cirestechnologies.miniprojet.exception.UnauthenticatedUserException;
import ma.cirestechnologies.miniprojet.exception.UnauthorizedUserException;
import ma.cirestechnologies.miniprojet.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        HttpStatus httpStatus;
        ErrorResponse errorResponse;
        if (e instanceof UnauthenticatedUserException) {
           errorResponse = new ErrorResponse(403, e.getMessage());
           httpStatus = HttpStatus.FORBIDDEN;
        } else if (e instanceof UnauthorizedUserException) {
            errorResponse = new ErrorResponse(401, e.getMessage());
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else if (e instanceof UserNotFoundException) {
            errorResponse = new ErrorResponse(404, e.getMessage());
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            errorResponse = new ErrorResponse(500, e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
