package com.renault.lablean.dto.response;

import java.util.Map;

public record AuditoriaSegurancaResponseDTO(
    Long idAuditoriaSeguranca,
    Long idApontamento,
    Map<String, Boolean> verificacoes,
    String status,
    String planoAcao
) {}
