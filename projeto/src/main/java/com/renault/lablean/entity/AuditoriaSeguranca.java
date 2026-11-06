package com.renault.lablean.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_auditoria_seguranca")
public class AuditoriaSeguranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria_seguranca")
    private Long idAuditoriaSeguranca;

    @OneToOne
    @JoinColumn(name = "id_apontamento", nullable = false, unique = true)
    private Apontamento apontamento;

    @Column(name = "epi_conforme", nullable = false)
    private Boolean epiConforme;

    @Column(name = "protecoes_maquinas_conformes", nullable = false)
    private Boolean protecoesMaquinasConformes;

    @Column(name = "rotas_emergencia_desobstruidas", nullable = false)
    private Boolean rotasEmergenciaDesobstruidas;

    @Column(name = "sinalizacao_adequada", nullable = false)
    private Boolean sinalizacaoAdequada;

    @Column(name = "area_sem_riscos_evidentes", nullable = false)
    private Boolean areaSemRiscosEvidentes;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(name = "plano_acao", columnDefinition = "TEXT")
    private String planoAcao;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    public Long getIdAuditoriaSeguranca() { return idAuditoriaSeguranca; }
    public void setIdAuditoriaSeguranca(Long idAuditoriaSeguranca) { this.idAuditoriaSeguranca = idAuditoriaSeguranca; }
    public Apontamento getApontamento() { return apontamento; }
    public void setApontamento(Apontamento apontamento) { this.apontamento = apontamento; }
    public Boolean getEpiConforme() { return epiConforme; }
    public void setEpiConforme(Boolean epiConforme) { this.epiConforme = epiConforme; }
    public Boolean getProtecoesMaquinasConformes() { return protecoesMaquinasConformes; }
    public void setProtecoesMaquinasConformes(Boolean protecoesMaquinasConformes) { this.protecoesMaquinasConformes = protecoesMaquinasConformes; }
    public Boolean getRotasEmergenciaDesobstruidas() { return rotasEmergenciaDesobstruidas; }
    public void setRotasEmergenciaDesobstruidas(Boolean rotasEmergenciaDesobstruidas) { this.rotasEmergenciaDesobstruidas = rotasEmergenciaDesobstruidas; }
    public Boolean getSinalizacaoAdequada() { return sinalizacaoAdequada; }
    public void setSinalizacaoAdequada(Boolean sinalizacaoAdequada) { this.sinalizacaoAdequada = sinalizacaoAdequada; }
    public Boolean getAreaSemRiscosEvidentes() { return areaSemRiscosEvidentes; }
    public void setAreaSemRiscosEvidentes(Boolean areaSemRiscosEvidentes) { this.areaSemRiscosEvidentes = areaSemRiscosEvidentes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPlanoAcao() { return planoAcao; }
    public void setPlanoAcao(String planoAcao) { this.planoAcao = planoAcao; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
