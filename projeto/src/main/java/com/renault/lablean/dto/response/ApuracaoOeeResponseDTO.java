package com.renault.lablean.dto.response;

public record ApuracaoOeeResponseDTO(
    Long idApuracaoOee,
    Long idApontamento,
    Integer tempoPlanejadoMinutos,
    Integer tempoParadaMinutos,
    Integer producaoTotal,
    Integer pecasRefugadas,
    Double tempoCicloPadrao,
    Double indiceDisponibilidade,
    Double indicePerformance,
    Double indiceQualidade,
    Double indiceOee
) {}