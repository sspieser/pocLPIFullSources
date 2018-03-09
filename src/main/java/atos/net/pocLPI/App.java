package atos.net.pocLPI;

import org.statefulj.fsm.TooBusyException;

import atos.net.pocLPI.utils.FSMManager;
import atos.net.pocLPI.utils.exception.FSMActionException;
import atos.net.pocLPI.utils.exception.FSMException;
import atos.net.pocLPI.utils.exception.FSMUnexpectedEventException;

/**
 * 
 * @author sspieser
 *
 */
public class App {
	
	public static void main(String[] args) throws TooBusyException {
		
		//System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
		//System.setProperty(org.slf4j.impl.SimpleLogger.LOG_FILE_KEY, "poc.log");
		
		// Init stuff
		FSMManager mgr = new FSMManager("SCUniCast");
		mgr.init();
		
        //
        // C'est parti...
        //
		try {
	        System.out.println("Current state: " + mgr.getCurrentStateName());
	        
	        System.out.println("Envoi event " + "GDR.RequeteUniCast");
	        mgr.sendEvent("GDR.RequeteUniCast");
	        System.out.println("Current state: " + mgr.getCurrentStateName());
	        System.out.println("...");
	        System.out.println("Envoi event " + "EAM.CRCertification");
	        mgr.sendEvent("EAM.CRCertification");
	        System.out.println("Current state: " + mgr.getCurrentStateName());
	        System.out.println("...");
	        System.out.println("Envoi event " + "GDR.ExecuterDemande");
	        mgr.sendEvent("GDR.ExecuterDemande");
	        System.out.println("Current state: " + mgr.getCurrentStateName());
	        System.out.println("...");
	        System.out.println("Envoi event " + "DUMMY_EVENT");
	        mgr.sendEvent("DUMMY_EVENT");
	        System.out.println("Current state: " + mgr.getCurrentStateName());
		} catch (FSMException e) {
			System.out.println("***FSMActionException: " + e.getMessage());
		}
		
        // tester un cas composite Action
        System.out.println("... Test composite ...");
        mgr = new FSMManager("SCUniCast");
		mgr.init("CR_Genere");

        System.out.println("Current state: " + mgr.getCurrentStateName());
        System.out.println("Envoi event " + "IMM.RepReqSC");
        try {
			mgr.sendEvent("IMM.RepReqSC");
		} catch (FSMException e) {
			System.out.println("***FSMActionException: " + e.getMessage());
		}
        System.out.println("Current state: " + mgr.getCurrentStateName());
		
        // tester un cas de event non valide (ou autre, state inconnu...)
        System.out.println("... Test event invalide sur etat courant ...");
        mgr = new FSMManager("SCUniCast");
        mgr.init("Rep_Enregistree");
        // envoi d'un event valide mais pas sur cet etat
        System.out.println("Current state: " + mgr.getCurrentStateName());
        System.out.println("Envoi event " + "EAM.CRCertification");
        try {
			mgr.sendEvent("EAM.CRCertification");
		} catch (FSMActionException e) {
			System.out.println("***FSMActionException: " + e.getMessage());
		} catch (FSMUnexpectedEventException e) {
			System.out.println(">>> Event non supporté pour l'état: " + e.getMessage());
		}
        System.out.println("Current state: " + mgr.getCurrentStateName());
        
        // envoi d'un event inconnu
        System.out.println("Envoi event " + "AZERTY.QSD");
        try {
			mgr.sendEvent("AZERTY.QSD");
		} catch (FSMException e) {
			System.out.println("*** FSMException: " + e.getMessage());
		}
        System.out.println("Current state: " + mgr.getCurrentStateName());
        
	}
}
