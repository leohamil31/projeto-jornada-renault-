package com.renault.lablean.controller;

import com.renault.lablean.dto.request.ApuracaoOeeRequestDTO;
import com.renault.lablean.dto.response.ApuracaoOeeResponseDTO;
import com.renault.lablean.service.ApuracaoOeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/oee")
@CrossOrigin(origins = "*")
public class ApuracaoOeeController {

    private final ApuracaoOeeService apuracaoOeeService;

    public ApuracaoOeeController(ApuracaoOeeService apuracaoOeeService) {
        this.apuracaoOeeService = apuracaoOeeService;
    }

    @GetMapping("/apontamento/{idApontamento}")
    public ResponseEntity<ApuracaoOeeResponseDTO> buscarPorApontamento(@PathVariable Long idApontamento) {
        ApuracaoOeeResponseDTO response = apuracaoOeeService.buscarPorIdApontamento(idApontamento);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApuracaoOeeResponseDTO> calcularESalvar(@RequestBody @Valid ApuracaoOeeRequestDTO dto) {
        ApuracaoOeeResponseDTO response = apuracaoOeeService.calcularESalvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}