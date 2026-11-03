package com.renault.lablean.repository;

import com.renault.lablean.entity.TipoIndicador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoIndicadorRepository extends JpaRepository<TipoIndicador, Long> {
    TipoIndicador findByCodigo(String codigo);
}