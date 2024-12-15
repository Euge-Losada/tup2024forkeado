package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaResponseDto;
import ar.edu.utn.frbb.tup.model.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    // Manejo de excepciones de tipo CuentaAlreadyExistsException
    @ExceptionHandler(CuentaAlreadyExistsException.class)
    public ResponseEntity<CustomApiError> handleCuentaAlreadyExistsException(CuentaAlreadyExistsException ex) {
        CustomApiError error = new CustomApiError(4002, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Manejo de excepciones de tipo TipoCuentaAlreadyExistsException
    @ExceptionHandler(TipoCuentaAlreadyExistsException.class)
    public ResponseEntity<CustomApiError> handleTipoCuentaAlreadyExistsException(TipoCuentaAlreadyExistsException ex) {
        CustomApiError error = new CustomApiError(4003, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<CustomApiError> handleInvalidDateFormatException(InvalidDateFormatException ex) {
        CustomApiError error = new CustomApiError(4004, ex.getMessage());
        return ResponseEntity.badRequest().body(error);
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomApiError> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        CustomApiError error = new CustomApiError(4004, ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }



}
