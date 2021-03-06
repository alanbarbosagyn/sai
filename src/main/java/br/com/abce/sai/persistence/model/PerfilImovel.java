package br.com.abce.sai.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "perfil_imovel", schema = "sai")
public class PerfilImovel {

    @Id
    @Column(name = "id_perfil_imovel", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPerfilImovel;

    @NotBlank(message = "A descrição do perfil é obritatória.")
    @Size(message = "A descrição deve conter entre 10 e 45 caracteres.", min = 10, max = 45)
    @Column(name = "descricao", nullable = false, length = 45)
    private String descricao;

//    @OneToMany(mappedBy = "perfilImovelByPerfilImovelIdPerfilImovel", fetch = FetchType.LAZY)
//    private Collection<Imovel> imovelsByIdPerfilImovel;
}
