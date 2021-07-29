package br.com.abce.sai.persistence.model;

public class Const {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_CLIENT = "ROLE_CLIENT";
    public static final Integer MIN_WITH = 50;
    public static final Integer MIN_HEIGHT = 50;
    public static final Integer MIN_PAGINATION = 2;

    public static final String SEM_RECUPERACAO_SENHA = "0";
    public static final String EM_PROCESSO_RECUPERACAO_SENHA = "0";

    public static final String ASSUNTO_SENHA_PROVISORIA = "Senha provisória";
    public static final String CORPO_EMAIL_SENHA_PROVISORIA = "Olá %s,\n" +
            "\n" +
            "Uma senha provisória foi gerada para você acessar o sistema.\n" +
            "\n" +
            "\tsenha: %s\n" +
            "\n" +
            "\n" +
            "Segue o link para acesso ao sistema:\n" +
            "\n" +
            "\t%s" +
            "\n" +
            "\n" +
            "Favor não responder, e-mail automático.";

    public static final String ASSUNTO_EMAIL_NOVO_USUARIO= "Criação de novo usuário";
    public static final String CORPO_EMAIL_NOVO_USUARIO  = "Olá %s,\n" +
            "\n" +
            "O seu usuário foi criado para acesso ao sistema. Segue abaixo a seus dados de acesso:\n" +
            "\n" +
            "\tusuário: %s\n" +
            "\tsenha: %s\n" +
            "\n" +
            "\n" +
            "Segue o link para acesso ao sistema:\n" +
            "\n" +
            "\t%s" +
            "\n" +
            "\n" +
            "Favor não responder, e-mail automático.";

    public static final String ASSUNTO_TROCA_SENHA = "Alteração de senha de usuário";
    public static final String CORPO_EMAIL_TROCA_SENHA = "Olá %s,\n" +
            "\n" +
            "Estamos apenas lhe informando que a sua senha foi alterada recentemente no sistema.\n" +
            "\n" +
            "Favor não responder, e-mail automático.";

    public static final String USUARIO_STATUS_NAO_AUTORIZADO = "0";
    public static final String USUARIO_EMAIL_NAO_VERIFICADO = "0";
}
