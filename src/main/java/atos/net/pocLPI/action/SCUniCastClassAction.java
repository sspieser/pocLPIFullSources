package atos.net.pocLPI.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author sspieser
 *
 * @param <T>
 */
public class SCUniCastClassAction<T> extends BPGenericAction<T> implements ExtendedAction<T> {
	
	private final Logger logger = LoggerFactory.getLogger(SCUniCastClassAction.class);
	
	public SCUniCastClassAction(String actionName) {
		super(actionName);
    }	
	@Override
	public void execute(T stateful, String event, Object... args) throws ActionException {
		switch (this.actionName) {
			case "Certifier()":
				certifier(stateful, event, args);
				break;
			case "Enregistrer Demande()":
				enregDmde(stateful, event, args);
				break;
			case "TraiterRequeteSC()":
				traiterReqSC(stateful, event, args);
				break;
			case "MajDonneesOP()":
			case "Evt()":
			case "NotifDSO()":
				autreActions(stateful, event, args);
				break;
			default:
				throw new IllegalStateException(this.actionName);
		}
		
	}

	public String executeNonDeterministic(T stateful, String event, Object... args) throws ActionException {
		String ret = null;
		switch (this.actionName) {
			case "DUMMY_ACTION()":
				logger.debug(" **** DUMMY_ACTION() *****");
				ret = dummy_action(stateful, event, args);
				break;
			default:
				throw new IllegalStateException(this.actionName);
		}
		return (ret);
	}

	private void autreActions(T stateful, String event, Object[] args) throws ActionException {
		try {
			System.out.println(">>" + this.actionName + ": event=" + event);
			if (event.equals("IMM.RepReqSC")) {
				System.out.println("... EXCEPTION!");
				throw new Exception("Test action en erreur");
			}
		} catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
	}
	private static void certifier(Object stateful, String event, Object[] args) {
		System.out.println(">>Certifier(): event=" + event);
	}
	private static void enregDmde(Object stateful, String event, Object[] args) {
		System.out.println(">>Enregistrer Demande(): event=" + event);
	}
	private static void traiterReqSC(Object stateful, String event, Object[] args) {
		System.out.println(">>TraiterRequeteSC(): event=" + event);
	}
	private static String dummy_action(Object stateful, String event, Object[] args) {
		System.out.println(">>DUMMY_ACTION(): event=" + event);
		
		return "En_Execution";
	}
}
