package com.renault.lablean.repository;

import com.renault.lablean.entity.ApuracaoOee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ApuracaoOeeRepository extends JpaRepository<ApuracaoOee, Long> {

    @Query("SELECT o FROM ApuracaoOee o WHERE o.apontamento.idApontamento = :idApontamento")
    Optional<ApuracaoOee> buscarPorIdApontamento(@Param("idApontamento") Long idApontamento);

}