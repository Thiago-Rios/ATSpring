package com.infnet.assessment.repositories;

import com.infnet.assessment.models.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Usuario findUsuarioByNome(String nome);
}
