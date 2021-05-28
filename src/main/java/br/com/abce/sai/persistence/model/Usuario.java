package br.com.abce.sai.persistence.model;

import br.com.abce.sai.persistence.model.entity.FotoEntity;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario", schema = "sai", catalog = "")
public class Usuario {

    @Id
    @Column(name = "id_usuario", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idUsuario;

    @Column(name = "login", nullable = true, length = 45)
    private String login;

    @Column(name = "senha", nullable = true, length = 45)
    private String senha;

    @Column(name = "status", nullable = true, length = 45)
    private String status;

//    @Column(name = "foto_id_foto", nullable = false)
//    private int fotoIdFoto;

    @OneToOne(mappedBy = "usuarioByUsuarioIdUsuario")
    private Construtor construtorsByIdUsuario;

    @OneToOne(mappedBy = "usuarioByUsuarioIdUsuario")
    private Corretor corretorsByIdUsuario;

    @OneToOne
    @JoinColumn(name = "foto_id_foto", referencedColumnName = "id_foto", nullable = false)
    private FotoEntity fotoByFotoIdFoto;
}
