package com.pagoya.resolucion.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pagoya.resolucion.dto.ResolucionRequest;
import com.pagoya.resolucion.service.ResolucionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/resoluciones")
@RequiredArgsConstructor
public class ResolucionController {

	private final ResolucionService service;

	@PutMapping("/{num}")
	public ResponseEntity<Map<String, Object>> resolver(@PathVariable Long num, @Valid @RequestBody ResolucionRequest req) {
		return ResponseEntity.ok(service.resolver(num, req));
	}
}
