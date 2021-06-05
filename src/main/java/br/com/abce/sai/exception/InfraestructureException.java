package br.com.abce.sai.exception;

import java.io.IOException;

public class InfraestructureException extends RuntimeException {

    public InfraestructureException(IOException e) {
        super("Falha na aplicação, por favor, acione o suporte técnico.");
    }
}
