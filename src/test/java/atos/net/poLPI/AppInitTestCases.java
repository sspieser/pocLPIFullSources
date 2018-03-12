package atos.net.poLPI;

import org.statefulj.fsm.TooBusyException;

import atos.net.pocLPI.utils.FSMManager;
import atos.net.pocLPI.utils.exception.FSMActionException;
import atos.net.pocLPI.utils.exception.FSMUnexpectedEventException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppInitTestCases extends TestCase {
	
	private FSMManager mgr = null;
	
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppInitTestCases(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppInitTestCases.class);
	}

	public void setUp() throws Exception {
		mgr = new FSMManager("SCUniCast");
		mgr.init();
	}
	public void tearDown() throws Exception {
		mgr = null;
	}
	public void testInitState() {
        assertEquals("Vide", mgr.getCurrentStateName());
    }
	public void testSendGDRRequeteUniCast() {
		try {
			mgr.sendEvent("GDR.RequeteUniCast");
			assertEquals("En_Certif", mgr.getCurrentStateName());
		} catch (FSMActionException e) {
			fail("Exception 'FSMActionException' inattendue: " + e.getMessage());
		} catch (FSMUnexpectedEventException e) {
			fail("Exception 'FSMUnexpectedEventException' inattendue: " + e.getMessage());
		} catch (TooBusyException e) {
			fail("Exception 'TooBusyException' inattendue: " + e.getMessage());
		}
	}
	public void testEventNotForState() {
		try {
			mgr.sendEvent("IMM.RepReqSC");
			fail("Devrait lever 'FSMUnexpectedEventException'");
		} catch (FSMActionException e) {
			fail("Exception 'FSMActionException' inattendue: " + e.getMessage());
		} catch (FSMUnexpectedEventException e) {
			assert(e instanceof FSMUnexpectedEventException);
		} catch (TooBusyException e) {
			fail("Exception 'TooBusyException' inattendue: " + e.getMessage());
		}	
	}
	public void testUnknownEvent() {
		try {
			mgr.sendEvent("AZERTY.QSD");
			fail("Devrait lever 'FSMUnexpectedEventException'");
		} catch (FSMActionException e) {
			fail("Exception 'FSMActionException' inattendue: " + e.getMessage());
		} catch (FSMUnexpectedEventException e) {
			assert(e instanceof FSMUnexpectedEventException);
		} catch (TooBusyException e) {
			fail("Exception 'TooBusyException' inattendue: " + e.getMessage());
		}	
	}
}
