package com.renault.lablean.mapper;

import com.renault.lablean.dto.response.ApontamentoResponseDTO;
import com.renault.lablean.entity.Apontamento;
import org.springframework.stereotype.Component;

@Component
public class ApontamentoMapper {

    public ApontamentoResponseDTO toResponseDTO(Apontamento entity) {
        if (entity == null) {
            return null;
        }

        return new ApontamentoResponseDTO(
                entity.getIdApontamento(),
                entity.getArea() != null ? entity.getArea().getNome() : null,
                entity.getTurno() != null ? entity.getTurno().getNome() : null,
                entity.getUsuario() != null ? entity.getUsuario().getNome() : null,
                entity.getTipoIndicador() != null ? entity.getTipoIndicador().getDescricao() : null,
                entity.getDataHora(),
                entity.getObservacoes(),
                entity.getStatus() != null ? entity.getStatus().name() : null
        );
    }
}