package com.renault.lablean.controller;

import com.renault.lablean.entity.Area;
import com.renault.lablean.repository.AreaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
@CrossOrigin(origins = "*")
public class AreaController {

    private final AreaRepository areaRepository;

    public AreaController(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Area>> listarTodas() {
        // Vai no banco, faz um SELECT * FROM tb_area e devolve como JSON
        List<Area> areas = areaRepository.findAll();
        return ResponseEntity.ok(areas);
    }
}