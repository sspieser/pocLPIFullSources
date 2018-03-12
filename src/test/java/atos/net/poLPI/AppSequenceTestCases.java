package atos.net.poLPI;

import org.statefulj.fsm.TooBusyException;

import atos.net.pocLPI.utils.FSMManager;
import atos.net.pocLPI.utils.exception.FSMException;
import atos.net.pocLPI.utils.exception.FSMUnexpectedEventException;
import junit.framework.TestCase;

public class AppSequenceTestCases extends TestCase {

	public void testSeqWithNonDeterministic() {
		FSMManager mgr = new FSMManager("SCUniCast");
		mgr.init();
		
		try {
			mgr.sendEvent("GDR.RequeteUniCast");
			assertEquals("En_Certif", mgr.getCurrentStateName());
	        mgr.sendEvent("EAM.CRCertification");
	        assertEquals("Dem_Enregistree", mgr.getCurrentStateName());
	        mgr.sendEvent("GDR.ExecuterDemande");
	        assertEquals("En_Execution", mgr.getCurrentStateName());
	        mgr.sendEvent("DUMMY_EVENT");
	        assertEquals("En_Certif", mgr.getCurrentStateName());
		} catch (FSMException e) {
			fail("Exception 'FSMException' inattendue: " + e.getMessage());
		} catch (TooBusyException e) {
			fail("Exception 'TooBusyException' inattendue: " + e.getMessage());
		}
	}
	public void testComposite() {
		FSMManager mgr = new FSMManager("SCUniCast");
		mgr.init("CR_Genere");
		
		try {
			mgr.sendEvent("IMM.RepReqSC");
			fail("Devrait lever 'Exception'");
		} catch (Exception e) {
			assert(e instanceof Exception);
			assertEquals("CR_Genere", mgr.getCurrentStateName());
		}
	}
}
