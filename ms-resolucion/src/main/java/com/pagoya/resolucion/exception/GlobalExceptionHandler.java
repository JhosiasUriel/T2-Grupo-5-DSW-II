package com.pagoya.resolucion.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ReclamoNotFoundException.class)
	public ResponseEntity<Map<String, String>> notFound(ReclamoNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", ex.getMessage()));
	}

	@ExceptionHandler(FeignException.NotFound.class)
	public ResponseEntity<Map<String, String>> feignNotFound(FeignException.NotFound ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Reclamo no existe"));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> badRequest(MethodArgumentNotValidException ex) {
		String msg = ex.getBindingResult().getFieldErrors().stream()
				.map(e -> e.getField() + ": " + e.getDefaultMessage())
				.findFirst().orElse("Solicitud invalida");
		return ResponseEntity.badRequest().body(Map.of("mensaje", msg));
	}
}
