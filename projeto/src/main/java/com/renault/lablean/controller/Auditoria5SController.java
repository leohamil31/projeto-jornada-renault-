package com.renault.lablean.controller;

import com.renault.lablean.dto.request.Auditoria5sRequestDTO;
import com.renault.lablean.dto.response.Auditoria5SResponseDTO;
import com.renault.lablean.service.Auditoria5sService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/5s")
@CrossOrigin(origins = "*")
public class Auditoria5SController {

    private final Auditoria5sService auditoria5sService;

    public Auditoria5SController(Auditoria5sService auditoria5sService) {
        this.auditoria5sService = auditoria5sService;
    }

    @PostMapping
    public ResponseEntity<Auditoria5SResponseDTO> registrarAuditoria(@Valid @RequestBody Auditoria5sRequestDTO dto) {
        Auditoria5SResponseDTO salva = auditoria5sService.salvarAuditoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auditoria5SResponseDTO> buscarAuditoria(@PathVariable Long id) {
        Auditoria5SResponseDTO auditoria = auditoria5sService.buscarPorId(id);
        return ResponseEntity.ok(auditoria);
    }

    @GetMapping("/apontamento/{idApontamento}")
    public ResponseEntity<Auditoria5SResponseDTO> buscarAuditoriaPorApontamento(@PathVariable Long idApontamento) {
        Auditoria5SResponseDTO auditoria = auditoria5sService.buscarPorIdApontamento(idApontamento);
        return ResponseEntity.ok(auditoria);
    }
}