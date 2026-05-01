package net.mkubik.gmms.controller;

import net.mkubik.gmms.exception.ApplicationException;
import net.mkubik.gmms.exception.ResourceAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    // 404
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
//        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
//    }
//
//    // 409
//    @ExceptionHandler({
//            ResourceAlreadyExistsException.class,
//            ConflictingStateException.class
//    })
//    public ProblemDetail handleConflict(ApplicationException ex) {
//        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
//    }

    // 409
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ProblemDetail handleAlreadyExists(ApplicationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()
        );
    }
}
