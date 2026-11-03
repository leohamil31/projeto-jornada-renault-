package com.renault.lablean.service;

import com.renault.lablean.dto.request.ApuracaoOeeRequestDTO;
import com.renault.lablean.dto.response.ApuracaoOeeResponseDTO;
import com.renault.lablean.entity.ApuracaoOee;

public interface ApuracaoOeeService {
    ApuracaoOee processarEGravarOee(ApuracaoOee oee);
    
    // Novo método para receber o DTO do Controller
    ApuracaoOeeResponseDTO calcularESalvar(ApuracaoOeeRequestDTO dto);
    ApuracaoOeeResponseDTO buscarPorIdApontamento(Long idApontamento);
}