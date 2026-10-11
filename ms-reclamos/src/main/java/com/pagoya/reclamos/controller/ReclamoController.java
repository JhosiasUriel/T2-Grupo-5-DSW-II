package com.pagoya.reclamos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pagoya.reclamos.dto.ReclamoRequest;
import com.pagoya.reclamos.dto.ReclamoUpdateRequest;
import com.pagoya.reclamos.entity.Reclamo;
import com.pagoya.reclamos.service.ReclamoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reclamos")
@RequiredArgsConstructor
public class ReclamoController {

	private final ReclamoService service;

	@PostMapping
	public ResponseEntity<Reclamo> registrar(@Valid @RequestBody ReclamoRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(req));
	}

	@GetMapping("/{num}")
	public ResponseEntity<Reclamo> buscar(@PathVariable Long num) {
		return ResponseEntity.ok(service.buscar(num));
	}

	@PutMapping("/{num}")
	public ResponseEntity<Reclamo> actualizar(@PathVariable Long num, @RequestBody ReclamoUpdateRequest req) {
		return ResponseEntity.ok(service.actualizar(num, req));
	}
}
