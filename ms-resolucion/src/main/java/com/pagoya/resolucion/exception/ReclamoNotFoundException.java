package com.pagoya.resolucion.exception;

public class ReclamoNotFoundException extends RuntimeException {

	public ReclamoNotFoundException(Long num) {
		super("Reclamo no existe: " + num);
	}
}
