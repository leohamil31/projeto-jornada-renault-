package com.renault.lablean.dto.response;

import java.time.LocalDateTime;

public record ApontamentoResponseDTO(
    Long idApontamento,
    String nomeArea,
    String nomeTurno,
    String nomeUsuario,
    String tipoIndicadorDescricao,
    LocalDateTime dataHora,
    String observacoes,
    String status
) {}