package atos.net.pocLPI.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.statefulj.fsm.FSM;
import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.TooBusyException;
import org.statefulj.fsm.UnexpectedEventException;
import org.statefulj.fsm.model.Action;
import org.statefulj.fsm.model.State;
import org.statefulj.fsm.model.StateActionPair;
import org.statefulj.fsm.model.Transition;
import org.statefulj.fsm.model.impl.CompositeActionImpl;
import org.statefulj.fsm.model.impl.StateActionPairImpl;
import org.statefulj.fsm.model.impl.StateImpl;
import org.statefulj.persistence.memory.MemoryPersisterImpl;

import atos.net.pocLPI.StateManager;
import atos.net.pocLPI.action.ActionException;
import atos.net.pocLPI.action.BPGenericAction;
import atos.net.pocLPI.utils.exception.FSMActionException;
import atos.net.pocLPI.utils.exception.FSMUnexpectedEventException;

public class FSMManager {
	final static String ACTION_SPLITTER = "#";
	
	String bpType = null;
	FSMParametersSingleton singleton = null;
	List<String[]> setOfStates = null;
	HashMap<String, String> mapEvents = new HashMap<String, String>();
	final HashMap<String, State<StateManager>> mapStates = new HashMap<String, State<StateManager>>();
	final HashMap<String, Action<StateManager>> mapActions = new HashMap<String, Action<StateManager>>();
	String etatStart = null;
	FSM<StateManager> sessionFSM = null;
	StateManager sm = new StateManager();

	/**
	 * Constructor for a given BP type
	 * note: pas de gestion de la transition de fin
	 * 
	 * @param bpType
	 */
	public FSMManager(String bpType) {
		this.bpType = bpType;
		singleton = FSMParametersSingleton.INSTANCE;
		setOfStates = singleton.getStates(bpType);
	}

	/**
	 * Init FSM data structure
	 * 
	 * @param currentState if not null moves machine state to this given state
	 */
	public void init(String currentState) {

		// chargement des Event, Actions et States
		doEventActionState();

		// mise en place des transitions
		doTransitions();
		
		// In-Memory Persister
        List<State<StateManager>> states = new LinkedList<State<StateManager>>();
        for (State<StateManager> value : mapStates.values()) {
        	states.add(value);
        }
		State<StateManager> etatStartClass = mapStates.get(etatStart);
		MemoryPersisterImpl<StateManager> persister = new MemoryPersisterImpl<StateManager>(states, etatStartClass);
		
        // FSM
		final FSM<StateManager> fsm = new FSM<StateManager>("StateManager FSM", persister);
        
		this.sessionFSM = fsm;
		
		// set the current state 
		if (currentState != null) {
			this.sessionFSM.getPersister().setStartState(mapStates.get(currentState));
		}
	}
	/**
	 * Init FSM data structure; moves to default start state
	 * 
	 */
	public void init() {
		init(null);
	}
	/**
	 * Send an event to the machine
	 * 
	 * @param eventString
	 * @return
	 * @throws TooBusyException
	 * @throws FSMActionException
	 * @throws FSMUnexpectedEventException 
	 */
	public State<StateManager> sendEvent(String eventString) throws TooBusyException, FSMActionException, FSMUnexpectedEventException {
		State<StateManager> state = null;
		try {
			state = sessionFSM.onEvent(sm, eventString);
		} catch (ActionException a) {
			throw new FSMActionException(a);
		} catch (UnexpectedEventException e) {
			throw new FSMUnexpectedEventException(e);
		}
		return state;
	}
	public State<StateManager> getCurrentState() {
		return sessionFSM.getCurrentState(sm);
	}
	public String getCurrentStateName() {
		return sessionFSM.getCurrentState(sm).getName();
	}
	/**
	 * Ne semble pas marcher??
	 * TODO
	 * 
	 * @param state
	 */
	public void setState(String state) {
		if (state != null) {
			State<StateManager> stateMgr = mapStates.get(state);
			if (stateMgr != null)
				this.sessionFSM.getPersister().setStartState(mapStates.get(state));
		}
	}
	
