package kit.prolog.exception;

import kit.prolog.controller.PostController;
import kit.prolog.dto.SuccessDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = PostController.class)
public class ExceptionController {
    private static final String SERVER_ERROR = "Unexpected Server Error";

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<SuccessDto> handleAccessDeniedException(AccessDeniedException e){
        return new ResponseEntity<>(new SuccessDto(false, e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<SuccessDto> handleIllegalArgumentException(IllegalArgumentException e){
        return new ResponseEntity<>(new SuccessDto(false, e.getMessage()), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<SuccessDto> handleNullPointerException(NullPointerException e){
        return new ResponseEntity<>(new SuccessDto(false, e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<SuccessDto> handleRuntimeException(RuntimeException e){
        return new ResponseEntity<>(new SuccessDto(false, SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
