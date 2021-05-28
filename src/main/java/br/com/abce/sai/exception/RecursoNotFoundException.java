package br.com.abce.sai.exception;

public class RecursoNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8889672697231051666L;

	public RecursoNotFoundException(Class classe, Object id) {
		super(String.format("%s %s não encontrado.", classe.getSimpleName(), id));
	}
}
