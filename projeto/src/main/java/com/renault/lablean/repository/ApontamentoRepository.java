package com.renault.lablean.repository;

import com.renault.lablean.entity.Apontamento;
import com.renault.lablean.enums.StatusApontamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApontamentoRepository extends JpaRepository<Apontamento, Long> {
    
    // Traz todos os apontamentos de um usuário específico
    List<Apontamento> findByUsuario_IdUsuario(Long idUsuario);

    // O Spring traduz "findByStatus" para: SELECT * FROM tb_apontamento WHERE status = ?
    List<Apontamento> findByStatus(StatusApontamento status);

}