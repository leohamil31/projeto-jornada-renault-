package com.renault.lablean.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AuditoriaSegurancaRequestDTO(
    @NotNull(message = "O ID do Apontamento é obrigatório")
    @Positive(message = "O ID do Apontamento deve ser maior que zero")
    Long idApontamento,

    @NotNull(message = "Informe a conformidade dos EPIs")
    Boolean epiConforme,

    @NotNull(message = "Informe a conformidade das proteções das máquinas")
    Boolean protecoesMaquinasConformes,

    @NotNull(message = "Informe se as rotas de emergência estão desobstruídas")
    Boolean rotasEmergenciaDesobstruidas,

    @NotNull(message = "Informe se a sinalização está adequada")
    Boolean sinalizacaoAdequada,

    @NotNull(message = "Informe se a área está livre de riscos evidentes")
    Boolean areaSemRiscosEvidentes,

    String planoAcao
) {
    @AssertTrue(message = "Informe um plano de ação para os itens não conformes")
    public boolean isPlanoAcaoValido() {
        boolean todosConformes = Boolean.TRUE.equals(epiConforme)
            && Boolean.TRUE.equals(protecoesMaquinasConformes)
            && Boolean.TRUE.equals(rotasEmergenciaDesobstruidas)
            && Boolean.TRUE.equals(sinalizacaoAdequada)
            && Boolean.TRUE.equals(areaSemRiscosEvidentes);
        return todosConformes || (planoAcao != null && !planoAcao.isBlank());
    }
}
