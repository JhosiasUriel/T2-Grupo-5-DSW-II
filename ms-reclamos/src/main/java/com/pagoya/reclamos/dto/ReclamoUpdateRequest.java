package com.pagoya.reclamos.dto;

import lombok.Data;

@Data
public class ReclamoUpdateRequest {

	private String analista;

	private Integer estado;

	private String observacion;
}
