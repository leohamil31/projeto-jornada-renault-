package com.renault.lablean.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ApontamentoRequestDTO {

    @NotNull(message = "O ID da Área é obrigatório")
    private Long idArea;

    @NotNull(message = "O ID do Turno é obrigatório")
    private Long idTurno;

    @NotNull(message = "O ID do Usuário é obrigatório")
    private Long idUsuario;

    @NotNull(message = "O ID do Tipo de Indicador é obrigatório")
    private Long idTipoIndicador;

    // A data pode ficar sem anotação se não for obrigatória no envio (já que o backend preenche automaticamente)
    private LocalDateTime dataHora; 

    @NotBlank(message = "As observações não podem estar em branco")
    private String observacoes;


    // --- GETTERS E SETTERS MANUAIS ---

    public Long getIdArea() {
        return idArea;
    }

    public void setIdArea(Long idArea) {
        this.idArea = idArea;
    }

    public Long getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Long idTurno) {
        this.idTurno = idTurno;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdTipoIndicador() {
        return idTipoIndicador;
    }

    public void setIdTipoIndicador(Long idTipoIndicador) {
        this.idTipoIndicador = idTipoIndicador;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}