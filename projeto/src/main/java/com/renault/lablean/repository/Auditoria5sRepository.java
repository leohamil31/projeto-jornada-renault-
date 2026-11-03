package com.renault.lablean.repository;

import com.renault.lablean.entity.Auditoria5s;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface Auditoria5sRepository extends JpaRepository<Auditoria5s, Long> {

    @Query("SELECT a FROM Auditoria5s a WHERE a.apontamento.idApontamento = :idApontamento")
    Optional<Auditoria5s> buscarPorIdApontamento(@Param("idApontamento") Long idApontamento);

}