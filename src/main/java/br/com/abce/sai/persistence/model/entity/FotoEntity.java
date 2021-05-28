package br.com.abce.sai.persistence.model.entity;

import br.com.abce.sai.persistence.model.Usuario;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "foto", schema = "sai", catalog = "")
public class FotoEntity {

    @Id
    @Column(name = "id_foto", nullable = false)
    private int idFoto;

    @NotNull(message = "foto é obrigatório.")
    @Basic
    @Column(name = "imagem", nullable = false)
    private byte[] imagem;

    @OneToMany(mappedBy = "fotoByFotoIdFoto")
    private Collection<ImovelHasFotoEntity> imovelHasFotosByIdFoto;

    @OneToOne(mappedBy = "fotoByFotoIdFoto", fetch = FetchType.LAZY)
    private Usuario usuariosByIdFoto;
}
