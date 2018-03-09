package atos.net.pocLPI.action;

public class ActionException extends RuntimeException {
	
	private static final long serialVersionUID = -6993106868454109158L;
	
	public ActionException(String reason) {
		super(reason);
	}
}
