package atos.net.pocLPI.action;

public abstract class BPGenericAction<T> implements ExtendedAction<T> {
	protected String actionName;
	
	public BPGenericAction(String actionName) {
        this.actionName = actionName;
    }
}
