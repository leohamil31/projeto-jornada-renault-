package com.renault.lablean.controller;

import com.renault.lablean.entity.Turno;
import com.renault.lablean.repository.TurnoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@CrossOrigin(origins = "*")
public class TurnoController {

    private final TurnoRepository turnoRepository;

    public TurnoController(TurnoRepository turnoRepository) {
        this.turnoRepository = turnoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Turno>> listarTodos() {
        return ResponseEntity.ok(turnoRepository.findAll());
    }
}