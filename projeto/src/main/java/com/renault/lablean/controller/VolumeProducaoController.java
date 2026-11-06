package com.renault.lablean.controller;

import com.renault.lablean.dto.request.VolumeProducaoRequestDTO;
import com.renault.lablean.dto.response.VolumeProducaoResponseDTO;
import com.renault.lablean.service.VolumeProducaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/volume-producao")
@CrossOrigin(origins = "*")
public class VolumeProducaoController {

    private final VolumeProducaoService volumeService;

    public VolumeProducaoController(VolumeProducaoService volumeService) {
        this.volumeService = volumeService;
    }

    @PostMapping
    public ResponseEntity<VolumeProducaoResponseDTO> salvar(
        @Valid @RequestBody VolumeProducaoRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(volumeService.salvar(dto));
    }

    @GetMapping("/apontamento/{idApontamento}")
    public ResponseEntity<VolumeProducaoResponseDTO> buscarPorApontamento(
        @PathVariable Long idApontamento
    ) {
        return ResponseEntity.ok(volumeService.buscarPorApontamento(idApontamento));
    }
}
