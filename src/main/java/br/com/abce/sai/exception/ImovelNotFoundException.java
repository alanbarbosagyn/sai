package br.com.abce.sai.exception;

public class ImovelNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8889672697231051666L;
	
	public ImovelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

	public ImovelNotFoundException(Long id) {
		super("Não foi possível ");
	}
}
