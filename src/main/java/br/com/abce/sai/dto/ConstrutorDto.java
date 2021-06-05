package br.com.abce.sai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConstrutorDto {

    private Long id;
    private Date dataCadastro;

    @NotNull(message = "O nome é obrigatório.")
    @Size(message = "O nome pode conter até 45 caracteres.", max = 45)
    private String nome;

    @CNPJ
    @Size(message = "O CNPJ deve ter 14 dígitos.", max = 14)
    private String cnpj;

    private Long usuarioId;

}
