package com.renault.lablean.service.impl;

import com.renault.lablean.dto.request.ApontamentoRequestDTO;
import com.renault.lablean.dto.request.ApontamentoUpdateDTO;
import com.renault.lablean.dto.response.ApontamentoResponseDTO;
import com.renault.lablean.entity.*;
import com.renault.lablean.repository.*;
import com.renault.lablean.enums.StatusApontamento;
import com.renault.lablean.exception.ResourceNotFoundException;
import com.renault.lablean.service.ApontamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ApontamentoServiceImpl implements ApontamentoService {

    private final ApontamentoRepository apontamentoRepository;
    private final AreaRepository areaRepository;
    private final TurnoRepository turnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoIndicadorRepository tipoIndicadorRepository;

    public ApontamentoServiceImpl(ApontamentoRepository apontamentoRepository,
                                  AreaRepository areaRepository,
                                  TurnoRepository turnoRepository,
                                  UsuarioRepository usuarioRepository,
                                  TipoIndicadorRepository tipoIndicadorRepository) {
        this.apontamentoRepository = apontamentoRepository;
        this.areaRepository = areaRepository;
        this.turnoRepository = turnoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoIndicadorRepository = tipoIndicadorRepository;
    }

    @Override
    public List<ApontamentoResponseDTO> listarTodos() {
        return apontamentoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Override
    @Transactional
    public ApontamentoResponseDTO criarApontamento(ApontamentoRequestDTO dto) {
        
        // 1. Buscar as entidades no banco (Agora usando a sua nova Exceção Customizada!)
        Area area = areaRepository.findById(dto.getIdArea())
                .orElseThrow(() -> new ResourceNotFoundException("Área não encontrada com ID: " + dto.getIdArea()));
                
        Turno turno = turnoRepository.findById(dto.getIdTurno())
                .orElseThrow(() -> new ResourceNotFoundException("Turno não encontrado com ID: " + dto.getIdTurno()));
                
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + dto.getIdUsuario()));
                
        TipoIndicador tipoIndicador = tipoIndicadorRepository.findById(dto.getIdTipoIndicador())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Indicador não encontrado com ID: " + dto.getIdTipoIndicador()));

        // 2. Instanciar a entidade Apontamento e popular os dados
        Apontamento apontamento = new Apontamento();
        apontamento.setArea(area);
        apontamento.setTurno(turno);
        apontamento.setUsuario(usuario);
        apontamento.setTipoIndicador(tipoIndicador);
        
        apontamento.setDataHora(java.time.LocalDateTime.now());
        apontamento.setObservacoes(dto.getObservacoes());
        apontamento.setStatus(StatusApontamento.PENDENTE_VALIDACAO); 

        // 3. Salvar no banco
        Apontamento salvo = apontamentoRepository.save(apontamento);
        
        // 4. Converter a entidade salva para o DTO Limpo usando o método privado abaixo
        return converterParaDTO(salvo);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        // Verifica se existe, caso contrário lança o erro que o ControllerAdvice vai capturar
        if (!apontamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Apontamento não encontrado com o ID: " + id);
        }
        apontamentoRepository.deleteById(id);
    }

    // --- MÉTODO TRADUTOR (De Entidade para DTO) ---
    private ApontamentoResponseDTO converterParaDTO(Apontamento apontamento) {
        return new ApontamentoResponseDTO(
                apontamento.getIdApontamento(),
                apontamento.getArea().getNome(),
                apontamento.getTurno().getNome(),
                apontamento.getUsuario().getNome(),
                apontamento.getTipoIndicador().getDescricao(),
                apontamento.getDataHora(),
                apontamento.getObservacoes(),
                apontamento.getStatus().name()
        );
    }

        @Override
        @Transactional
        public ApontamentoResponseDTO atualizar(Long id, ApontamentoUpdateDTO dto) {
        Apontamento apontamento = apontamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Apontamento não encontrado com o ID: " + id));

        // Atualiza apenas os campos que vieram preenchidos
        if (dto.getObservacoes() != null) {
                apontamento.setObservacoes(dto.getObservacoes());
        }
        
        if (dto.getStatus() != null) {
                apontamento.setStatus(StatusApontamento.valueOf(dto.getStatus().toUpperCase()));
        }

        Apontamento salvo = apontamentoRepository.save(apontamento);
        return converterParaDTO(salvo);
        }

        @Override
        public List<ApontamentoResponseDTO> buscarPorStatus(String status) {
        try {
                // Converte a String ("PENDENTE_VALIDACAO") para o Enum
                StatusApontamento statusEnum = StatusApontamento.valueOf(status.toUpperCase());
                
                // Busca no banco, passa pelo stream e converte tudo para DTO limpo
                return apontamentoRepository.findByStatus(statusEnum)
                        .stream()
                        .map(this::converterParaDTO)
                        .toList();
                        
        } catch (IllegalArgumentException e) {
                throw new ResourceNotFoundException("Status inválido: " + status);
        }
        }
}