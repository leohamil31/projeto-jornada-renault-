package com.renault.lablean.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record VolumeProducaoRequestDTO(
    @NotNull(message = "O ID do Apontamento é obrigatório")
    @Positive(message = "O ID do Apontamento deve ser maior que zero")
    Long idApontamento,

    @NotNull(message = "A quantidade produzida é obrigatória")
    @PositiveOrZero(message = "A quantidade produzida não pode ser negativa")
    Integer quantidadeProduzida
) {}
