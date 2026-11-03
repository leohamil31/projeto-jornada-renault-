package com.renault.lablean.entity;

import com.renault.lablean.enums.StatusApontamento;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_apontamento")
public class Apontamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idApontamento;

    @ManyToOne
    @JoinColumn(name = "id_area", nullable = false)
    private Area area;

    @ManyToOne
    @JoinColumn(name = "id_turno", nullable = false)
    private Turno turno;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "id_tipo_indicador", nullable = false)
    private TipoIndicador tipoIndicador;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusApontamento status = StatusApontamento.PENDENTE_VALIDACAO;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    // --- GETTERS E SETTERS MANUAIS ---

    public Long getIdApontamento() {
        return idApontamento;
    }

    public void setIdApontamento(Long idApontamento) {
        this.idApontamento = idApontamento;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TipoIndicador getTipoIndicador() {
        return tipoIndicador;
    }

    public void setTipoIndicador(TipoIndicador tipoIndicador) {
        this.tipoIndicador = tipoIndicador;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusApontamento getStatus() {
        return status;
    }

    public void setStatus(StatusApontamento status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}