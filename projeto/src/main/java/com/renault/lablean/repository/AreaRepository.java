package com.renault.lablean.repository;

import com.renault.lablean.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    // Exemplo de "mágica" do Spring: 
    // Só de escrever esse nome, ele cria o SQL: SELECT * FROM tb_area WHERE codigo = ?
    Area findByCodigo(String codigo);
}