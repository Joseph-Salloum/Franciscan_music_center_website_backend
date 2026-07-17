package music_center_backend.exception.handler;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import music_center_backend.exception.exceptions.MalformedIdException;
import music_center_backend.exception.exceptions.MedalNotFoundException;
import music_center_backend.exception.exceptions.IllegalOperationException;
import music_center_backend.exception.exceptions.InvalidCredentialsException;
import music_center_backend.exception.exceptions.UserNotFoundException;
import music_center_backend.exception.exceptions.VideoNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    static class ApiError {
        public LocalDateTime timestamp;
        public int status;
        public String message;
        public String path;

        ApiError() {
            this.timestamp = LocalDateTime.now();
        }

        ApiError(int status, String message, String path) {
            this.timestamp = LocalDateTime.now();
            this.status = status;
            this.message = message;
            this.path = path;
        }
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VideoNotFoundException.class)
    public ResponseEntity<ApiError> handleVideoNotFoundException(VideoNotFoundException e, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MedalNotFoundException.class)
    public ResponseEntity<ApiError> handleMedalNotFoundException(MedalNotFoundException e, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentialsException(InvalidCredentialsException e, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.FORBIDDEN.value(), "You do not have permission to access this resource", request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalOperationException.class)
    public ResponseEntity<ApiError> handleIllegalOperationException(IllegalOperationException e, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.FORBIDDEN.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        ApiError error = new ApiError();
        error.status = HttpStatus.BAD_REQUEST.value();
        error.path = request.getRequestURI();
        
        if (e.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) e.getCause();

            if (ife.getTargetType() != null && ife.getTargetType().isEnum()) {
                String providedValue = ife.getValue().toString();
                String expectedValues = Arrays.toString(ife.getTargetType().getEnumConstants());

                error.message = String.format("Invalid value '%s'. Expected one of: %s", providedValue, expectedValues);
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
        }

        error.message = "Malformed JSON request body";
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedIdException.class)
    public ResponseEntity<ApiError> handleMalformedIdException(MalformedIdException e, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericExceptions(Exception e, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
