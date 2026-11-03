package com.renault.lablean.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_auditoria_5s")
public class Auditoria5s {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Long idAuditoria5s;

    // Relacionamento 1 para 1 com a tabela de Apontamento
    @OneToOne
    @JoinColumn(name = "id_apontamento", nullable = false, unique = true)
    private Apontamento apontamento;

    @Column(name = "nota_seiri", nullable = false, columnDefinition = "TINYINT")
    private Integer notaSeiri;

    @Column(name = "nota_seiton", nullable = false, columnDefinition = "TINYINT")
    private Integer notaSeiton;

    @Column(name = "nota_seiso", nullable = false, columnDefinition = "TINYINT" )
    private Integer notaSeiso;

    @Column(name = "nota_seiketsu", nullable = false, columnDefinition = "TINYINT")
    private Integer notaSeiketsu;

    @Column(name = "nota_shitsuke", nullable = false, columnDefinition = "TINYINT")
    private Integer notaShitsuke;

    // O banco calcula, o Java apenas lê o resultado.
    @Column(name = "nota_total", insertable = false, updatable = false, columnDefinition = "DECIMAL(4,2)")
    private Double notaTotal;

    @Column(name = "plano_acao", columnDefinition = "TEXT")
    private String planoAcao;

    // --- GETTERS E SETTERS MANUAIS ---

    public Long getIdAuditoria5s() { return idAuditoria5s; }
    public void setIdAuditoria5s(Long idAuditoria5s) { this.idAuditoria5s = idAuditoria5s; }

    public Apontamento getApontamento() { return apontamento; }
    public void setApontamento(Apontamento apontamento) { this.apontamento = apontamento; }

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

    public Double getNotaTotal() { return notaTotal; }
    public void setNotaTotal(Double notaTotal) { this.notaTotal = notaTotal; }

    public String getPlanoAcao() { return planoAcao; }
    public void setPlanoAcao(String planoAcao) { this.planoAcao = planoAcao; }

    public Long getIdAuditoria() { return idAuditoria5s; }
    public void setIdAuditoria(Long idAuditoria) { this.idAuditoria5s = idAuditoria; }
}
