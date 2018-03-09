package org.statefulj.fsm;

/**
 * @author sspieser
 *
 */
public class UnexpectedEventException extends Exception {

	private static final long serialVersionUID = 5813853492292416450L;

	public UnexpectedEventException() {

	}

	public UnexpectedEventException(String arg0) {
		super(arg0);
	}
}
