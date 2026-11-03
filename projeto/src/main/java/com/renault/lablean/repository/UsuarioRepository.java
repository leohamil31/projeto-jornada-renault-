package com.renault.lablean.repository;

import com.renault.lablean.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Útil para quando o usuário for fazer login ou buscar os dados dele
    Usuario findByMatricula(String matricula);
    Usuario findByEmail(String email);
}