package com.renault.lablean.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Auditoria5sRequestDTO {

    @NotNull(message = "O ID do Apontamento é obrigatório")
    private Long idApontamento;

    @NotNull(message = "A nota de Utilização (Seiri) é obrigatória")
    @Min(value = 0) @Max(value = 5)
    private Integer notaSeiri;

    @NotNull(message = "A nota de Organização (Seiton) é obrigatória")
    @Min(value = 0) @Max(value = 5)
    private Integer notaSeiton;

    @NotNull(message = "A nota de Limpeza (Seiso) é obrigatória")
    @Min(value = 0) @Max(value = 5)
    private Integer notaSeiso;

    @NotNull(message = "A nota de Padronização (Seiketsu) é obrigatória")
    @Min(value = 0) @Max(value = 5)
    private Integer notaSeiketsu;

    @NotNull(message = "A nota de Disciplina (Shitsuke) é obrigatória")
    @Min(value = 0) @Max(value = 5)
    private Integer notaShitsuke;

    private String planoAcao;

    public Long getIdApontamento() { return idApontamento; }
    public void setIdApontamento(Long idApontamento) { this.idApontamento = idApontamento; }

    public Integer getNotaSeiri() { return notaSeiri; }
    public void setNotaSeiri(Integer notaSeiri) { this.notaSeiri = notaSeiri; }

    public Integer getNotaSeiton() { return notaSeiton; }
    public void setNotaSeiton(Integer notaSeiton) { this.notaSeiton = notaSeiton; }

    public Integer getNotaSeiso() { return notaSeiso; }
    public void setNotaSeiso(Integer notaSeiso) { this.notaSeiso = notaSeiso; }

    public Integer getNotaSeiketsu() { return notaSeiketsu; }
    public void setNotaSeiketsu(Integer notaSeiketsu) { this.notaSeiketsu = notaSeiketsu; }

    public Integer getNotaShitsuke() { return notaShitsuke; }
    public void setNotaShitsuke(Integer notaShitsuke) { this.notaShitsuke = notaShitsuke; }

    public String getPlanoAcao() { return planoAcao; }
    public void setPlanoAcao(String planoAcao) { this.planoAcao = planoAcao; }
}