package gameworld;

/**
 * A parser exception is thrown when the parser encounters a problem 
 * with the format of the files
 * @author Jasen
 *
 */
public class ParserException extends Exception {

	private static final long serialVersionUID = -2216588336403050836L;
	
	public ParserException() {
		super();
	}
	
	public ParserException(String message) {
		super(message);
	}

}
