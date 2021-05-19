package br.com.abce.sai.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Column(nullable = false, scale = 120)
    @Getter
    @Setter
    private String nome;

    @Column(nullable = false, scale = 100)
    @Getter
    @Setter
    private String login;

    @Email
    @Column(nullable = false)
    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String imageUrl;

    @Column(nullable = false)
    @Getter
    @Setter
    private Boolean emailVerified = false;

    @JsonIgnore
    @Getter
    @Setter
    private String password;

    @NotNull
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_role",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id")
    )
    @Getter
    @Setter
    private List<Role> roles;

    public <T> Usuario(String name, String email, String password, Stream<T> role) {
        this.nome = name;
        this.email = email;
        this.password = password;
        this.roles = (List<Role>) role;
    }

    public Usuario(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.login = usuario.getLogin();
        this.email = usuario.getEmail();
        this.imageUrl = usuario.getImageUrl();
        this.emailVerified = usuario.getEmailVerified();
        this.password = usuario.getPassword();
        this.provider = usuario.getProvider();
        this.providerId = usuario.getProviderId();
        this.roles = usuario.getRoles();
    }
}
