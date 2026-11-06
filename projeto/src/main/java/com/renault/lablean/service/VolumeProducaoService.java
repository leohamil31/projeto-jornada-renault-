package com.renault.lablean.service;

import com.renault.lablean.dto.request.VolumeProducaoRequestDTO;
import com.renault.lablean.dto.response.VolumeProducaoResponseDTO;
import com.renault.lablean.entity.Apontamento;
import com.renault.lablean.entity.VolumeProducao;
import com.renault.lablean.exception.RegistroNaoEncontradoException;
import com.renault.lablean.repository.ApontamentoRepository;
import com.renault.lablean.repository.VolumeProducaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VolumeProducaoService {

    private final VolumeProducaoRepository volumeRepository;
    private final ApontamentoRepository apontamentoRepository;

    public VolumeProducaoService(
        VolumeProducaoRepository volumeRepository,
        ApontamentoRepository apontamentoRepository
    ) {
        this.volumeRepository = volumeRepository;
        this.apontamentoRepository = apontamentoRepository;
    }

    @Transactional
    public VolumeProducaoResponseDTO salvar(VolumeProducaoRequestDTO dto) {
        Apontamento apontamento = apontamentoRepository.findById(dto.idApontamento())
            .orElseThrow(() -> new RegistroNaoEncontradoException(
                "Apontamento não encontrado com ID: " + dto.idApontamento()
            ));

        VolumeProducao volume = volumeRepository
            .findByApontamento_IdApontamento(dto.idApontamento())
            .orElseGet(VolumeProducao::new);
        volume.setApontamento(apontamento);
        volume.setQuantidadeProduzida(dto.quantidadeProduzida());
        return paraResponse(volumeRepository.save(volume));
    }

    public VolumeProducaoResponseDTO buscarPorApontamento(Long idApontamento) {
        VolumeProducao volume = volumeRepository
            .findByApontamento_IdApontamento(idApontamento)
            .orElseThrow(() -> new RegistroNaoEncontradoException(
                "Volume de produção não encontrado para o Apontamento ID: " + idApontamento
            ));
        return paraResponse(volume);
    }

    private VolumeProducaoResponseDTO paraResponse(VolumeProducao volume) {
        return new VolumeProducaoResponseDTO(
            volume.getIdVolumeProducao(),
            volume.getApontamento().getIdApontamento(),
            volume.getQuantidadeProduzida()
        );
    }
}
