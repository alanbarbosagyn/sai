package br.com.abce.sai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestLoginDto {

    @NotNull(message = "Login obrigatório.")
    private String login;
    @NotNull(message = "Senha obrigatório.")
    private String password;
}
