package br.com.abce.sai.persistence.model;

public enum AuthProvider {
    LOCAL,
    FACEBOOK,
    GOOGLE;

    public static AuthProvider get(String nome){

        for (AuthProvider authProvider : AuthProvider.values()) {
            if (authProvider.name().equals(nome.toUpperCase()))
                return authProvider;
        }

        return AuthProvider.LOCAL;
    }
}
