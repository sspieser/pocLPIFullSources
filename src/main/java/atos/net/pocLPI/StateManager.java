package atos.net.pocLPI;

import org.statefulj.persistence.annotations.State;

/**
 * 
 * @author sspieser
 *
 */
public class StateManager {
	@State /*(accessorType=State.AccessorType.METHOD)*/
	String state;
	
	/**
	 * Setter pour l'etat; si present StatefulJ l'utilise, sinon travaille par inspection
	 * en l'ajoutant on peut changer l'etat a partir d'une action par exemple (cas non deterministe)
	 * 
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}
}
