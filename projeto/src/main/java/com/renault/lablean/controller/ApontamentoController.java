package com.renault.lablean.controller;

import com.renault.lablean.dto.request.ApontamentoRequestDTO;
import com.renault.lablean.dto.request.ApontamentoUpdateDTO;
import com.renault.lablean.dto.response.ApontamentoResponseDTO;
import com.renault.lablean.entity.Apontamento;
import com.renault.lablean.service.ApontamentoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/apontamentos")
@CrossOrigin(origins = "*") 
public class ApontamentoController {

    private final ApontamentoService apontamentoService;

    // Construtor criado manualmente (O Spring faz a injeção de dependência aqui)
    public ApontamentoController(ApontamentoService apontamentoService) {
        this.apontamentoService = apontamentoService;
    }
 

    @PostMapping
    public ResponseEntity<ApontamentoResponseDTO> criarApontamento(@Valid @RequestBody ApontamentoRequestDTO dto) {
        ApontamentoResponseDTO criado = apontamentoService.criarApontamento(dto);

        System.out.println("=== CHEGOU NO CONTROLLER ===");
        System.out.println("ID Area: " + dto.getIdArea());
        System.out.println("ID Turno: " + dto.getIdTurno());
        System.out.println("ID Usuario: " + dto.getIdUsuario());
        System.out.println("ID Tipo Indicador: " + dto.getIdTipoIndicador());
        System.out.println("============================");

        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }
    // ... método POST ...
// Importe o DTO: import com.renault.lablean.dto.response.ApontamentoResponseDTO;

    @GetMapping
    public ResponseEntity<List<ApontamentoResponseDTO>> listarTodos() {
        List<ApontamentoResponseDTO> lista = apontamentoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
    apontamentoService.deletar(id);
    return ResponseEntity.noContent().build();
    
    }

        @PutMapping("/{id}")
    public ResponseEntity<ApontamentoResponseDTO> atualizar(@PathVariable Long id, @RequestBody ApontamentoUpdateDTO dto) {
        ApontamentoResponseDTO atualizado = apontamentoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/status/{status}")
        public ResponseEntity<List<ApontamentoResponseDTO>> buscarPorStatus(@PathVariable String status) {
            List<ApontamentoResponseDTO> lista = apontamentoService.buscarPorStatus(status);
            return ResponseEntity.ok(lista);
        }

}

