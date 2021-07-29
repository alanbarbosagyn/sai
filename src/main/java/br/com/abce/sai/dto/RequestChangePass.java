package br.com.abce.sai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestChangePass {

    @Email(message = "Login inválido.")
    @NotNull(message = "Login obrigatório.")
    private String login;

    @NotNull(message = "Senha obrigatória.")
    private String password;

    @NotNull(message = "Nova senha obrigatória.")
    private String newPassword;
}
