package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    Optional<Usuario> findByIdUsuario(Long id);

    Optional<Usuario> findByLogin(String login);

    Optional<Usuario> findByLoginOrEmail(String login, String email);

    Iterable<Usuario> findByStatus(String status);

    Boolean existsByLogin(String login);

    Boolean existsByEmail(String email);
}
