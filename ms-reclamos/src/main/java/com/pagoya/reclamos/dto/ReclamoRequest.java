package com.pagoya.reclamos.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReclamoRequest {

	private LocalDate fechareclamo;

	@NotBlank
	private String nomcliente;

	@NotBlank
	@Size(min = 4, max = 4)
	private String ultimos4tarjeta;

	@NotBlank
	private String motivo;
}
