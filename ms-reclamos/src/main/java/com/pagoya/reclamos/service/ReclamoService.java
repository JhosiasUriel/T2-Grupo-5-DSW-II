package com.pagoya.reclamos.service;

import org.springframework.stereotype.Service;

import com.pagoya.reclamos.dto.ReclamoRequest;
import com.pagoya.reclamos.dto.ReclamoUpdateRequest;
import com.pagoya.reclamos.entity.Reclamo;
import com.pagoya.reclamos.exception.ReclamoNotFoundException;
import com.pagoya.reclamos.repository.ReclamoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReclamoService {

	private final ReclamoRepository repository;

	public Reclamo registrar(ReclamoRequest req) {
		Reclamo r = new Reclamo();
		r.setFechareclamo(req.getFechareclamo());
		r.setNomcliente(req.getNomcliente());
		r.setUltimos4tarjeta(req.getUltimos4tarjeta());
		r.setMotivo(req.getMotivo());
		return repository.save(r);
	}

	public Reclamo buscar(Long num) {
		return repository.findById(num).orElseThrow(() -> new ReclamoNotFoundException(num));
	}

	public Reclamo actualizar(Long num, ReclamoUpdateRequest req) {
		Reclamo r = buscar(num);
		if (req.getAnalista() != null) r.setAnalista(req.getAnalista());
		if (req.getEstado() != null) r.setEstado(req.getEstado());
		if (req.getObservacion() != null) r.setObservacion(req.getObservacion());
		return repository.save(r);
	}
}
