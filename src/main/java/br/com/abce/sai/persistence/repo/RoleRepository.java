package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByNome(String name);
}
