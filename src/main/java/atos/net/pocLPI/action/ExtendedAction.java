package atos.net.pocLPI.action;

import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.model.Action;

public interface ExtendedAction<T> extends Action<T> {

	/**
	 * Called to execute an action based off a State Transition.
	 *
	 * @param stateful The Stateful Entity
	 * @param event The ocurring Event
	 * @param args A set of optional arguments passed into the onEvent method of the {@link org.statefulj.fsm.FSM}
	 * @throws RetryException thrown when the event must be retried due to Stale state or some other error condition
	 */
	void execute(T stateful, String event, Object ... args) throws RetryException, ActionException;
	
	/**
	 * Gestion des cas non deterministes
	 * 
	 * @param stateful
	 * @param event
	 * @param args
	 * @return
	 */
	String executeNonDeterministic(T stateful, String event, Object... args) throws ActionException;

}