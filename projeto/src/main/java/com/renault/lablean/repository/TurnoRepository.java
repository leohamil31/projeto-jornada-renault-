package com.renault.lablean.repository;

import com.renault.lablean.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {
    Turno findByCodigo(String codigo);
}