	private void doEventActionState() {
		Iterator<String[]> iterator = setOfStates.iterator();
		while (iterator.hasNext()) {
			String[] fields = iterator.next();

			String etatIn = fields[1];
			String event = fields[2];
			String action = fields[3];
			String toStart = fields[5];

			if (!mapEvents.containsKey(event))
				mapEvents.put(event, event);
			if (!mapStates.containsKey(etatIn)) {
				final State<StateManager> stateA = new StateImpl<StateManager>(etatIn);
				mapStates.put(etatIn, stateA);
			}
			String classNm = BPGenericAction.class.getPackage().getName() + "." + this.bpType + "ClassAction";
			if (action.contains(ACTION_SPLITTER)) { // cas multi actions
				for (String retval: action.split(ACTION_SPLITTER)) {
					if (!mapActions.containsKey(retval)) {
						Action<StateManager> a = FSMUtils.loadActionClass(classNm, retval);
						
						mapActions.put(retval, a);
					}
				}
			} else {
				if (!mapActions.containsKey(action)) {
					Action<StateManager> a = FSMUtils.loadActionClass(classNm, action);
					
					mapActions.put(action, a);
				}
			}
			
			if ("1".equals(toStart)) {
				etatStart = etatIn;
			}
		}		
	}
	private void doTransitions() {
		Iterator<String[]> iter = setOfStates.iterator();
		while (iter.hasNext()) {
			String[] fields = iter.next();

			String etatIn = fields[1];
			String event = fields[2];
			final String action = fields[3];
			String etatOut = fields[4];
			// System.out.println(etatIn + "|" + event + "|" + action + "|" + etatOut + "|" + toStart);

			// Cas de transition deterministe ou non?
			if (etatOut != null && !"".equals(etatOut)) { // deterministic
				State<StateManager> stateA = mapStates.get(etatIn);
				String eventA = mapEvents.get(event);
				State<StateManager> stateB = mapStates.get(etatOut);
				
				Action<StateManager> actionA = null;
				if (action.contains(ACTION_SPLITTER)) { // cas multi actions
					List<Action<StateManager>> listActions = new ArrayList<Action<StateManager>>();
					for (String retval: action.split(ACTION_SPLITTER)) {
						listActions.add(mapActions.get(retval));
					}
					actionA = new CompositeActionImpl<StateManager>(listActions);
					//throw new NotImplementedException("cas multi actions");
				} else {
					actionA = mapActions.get(action);
				}
				
				stateA.addTransition(eventA, stateB, actionA);

				// System.out.println("Transition: si event '" + eventA + "' sur '" +
				// stateA.getName() + "' alors etat sortie '" + stateB.getName() + "'");
				
			} else { // non-deterministic
				State<StateManager> stateA = mapStates.get(etatIn);
				String eventA = mapEvents.get(event);

				stateA.addTransition(eventA, new Transition<StateManager>() {
					
					private final Logger logger = LoggerFactory.getLogger(Transition.class);
					
					@Override
					public StateActionPair<StateManager> getStateActionPair(StateManager stateful, String event,
							Object... args) throws RetryException {
						State<StateManager> next = null;

						String classNm = BPGenericAction.class.getPackage().getName() + "." + bpType + "ClassAction";
						String param1 = action;
						Action<StateManager> specificAction = FSMUtils.loadActionClass(classNm, param1);
						
						String nextStateString = ((BPGenericAction<StateManager>) specificAction).executeNonDeterministic(stateful, event, args);
						next = mapStates.get(nextStateString);
						
						logger.debug(" **** Non deterministic: from getStateActionPair() customise... *****");

						return new StateActionPairImpl<StateManager>(next, null /*specificAction*/);
					}
				});
			}
		}		
	}
}
