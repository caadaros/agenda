package com.peluqueria.agenda.Exception;

/*Manejador Global de Excepciones. Su función principal es "interceptar" 
los errores que ocurren en cualquier parte de tu aplicación 
(especialmente en los controladores) y transformarlos en una respuesta 
JSON limpia y organizada para el cliente
*/

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/*Esta notación indica que si falla algo en el Controlador, 
esta clase tiene las instrucciones para manejarlo*/
@RestControllerAdvice
public class GlobalException {
    // Captura errores de @Valid en los Controllers.
    // Devuelve 400 con { "campo": "mensaje de error" }.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errores.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errores);
    }

    /* Captura las excepciones de tipo RuntimeException, los cuales suelen ser 
    errores de lógica de negocio que tú lanzas manualmente en el Service
    Devuelve 400 con { "error": "mensaje" }.*/
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(
            RuntimeException ex) {

        Map<String, String> error = new LinkedHashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
