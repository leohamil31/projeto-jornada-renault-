package com.renault.lablean.service.impl;

import com.renault.lablean.dto.request.ApuracaoOeeRequestDTO;
import com.renault.lablean.dto.response.ApuracaoOeeResponseDTO;
import com.renault.lablean.entity.Apontamento;
import com.renault.lablean.entity.ApuracaoOee;
import com.renault.lablean.repository.ApontamentoRepository;
import com.renault.lablean.repository.ApuracaoOeeRepository;
import com.renault.lablean.service.ApuracaoOeeService;
import org.springframework.stereotype.Service;

@Service
public class ApuracaoOeeServiceImpl implements ApuracaoOeeService {

    private final ApuracaoOeeRepository apuracaoOeeRepository;
    private final ApontamentoRepository apontamentoRepository;

    // Injetamos também o repositório de Apontamento
    public ApuracaoOeeServiceImpl(ApuracaoOeeRepository apuracaoOeeRepository, 
                                  ApontamentoRepository apontamentoRepository) {
        this.apuracaoOeeRepository = apuracaoOeeRepository;
        this.apontamentoRepository = apontamentoRepository;
    }

    @Override
    public ApuracaoOeeResponseDTO calcularESalvar(ApuracaoOeeRequestDTO dto) {
        // 1. Buscar o Apontamento pai
        Apontamento apontamento = apontamentoRepository.findById(dto.idApontamento())
                .orElseThrow(() -> new RuntimeException("Apontamento não encontrado com ID: " + dto.idApontamento()));

        // 2. Converter DTO para Entidade
        ApuracaoOee oee = new ApuracaoOee();
        oee.setApontamento(apontamento);
        oee.setTempoPlanejadoMinutos(dto.tempoPlanejadoMinutos());
        oee.setTempoParadaMinutos(dto.tempoParadaMinutos());
        oee.setQuantidadeProduzida(dto.producaoTotal());
        oee.setQuantidadeRefugo(dto.pecasRefugadas());
        oee.setTempoCicloIdealSegundos(dto.tempoCicloPadrao());

        // 3. Processar matemática e salvar (reaproveitando o método existente)
        ApuracaoOee salvo = processarEGravarOee(oee);

        // 4. Retornar ResponseDTO
        return new ApuracaoOeeResponseDTO(
                salvo.getIdApuracao(),
                salvo.getApontamento().getIdApontamento(),
                salvo.getTempoPlanejadoMinutos(),
                salvo.getTempoParadaMinutos(),
                salvo.getQuantidadeProduzida(),
                salvo.getQuantidadeRefugo(),
                salvo.getTempoCicloIdealSegundos(),
                salvo.getDisponibilidade(),
                salvo.getPerformance(),
                salvo.getQualidade(),
                salvo.getOeeTotal()
        );
    }

    @Override
    public ApuracaoOee processarEGravarOee(ApuracaoOee oee) {
        calcularIndicadoresOee(oee);
        return apuracaoOeeRepository.save(oee);
    }

    private void calcularIndicadoresOee(ApuracaoOee oee) {
        double tempoPlanejado = oee.getTempoPlanejadoMinutos();
        double tempoParada = oee.getTempoParadaMinutos();
        double cicloIdealSegundos = oee.getTempoCicloIdealSegundos();
        double qtdProduzida = oee.getQuantidadeProduzida();
        double qtdRefugo = oee.getQuantidadeRefugo();

        double tempoProdutivoMinutos = tempoPlanejado - tempoParada;
        double disponibilidade = tempoPlanejado > 0 ? (tempoProdutivoMinutos / tempoPlanejado) : 0.0;

        double tempoProdutivoSegundos = tempoProdutivoMinutos * 60.0;
        double performance = tempoProdutivoSegundos > 0 ? ((cicloIdealSegundos * qtdProduzida) / tempoProdutivoSegundos) : 0.0;

        double qualidade = qtdProduzida > 0 ? ((qtdProduzida - qtdRefugo) / qtdProduzida) : 0.0;

        double oeeTotal = disponibilidade * performance * qualidade;

        oee.setDisponibilidade(disponibilidade * 100);
        oee.setPerformance(performance * 100);
        oee.setQualidade(qualidade * 100);
        oee.setOeeTotal(oeeTotal * 100);
    }
    @Override
    public ApuracaoOeeResponseDTO buscarPorIdApontamento(Long idApontamento) {
        ApuracaoOee oee = apuracaoOeeRepository.buscarPorIdApontamento(idApontamento)
                .orElseThrow(() -> new RuntimeException("Apuração OEE não encontrada para o Apontamento ID: " + idApontamento));

        return new ApuracaoOeeResponseDTO(
                oee.getIdApuracao(),
                oee.getApontamento().getIdApontamento(),
                oee.getTempoPlanejadoMinutos(),
                oee.getTempoParadaMinutos(),
                oee.getQuantidadeProduzida(),
                oee.getQuantidadeRefugo(),
                oee.getTempoCicloIdealSegundos(),
                oee.getDisponibilidade(),
                oee.getPerformance(),
                oee.getQualidade(),
                oee.getOeeTotal()
        );
    }
}