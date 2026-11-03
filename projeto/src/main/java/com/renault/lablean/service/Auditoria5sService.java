package com.renault.lablean.service;

import com.renault.lablean.dto.request.Auditoria5sRequestDTO;
import com.renault.lablean.dto.response.Auditoria5SResponseDTO;
import com.renault.lablean.entity.Apontamento;
import com.renault.lablean.entity.Auditoria5s;
import com.renault.lablean.repository.ApontamentoRepository;
import com.renault.lablean.repository.Auditoria5sRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Auditoria5sService {

    private final Auditoria5sRepository auditoria5sRepository;
    private final ApontamentoRepository apontamentoRepository;

    public Auditoria5sService(Auditoria5sRepository auditoria5sRepository, 
                              ApontamentoRepository apontamentoRepository) {
        this.auditoria5sRepository = auditoria5sRepository;
        this.apontamentoRepository = apontamentoRepository;
    }

    @Transactional
    public Auditoria5SResponseDTO salvarAuditoria(Auditoria5sRequestDTO dto) {
        
        validarNota(dto.getNotaSeiri(), "Seiri");
        validarNota(dto.getNotaSeiton(), "Seiton");
        validarNota(dto.getNotaSeiso(), "Seiso");
        validarNota(dto.getNotaSeiketsu(), "Seiketsu");
        validarNota(dto.getNotaShitsuke(), "Shitsuke");

        Apontamento apontamento = apontamentoRepository.findById(dto.getIdApontamento())
                .orElseThrow(() -> new RuntimeException("Apontamento não encontrado com ID: " + dto.getIdApontamento()));

        Auditoria5s auditoria = new Auditoria5s();
        auditoria.setApontamento(apontamento);
        auditoria.setNotaSeiri(dto.getNotaSeiri());
        auditoria.setNotaSeiton(dto.getNotaSeiton());
        auditoria.setNotaSeiso(dto.getNotaSeiso());
        auditoria.setNotaSeiketsu(dto.getNotaSeiketsu());
        auditoria.setNotaShitsuke(dto.getNotaShitsuke());
        auditoria.setPlanoAcao(dto.getPlanoAcao());

        Auditoria5s salva = auditoria5sRepository.save(auditoria);

        return new Auditoria5SResponseDTO(
                salva.getIdAuditoria(),
                salva.getApontamento().getIdApontamento(),
                salva.getNotaSeiri(),
                salva.getNotaSeiton(),
                salva.getNotaSeiso(),
                salva.getNotaSeiketsu(),
                salva.getNotaShitsuke(),
                salva.getNotaTotal(),
                salva.getPlanoAcao()
        );
    }

    public Auditoria5SResponseDTO buscarPorId(Long id) {
        Auditoria5s auditoria = auditoria5sRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auditoria não encontrada com ID: " + id));

        return new Auditoria5SResponseDTO(
                auditoria.getIdAuditoria(),
                auditoria.getApontamento().getIdApontamento(),
                auditoria.getNotaSeiri(),
                auditoria.getNotaSeiton(),
                auditoria.getNotaSeiso(),
                auditoria.getNotaSeiketsu(),
                auditoria.getNotaShitsuke(),
                auditoria.getNotaTotal(),
                auditoria.getPlanoAcao()
        );
    }

    public Auditoria5SResponseDTO buscarPorIdApontamento(Long idApontamento) {
    Auditoria5s auditoria = auditoria5sRepository.buscarPorIdApontamento(idApontamento)
            .orElseThrow(() -> new RuntimeException("Auditoria não encontrada para o Apontamento ID: " + idApontamento));

        return new Auditoria5SResponseDTO(
                auditoria.getIdAuditoria(),
                auditoria.getApontamento().getIdApontamento(),
                auditoria.getNotaSeiri(),
                auditoria.getNotaSeiton(),
                auditoria.getNotaSeiso(),
                auditoria.getNotaSeiketsu(),
                auditoria.getNotaShitsuke(),
                auditoria.getNotaTotal(),
                auditoria.getPlanoAcao()
        );
    }

    private void validarNota(Integer nota, String s) {
        if (nota == null || nota < 0 || nota > 5) {
            throw new IllegalArgumentException("A nota do 'S' (" + s + ") deve estar entre 0 e 5.");
        }
    }
}