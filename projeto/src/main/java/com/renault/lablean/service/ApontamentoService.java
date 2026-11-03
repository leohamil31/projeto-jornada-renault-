package com.renault.lablean.service;

import com.renault.lablean.dto.request.ApontamentoRequestDTO;
import com.renault.lablean.dto.request.ApontamentoUpdateDTO;
import com.renault.lablean.dto.response.ApontamentoResponseDTO;
import java.util.List;

public interface ApontamentoService {

    ApontamentoResponseDTO criarApontamento(ApontamentoRequestDTO dto);

    List<ApontamentoResponseDTO> listarTodos();

    void deletar(Long id);

    ApontamentoResponseDTO atualizar(Long id, ApontamentoUpdateDTO dto);

    // Adicione esta linha:
    List<ApontamentoResponseDTO> buscarPorStatus(String status);
}