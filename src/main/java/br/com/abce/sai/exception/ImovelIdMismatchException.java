package br.com.abce.sai.exception;

public class ImovelIdMismatchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 37626617294116711L;
	
	public ImovelIdMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public ImovelIdMismatchException() {
		super();
	}

}
