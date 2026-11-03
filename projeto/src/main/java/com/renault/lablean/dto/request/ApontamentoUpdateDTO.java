package com.renault.lablean.dto.request;

public class ApontamentoUpdateDTO {
    private String observacoes;
    private String status; // Usamos String para receber do JSON e depois converter para o Enum

    // Getters e Setters
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}