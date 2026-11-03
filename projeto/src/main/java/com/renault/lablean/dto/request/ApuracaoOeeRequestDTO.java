package com.renault.lablean.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ApuracaoOeeRequestDTO(
    @NotNull(message = "O ID do Apontamento é obrigatório")
    @Positive(message = "O ID do Apontamento deve ser válido e maior que zero")
    Long idApontamento,

    @NotNull(message = "O tempo planejado é obrigatório")
    @Positive(message = "O tempo planejado deve ser maior que zero")
    Integer tempoPlanejadoMinutos,

    @NotNull(message = "O tempo de parada é obrigatório")
    @PositiveOrZero(message = "O tempo de parada não pode ser negativo")
    Integer tempoParadaMinutos,

    @NotNull(message = "A produção total é obrigatória")
    @PositiveOrZero(message = "A produção total não pode ser negativa")
    Integer producaoTotal,

    @NotNull(message = "A quantidade de peças refugadas é obrigatória")
    @PositiveOrZero(message = "A quantidade de refugo não pode ser negativa")
    Integer pecasRefugadas,

    @NotNull(message = "O tempo de ciclo padrão é obrigatório")
    @Positive(message = "O tempo de ciclo padrão deve ser maior que zero")
    Double tempoCicloPadrao
) {}