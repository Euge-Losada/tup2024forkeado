package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaResponseDto;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.model.exception.InvalidDniException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TupResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidDniException.class)
    public ResponseEntity<CustomApiError> handleInvalidDniException(InvalidDniException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(new CustomApiError(4001, ex.getMessage()));
    }



    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<CustomApiError> handleBusinessLogicException(BusinessLogicException ex, WebRequest request) {
        CustomApiError error = new CustomApiError(4000, ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomApiError> handleGenericException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomApiError(5000, "Error inesperado: " + ex.getMessage()));
    }

}
