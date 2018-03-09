package atos.net.pocLPI.utils.exception;

public class FSMUnexpectedEventException extends FSMException {

	private static final long serialVersionUID = -4003705713754142416L;
	
	public FSMUnexpectedEventException(String arg0) {
		super(arg0);
	}
	public FSMUnexpectedEventException(Throwable arg0) {
		super(arg0);
	}
}
