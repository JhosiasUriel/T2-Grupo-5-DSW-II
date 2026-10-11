package com.pagoya.resolucion.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResolucionRequest {

	@NotNull
	@Min(2)
	@Max(3)
	private Integer estado;

	@NotBlank
	private String analista;

	@NotBlank
	private String observacion;
}
