package com.renault.lablean.service;

import com.renault.lablean.dto.request.AuditoriaSegurancaRequestDTO;
import com.renault.lablean.dto.response.AuditoriaSegurancaResponseDTO;
import com.renault.lablean.entity.Apontamento;
import com.renault.lablean.entity.AuditoriaSeguranca;
import com.renault.lablean.exception.RegistroNaoEncontradoException;
import com.renault.lablean.repository.ApontamentoRepository;
import com.renault.lablean.repository.AuditoriaSegurancaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AuditoriaSegurancaService {

    private final AuditoriaSegurancaRepository auditoriaRepository;
    private final ApontamentoRepository apontamentoRepository;

    public AuditoriaSegurancaService(
        AuditoriaSegurancaRepository auditoriaRepository,
        ApontamentoRepository apontamentoRepository
    ) {
        this.auditoriaRepository = auditoriaRepository;
        this.apontamentoRepository = apontamentoRepository;
    }

    @Transactional
    public AuditoriaSegurancaResponseDTO salvar(AuditoriaSegurancaRequestDTO dto) {
        Apontamento apontamento = apontamentoRepository.findById(dto.idApontamento())
            .orElseThrow(() -> new RegistroNaoEncontradoException(
                "Apontamento não encontrado com ID: " + dto.idApontamento()
            ));

        AuditoriaSeguranca auditoria = auditoriaRepository
            .findByApontamento_IdApontamento(dto.idApontamento())
            .orElseGet(AuditoriaSeguranca::new);
        auditoria.setApontamento(apontamento);
        auditoria.setEpiConforme(dto.epiConforme());
        auditoria.setProtecoesMaquinasConformes(dto.protecoesMaquinasConformes());
        auditoria.setRotasEmergenciaDesobstruidas(dto.rotasEmergenciaDesobstruidas());
        auditoria.setSinalizacaoAdequada(dto.sinalizacaoAdequada());
        auditoria.setAreaSemRiscosEvidentes(dto.areaSemRiscosEvidentes());
        auditoria.setStatus(todosConformes(dto) ? "CONFORME" : "AÇÃO NECESSÁRIA");
        auditoria.setPlanoAcao(dto.planoAcao());
        return paraResponse(auditoriaRepository.save(auditoria));
    }

    public AuditoriaSegurancaResponseDTO buscarPorApontamento(Long idApontamento) {
        AuditoriaSeguranca auditoria = auditoriaRepository
            .findByApontamento_IdApontamento(idApontamento)
            .orElseThrow(() -> new RegistroNaoEncontradoException(
                "Auditoria de segurança não encontrada para o Apontamento ID: " + idApontamento
            ));
        return paraResponse(auditoria);
    }

    private boolean todosConformes(AuditoriaSegurancaRequestDTO dto) {
        return Boolean.TRUE.equals(dto.epiConforme())
            && Boolean.TRUE.equals(dto.protecoesMaquinasConformes())
            && Boolean.TRUE.equals(dto.rotasEmergenciaDesobstruidas())
            && Boolean.TRUE.equals(dto.sinalizacaoAdequada())
            && Boolean.TRUE.equals(dto.areaSemRiscosEvidentes());
    }

    private AuditoriaSegurancaResponseDTO paraResponse(AuditoriaSeguranca auditoria) {
        Map<String, Boolean> verificacoes = Map.of(
            "epiConforme", auditoria.getEpiConforme(),
            "protecoesMaquinasConformes", auditoria.getProtecoesMaquinasConformes(),
            "rotasEmergenciaDesobstruidas", auditoria.getRotasEmergenciaDesobstruidas(),
            "sinalizacaoAdequada", auditoria.getSinalizacaoAdequada(),
            "areaSemRiscosEvidentes", auditoria.getAreaSemRiscosEvidentes()
        );
        return new AuditoriaSegurancaResponseDTO(
            auditoria.getIdAuditoriaSeguranca(),
            auditoria.getApontamento().getIdApontamento(),
            verificacoes,
            auditoria.getStatus(),
            auditoria.getPlanoAcao()
        );
    }
}
