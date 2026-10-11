package com.pagoya.reclamos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pagoya.reclamos.entity.Reclamo;

public interface ReclamoRepository extends JpaRepository<Reclamo, Long> {
}
