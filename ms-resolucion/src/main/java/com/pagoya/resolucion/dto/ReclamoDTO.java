package com.pagoya.resolucion.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReclamoDTO {

	private Long numreclamo;

	private LocalDate fechareclamo;

	private String nomcliente;

	private String ultimos4tarjeta;

	private String motivo;

	private String analista;

	private Integer estado;

	private String observacion;
}
