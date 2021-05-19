package br.com.abce.sai.persistence.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Column(nullable = false, scale = 20)
    @Getter
    @Setter
    private String nome;

    public Role(String roleName) {
        this.nome = roleName;
    }

    @Override
    public String getAuthority() {
        return this.nome;
    }
}
