package br.com.abce.sai.exception;

public class ResourcedMismatchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 37626617294116711L;
	
	public ResourcedMismatchException(Long id) {
		super(String.format("Recurso %s não cadastrado.", id));
	}
}
