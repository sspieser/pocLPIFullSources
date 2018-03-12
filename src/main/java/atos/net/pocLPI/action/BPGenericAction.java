package atos.net.pocLPI.action;

import org.statefulj.fsm.model.Action;

public abstract class BPGenericAction<T> implements Action<T> /*ExtendedAction<T>*/ {
	protected String actionName;
	
	public BPGenericAction(String actionName) {
        this.actionName = actionName;
    }
}
