package br.com.abce.sai.config;

import br.com.abce.sai.exception.DataValidationException;
import br.com.abce.sai.exception.InfraestructureException;
import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.exception.ResourcedMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private HttpHeaders headers = new HttpHeaders();
	
	@ExceptionHandler({ RecursoNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(
      Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, Map.of("validacao",ex.getMessage()),
                getHttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ ResourcedMismatchException.class,
            DataValidationException.class })
    public ResponseEntity<Object> handleBadRequest(
      Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, Map.of("validacao", ex.getLocalizedMessage()),
          getHttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({InfraestructureException.class,
            DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleHttpMessageNotWritable(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, Map.of("erro", "Erro na aplicação, favor entrar em contato como o suporte."),
                getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }

    private HttpHeaders getHttpHeaders(){
        if (headers.isEmpty()) {
            headers.put(HttpHeaders.ACCEPT, List.of(MediaType.APPLICATION_JSON_VALUE));
            headers.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_JSON_VALUE));
        }
        return headers;
    }
}
