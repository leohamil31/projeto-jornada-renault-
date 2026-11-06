package com.renault.lablean.controller;

import com.renault.lablean.dto.request.AuditoriaSegurancaRequestDTO;
import com.renault.lablean.dto.response.AuditoriaSegurancaResponseDTO;
import com.renault.lablean.service.AuditoriaSegurancaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seguranca")
@CrossOrigin(origins = "*")
public class AuditoriaSegurancaController {

    private final AuditoriaSegurancaService auditoriaService;

    public AuditoriaSegurancaController(AuditoriaSegurancaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @PostMapping
    public ResponseEntity<AuditoriaSegurancaResponseDTO> salvar(
        @Valid @RequestBody AuditoriaSegurancaRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auditoriaService.salvar(dto));
    }

    @GetMapping("/apontamento/{idApontamento}")
    public ResponseEntity<AuditoriaSegurancaResponseDTO> buscarPorApontamento(
        @PathVariable Long idApontamento
    ) {
        return ResponseEntity.ok(auditoriaService.buscarPorApontamento(idApontamento));
    }
}
