package com.renault.lablean.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_apuracao_oee")
public class ApuracaoOee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_apuracao")
    private Long idApuracao;

    // Relacionamento 1 para 1 com o Apontamento Pai
    @OneToOne
    @JoinColumn(name = "id_apontamento", nullable = false, unique = true)
    private Apontamento apontamento;

    // 1. Variáveis para Disponibilidade
    @Column(name = "tempo_planejado_minutos", nullable = false)
    private Integer tempoPlanejadoMinutos;

    @Column(name = "tempo_parada_minutos", nullable = false)
    private Integer tempoParadaMinutos;

    // 2. Variáveis para Performance
    @Column(name = "tempo_ciclo_ideal_segundos", nullable = false)
    private Double tempoCicloIdealSegundos;

    @Column(name = "quantidade_produzida", nullable = false)
    private Integer quantidadeProduzida;

    // 3. Variáveis para Qualidade
    @Column(name = "quantidade_refugo", nullable = false)
    private Integer quantidadeRefugo;

    // Resultados Calculados (Opcional salvar no banco, mas excelente para relatórios no Power BI)
    @Column(name = "indice_disponibilidade")
    private Double disponibilidade;

    @Column(name = "indice_performance")
    private Double performance;

    @Column(name = "indice_qualidade")
    private Double qualidade;

    @Column(name = "oee_total")
    private Double oeeTotal;

    // Construtores
    public ApuracaoOee() {}

    // Getters e Setters (Você pode gerar automaticamente com a sua IDE ou usar Lombok)
    public Long getIdApuracao() { return idApuracao; }
    public void setIdApuracao(Long idApuracao) { this.idApuracao = idApuracao; }

    public Apontamento getApontamento() { return apontamento; }
    public void setApontamento(Apontamento apontamento) { this.apontamento = apontamento; }

    public Integer getTempoPlanejadoMinutos() { return tempoPlanejadoMinutos; }
    public void setTempoPlanejadoMinutos(Integer tempoPlanejadoMinutos) { this.tempoPlanejadoMinutos = tempoPlanejadoMinutos; }

    public Integer getTempoParadaMinutos() { return tempoParadaMinutos; }
    public void setTempoParadaMinutos(Integer tempoParadaMinutos) { this.tempoParadaMinutos = tempoParadaMinutos; }

    public Double getTempoCicloIdealSegundos() { return tempoCicloIdealSegundos; }
    public void setTempoCicloIdealSegundos(Double tempoCicloIdealSegundos) { this.tempoCicloIdealSegundos = tempoCicloIdealSegundos; }

    public Integer getQuantidadeProduzida() { return quantidadeProduzida; }
    public void setQuantidadeProduzida(Integer quantidadeProduzida) { this.quantidadeProduzida = quantidadeProduzida; }

    public Integer getQuantidadeRefugo() { return quantidadeRefugo; }
    public void setQuantidadeRefugo(Integer quantidadeRefugo) { this.quantidadeRefugo = quantidadeRefugo; }

    public Double getDisponibilidade() { return disponibilidade; }
    public void setDisponibilidade(Double disponibilidade) { this.disponibilidade = disponibilidade; }

    public Double getPerformance() { return performance; }
    public void setPerformance(Double performance) { this.performance = performance; }

    public Double getQualidade() { return qualidade; }
    public void setQualidade(Double qualidade) { this.qualidade = qualidade; }

    public Double getOeeTotal() { return oeeTotal; }
    public void setOeeTotal(Double oeeTotal) { this.oeeTotal = oeeTotal; }
}