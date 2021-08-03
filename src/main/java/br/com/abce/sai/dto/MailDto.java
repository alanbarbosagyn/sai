package br.com.abce.sai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {

    @Email(message = "E-mail destinatário inválido")
    @NotNull(message = "E-mail destinatário obrigatório")
    private String destinatario;

    @NotNull(message = "Assunto obrigatório")
    private String assunto;

    @NotNull(message = "Texto do e-mail obrigatório")
    private String corpoTexto;
}
