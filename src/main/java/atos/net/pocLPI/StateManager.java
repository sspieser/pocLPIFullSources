package atos.net.pocLPI;

import org.statefulj.persistence.annotations.State;

/**
 * 
 * @author sspieser
 *
 */
public class StateManager {
	@State
	String state;
	
	public String getState() {
		return state;
	}
}
