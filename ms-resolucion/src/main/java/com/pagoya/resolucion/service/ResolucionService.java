package com.pagoya.resolucion.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.pagoya.resolucion.client.ReclamoClient;
import com.pagoya.resolucion.dto.ReclamoDTO;
import com.pagoya.resolucion.dto.ReclamoUpdateRequest;
import com.pagoya.resolucion.dto.ResolucionRequest;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResolucionService {

	private final ReclamoClient reclamoClient;

	public Map<String, Object> resolver(Long num, ResolucionRequest req) {
		try {
			reclamoClient.buscar(num);
		} catch (FeignException.NotFound e) {
			throw new com.pagoya.resolucion.exception.ReclamoNotFoundException(num);
		}

		ReclamoUpdateRequest update = new ReclamoUpdateRequest();
		update.setAnalista(req.getAnalista());
		update.setEstado(req.getEstado());
		update.setObservacion(req.getObservacion());

		ReclamoDTO actualizado = reclamoClient.actualizar(num, update);

		String tipo = req.getEstado() == 2 ? "procedente" : "improcedente";
		return Map.of("mensaje", "Reclamo " + tipo + " actualizado correctamente", "reclamo", actualizado);
	}
}
