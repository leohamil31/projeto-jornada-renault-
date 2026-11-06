package com.renault.lablean.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_volume_producao")
public class VolumeProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_volume_producao")
    private Long idVolumeProducao;

    @OneToOne
    @JoinColumn(name = "id_apontamento", nullable = false, unique = true)
    private Apontamento apontamento;

    @Column(name = "quantidade_produzida", nullable = false)
    private Integer quantidadeProduzida;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    public Long getIdVolumeProducao() { return idVolumeProducao; }
    public void setIdVolumeProducao(Long idVolumeProducao) { this.idVolumeProducao = idVolumeProducao; }
    public Apontamento getApontamento() { return apontamento; }
    public void setApontamento(Apontamento apontamento) { this.apontamento = apontamento; }
    public Integer getQuantidadeProduzida() { return quantidadeProduzida; }
    public void setQuantidadeProduzida(Integer quantidadeProduzida) { this.quantidadeProduzida = quantidadeProduzida; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
