package br.com.abce.sai.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "endereco", schema = "sai", catalog = "")
public class Endereco {

    @Id
    @Column(name = "id_endereco", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEndereco;

    @NotBlank(message = "O cep é obritatório.")
    @Size(message = "O cep deve ter 8 caracteres", min = 8, max = 8)
    @Pattern(regexp = "^\\d{8}$", message = "O cep deve conter apenas números")
    @Column(name = "cep", nullable = false, length = 8)
    private String cep;

    @NotBlank(message = "O logradouro é obritatório.")
    @Size(message = "O logradouro deve entre 5 a 50 caracteres", min = 5, max = 50)
    @Column(name = "logradouro", nullable = false, length = 50)
    private String logradouro;

    @NotBlank(message = "O complemento é obritatório.")
    @Size(message = "O logradouro deve entre 5 a 45 caracteres", min = 5, max = 50)
    @Column(name = "complemento", nullable = false, length = 45)
    private String complemento;

    @NotBlank(message = "O numero é obritatório.")
    @Size(message = "O número pode ter até 5 caracteres", max = 5)
    @Column(name = "numero", nullable = true, length = 5)
    private String numero;

    @NotBlank(message = "A UF é obritatória")
    @Size(message = "A UF deve ter 2 caracteres", min = 2, max = 2)
    @Column(name = "uf", nullable = false, length = 2)
    private String uf;

    @NotBlank(message = "A cidade é obritatória")
    @Size(message = "A cidade deve ter até 70 caracteres", max = 70)
    @Column(name = "cidade", nullable = false, length = 70)
    private String cidade;

    @NotBlank(message = "O bairro é obritatória")
    @Size(message = "O bairro deve ter até 45 caracteres", max = 45)
    @Column(name = "bairro", nullable = false, length = 45)
    private String bairro;

//    @OneToMany(mappedBy = "enderecoByEnderecoIdEndereco")
//    private Collection<Corretor> corretorsByIdEndereco;
//
//    @OneToMany(mappedBy = "enderecoByEnderecoIdEndereco")
//    private Collection<Imovel> imovelsByIdEndereco;
}
