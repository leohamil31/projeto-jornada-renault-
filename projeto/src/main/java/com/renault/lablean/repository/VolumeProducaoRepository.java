package com.renault.lablean.repository;

import com.renault.lablean.entity.VolumeProducao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VolumeProducaoRepository extends JpaRepository<VolumeProducao, Long> {
    Optional<VolumeProducao> findByApontamento_IdApontamento(Long idApontamento);
}
