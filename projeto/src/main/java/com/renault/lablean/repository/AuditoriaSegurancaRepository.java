package com.renault.lablean.repository;

import com.renault.lablean.entity.AuditoriaSeguranca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuditoriaSegurancaRepository extends JpaRepository<AuditoriaSeguranca, Long> {
    Optional<AuditoriaSeguranca> findByApontamento_IdApontamento(Long idApontamento);
}
