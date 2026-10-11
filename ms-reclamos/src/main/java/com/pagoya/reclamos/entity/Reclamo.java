package com.pagoya.reclamos.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reclamos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reclamo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long numreclamo;

	private LocalDate fechareclamo;

	@NotBlank
	private String nomcliente;

	@NotBlank
	@Size(min = 4, max = 4)
	@Column(length = 4)
	private String ultimos4tarjeta;

	@NotBlank
	private String motivo;

	private String analista = "No asignado";

	private Integer estado = 1;

	private String observacion = "por evaluar";

	@PrePersist
	void defaults() {
		if (analista == null) analista = "No asignado";
		if (estado == null) estado = 1;
		if (observacion == null) observacion = "por evaluar";
		if (fechareclamo == null) fechareclamo = LocalDate.now();
	}
}
