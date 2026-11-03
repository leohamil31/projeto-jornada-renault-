package com.renault.lablean.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "tb_turno")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Long idTurno;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;


    // ==========================================
    // GETTERS E SETTERS MANUAIS
    // ==========================================
    public Long getIdTurno() { return idTurno; }
    public void setIdTurno(Long idTurno) { this.idTurno = idTurno; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public java.time.LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(java.time.LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public java.time.LocalTime getHoraFim() { return horaFim; }
    public void setHoraFim(java.time.LocalTime horaFim) { this.horaFim = horaFim; }
}