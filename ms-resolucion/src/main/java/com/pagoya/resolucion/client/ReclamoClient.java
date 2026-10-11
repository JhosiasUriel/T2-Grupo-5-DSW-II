package com.pagoya.resolucion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.pagoya.resolucion.dto.ReclamoDTO;
import com.pagoya.resolucion.dto.ReclamoUpdateRequest;

@FeignClient(name = "ms-reclamos")
public interface ReclamoClient {

	@GetMapping("/reclamos/{num}")
	ReclamoDTO buscar(@PathVariable("num") Long num);

	@PutMapping("/reclamos/{num}")
	ReclamoDTO actualizar(@PathVariable("num") Long num, @RequestBody ReclamoUpdateRequest req);
}
