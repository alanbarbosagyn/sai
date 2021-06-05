package br.com.abce.sai.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "foto", schema = "sai", catalog = "")
public class Foto {

    @Id
    @Column(name = "id_foto", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idFoto;

    @NotNull(message = "foto é obrigatório.")
    @Lob
    @Column(name = "imagem", nullable = false)
    @JsonIgnore
    private byte[] imagem;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    @Column(name = "nome_arquivo", nullable = false, length = 100)
    private String nameFile;

//    @OneToMany(mappedBy = "fotoByFotoIdFoto")
//    private Collection<ImovelHasFotoEntity> imovelHasFotosByIdFoto;

//    @OneToOne(mappedBy = "fotoByFotoIdFoto", fetch = FetchType.LAZY)
//    private Usuario usuariosByIdFoto;
}
