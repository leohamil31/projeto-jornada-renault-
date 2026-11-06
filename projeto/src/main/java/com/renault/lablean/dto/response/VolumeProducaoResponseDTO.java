package com.renault.lablean.dto.response;

public record VolumeProducaoResponseDTO(
    Long idVolumeProducao,
    Long idApontamento,
    Integer quantidadeProduzida
) {}
